# app.py
from fastapi import FastAPI, Request
from ultralytics import YOLO
from PIL import Image
import io

app = FastAPI()
model = YOLO("./best_models/best.pt")


@app.get("/health")
def health():
    """Return service health status."""
    return {"status": "ok"}


@app.post("/infer")
async def infer(request: Request):
    """
    Run object detection on an uploaded image.

    The request body must contain raw image bytes. The response contains a list
    of detections with class labels, confidence scores, and normalized bounding
    boxes in the range [0, 1].

    Bounding box format:
        bbox = {
            "xMin": normalized left coordinate,
            "yMin": normalized top coordinate,
            "xMax": normalized right coordinate,
            "yMax": normalized bottom coordinate,
        }
    """
    img_bytes = await request.body()
    img = Image.open(io.BytesIO(img_bytes)).convert("RGB")
    results = model.predict(img, verbose=False)

    dets = []
    r = results[0]
    names = r.names  # Dictionary mapping class IDs to labels.

    if r.boxes is not None:
        for b in r.boxes:
            cls_id = int(b.cls[0].item())
            conf = float(b.conf[0].item())

            x1, y1, x2, y2 = [float(v) for v in b.xyxyn[0].tolist()]

            dets.append({
                "clazz": names[cls_id],
                "confidence": conf,
                "bbox": {
                    "xMin": x1,
                    "yMin": y1,
                    "xMax": x2,
                    "yMax": y2,
                },
            })

    return {"detections": dets}

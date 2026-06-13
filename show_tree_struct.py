#!/usr/bin/env python3

from pathlib import Path
import argparse


def list_items_recursive(
    folder_path: Path,
    output_file: Path,
    mode: str,
    relative: bool
) -> None:
    if not folder_path.exists():
        raise FileNotFoundError(f"Folder does not exist: {folder_path}")

    if not folder_path.is_dir():
        raise NotADirectoryError(f"Path is not a folder: {folder_path}")

    folder_path = folder_path.resolve()

    with output_file.open("w", encoding="utf-8") as f:
        for item_path in folder_path.rglob("*"):
            if mode == "files" and not item_path.is_file():
                continue

            if mode == "dirs" and not item_path.is_dir():
                continue

            if relative:
                path_to_write = item_path.relative_to(folder_path)
            else:
                path_to_write = item_path.resolve()

            f.write(str(path_to_write) + "\n")


def main() -> None:
    parser = argparse.ArgumentParser(
        description="Recursively list files or directories in a folder and save them to a file."
    )

    parser.add_argument(
        "folder",
        help="Folder to scan recursively"
    )

    parser.add_argument(
        "-o",
        "--output",
        default="items_list.txt",
        help="Output file path. Default: items_list.txt"
    )

    parser.add_argument(
        "-m",
        "--mode",
        choices=["files", "dirs"],
        default="files",
        help="What to list: files or dirs. Default: files"
    )

    parser.add_argument(
        "-r",
        "--relative",
        action="store_true",
        help="Write paths relative to the scanned folder"
    )

    args = parser.parse_args()

    list_items_recursive(
        Path(args.folder),
        Path(args.output),
        args.mode,
        args.relative
    )

    print(f"Saved {args.mode} list to: {args.output}")


if __name__ == "__main__":
    main()

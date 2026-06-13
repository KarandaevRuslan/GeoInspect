#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import sys
import shutil
import subprocess
from pathlib import Path


# =========================================================
# CONFIG
# This script should be placed in the Gradle project root.
# Example:
#
# proj/
#   android_sign_cli.py
#   settings.gradle
#   build.gradle
#   gradlew.bat
#   app/
# =========================================================

PROJECT_ROOT = Path(__file__).resolve().parent

# ---------------------------------------------------------
# Explicit tool paths
# Change these paths for your machine.
# ---------------------------------------------------------

GRADLEW = PROJECT_ROOT / "gradlew.bat"

ZIPALIGN = Path(
    r"C:\Users\secre\AppData\Local\Android\Sdk\build-tools\36.0.0\zipalign.exe"
)

APKSIGNER = Path(
    r"C:\Users\secre\AppData\Local\Android\Sdk\build-tools\36.0.0"
    r"\apksigner.bat"
)

KEYTOOL = "keytool"

# ---------------------------------------------------------
# Gradle build config
# ---------------------------------------------------------

GRADLE_TASK = ":app:assembleUnsignedRelease"

RUN_CLEAN_BEFORE_BUILD = False

# If empty, the script will try to find the newest APK after Gradle build.
# Example:
# INPUT_APK_TO_SIGN = PROJECT_ROOT / "app/build/outputs/apk/release/app-release-unsigned.apk"
INPUT_APK_TO_SIGN = PROJECT_ROOT / "app" / "build" / "outputs" / \
    "apk" / "unsignedRelease" / "app-unsignedRelease-unsigned.apk"

APK_SEARCH_DIRS = [
    PROJECT_ROOT / "app" / "build" / "outputs" / "apk",
    PROJECT_ROOT / "build" / "outputs" / "apk",
]

# ---------------------------------------------------------
# Output config
# ---------------------------------------------------------

OUTPUT_DIR = PROJECT_ROOT / "build_out"
OLD_KEYS_DIR = OUTPUT_DIR / "old_keys"

ALIGNED_APK_NAME = "app-aligned.apk"
SIGNED_APK_NAME = "app-signed.apk"

# ---------------------------------------------------------
# Keystore config
# ---------------------------------------------------------

KEYSTORE_PROPERTIES_FILE = PROJECT_ROOT / "keystore.properties"

KEY_DNAME = "CN=Proj Release,O=Ruslan Karandaev,OU=Android,C=US"

KEY_ALG = "RSA"
KEY_SIZE = 2048
KEY_VALIDITY = 10000

# If True, existing keystore is moved to old_keys before creating a new one.
# It is never overwritten.
BACKUP_EXISTING_KEYSTORE_ON_GENERATE = True

# ---------------------------------------------------------
# Signing config
# ---------------------------------------------------------

DO_VERIFY_AFTER_SIGN = True


# =========================================================
# HELPERS
# =========================================================

def is_windows():
    return os.name == "nt"


def print_header(title):
    print()
    print("=" * 60)
    print(title)
    print("=" * 60)


def ensure_dirs():
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    OLD_KEYS_DIR.mkdir(parents=True, exist_ok=True)


def load_properties(path):
    """
    Loads simple .properties file.

    Supported format:
      key=value

    Empty lines and comments starting with # are ignored.
    """
    props = {}

    if not path.exists():
        raise RuntimeError(
            f"Properties file was not found: {path}\n"
            f"Create keystore.properties in the project root."
        )

    with path.open("r", encoding="utf-8") as file:
        for line_number, raw_line in enumerate(file, start=1):
            line = raw_line.strip()

            if not line or line.startswith("#"):
                continue

            if "=" not in line:
                raise RuntimeError(
                    f"Invalid line in {path} at line {line_number}: "
                    f"{raw_line.rstrip()}"
                )

            key, value = line.split("=", 1)
            props[key.strip()] = value.strip()

    return props


def get_keystore_config():
    """
    Reads keystore settings from keystore.properties.
    """
    props = load_properties(KEYSTORE_PROPERTIES_FILE)

    required_keys = [
        "storeFile",
        "storePassword",
        "keyAlias",
        "keyPassword",
    ]

    missing_keys = [
        key for key in required_keys
        if not props.get(key)
    ]

    if missing_keys:
        raise RuntimeError(
            f"Missing required keys in {KEYSTORE_PROPERTIES_FILE}: "
            f"{', '.join(missing_keys)}"
        )

    store_file = Path(props["storeFile"])

    if not store_file.is_absolute():
        store_file = PROJECT_ROOT / store_file

    return {
        "storeFile": store_file,
        "storePassword": props["storePassword"],
        "keyAlias": props["keyAlias"],
        "keyPassword": props["keyPassword"],
    }


def get_keystore_file():
    return get_keystore_config()["storeFile"]


def mask_secret(value):
    value = str(value)

    if not value:
        return value

    if value.startswith("pass:"):
        return "pass:***"

    return "***"


def printable_command(command):
    masked_command = []
    secret_flags = {
        "-storepass",
        "-keypass",
        "--ks-pass",
        "--key-pass",
    }

    mask_next = False

    for item in command:
        item_str = str(item)

        if mask_next:
            masked_command.append(mask_secret(item_str))
            mask_next = False
            continue

        masked_command.append(item_str)

        if item_str in secret_flags:
            mask_next = True

    return " ".join(
        f'"{x}"' if " " in x else x
        for x in masked_command
    )


def resolve_tool(tool):
    """
    Resolves either a full path or a command from PATH.
    """
    if isinstance(tool, Path):
        if tool.exists():
            return str(tool)
        return None

    tool_path = Path(str(tool))
    if tool_path.exists():
        return str(tool_path)

    found = shutil.which(str(tool))
    if found:
        return found

    return None


def require_tool(tool, name):
    resolved = resolve_tool(tool)

    if not resolved:
        raise RuntimeError(
            f"{name} was not found: {tool}\n"
            f"Set the correct path in the config section."
        )

    return resolved


def run_command(command, cwd=None):
    print(f"> {printable_command(command)}")

    first = str(command[0]).lower()
    use_shell = is_windows() and (
        first.endswith(".bat") or first.endswith(".cmd")
    )

    if use_shell:
        command_line = subprocess.list2cmdline([str(x) for x in command])
        result = subprocess.run(
            command_line,
            cwd=str(cwd) if cwd else None,
            shell=True,
        )
    else:
        result = subprocess.run(
            [str(x) for x in command],
            cwd=str(cwd) if cwd else None,
            shell=False,
        )

    if result.returncode != 0:
        raise RuntimeError(
            f"Command failed with exit code: {result.returncode}")


def make_non_conflicting_path(directory, filename):
    """
    Returns a path that does not exist.

    Example:
      release.keystore
      release_1.keystore
      release_2.keystore
      release_123.keystore
    """
    original = Path(filename)
    stem = original.stem
    suffix = original.suffix

    candidate = directory / original.name

    if not candidate.exists():
        return candidate

    counter = 1

    while True:
        candidate = directory / f"{stem}_{counter}{suffix}"

        if not candidate.exists():
            return candidate

        counter += 1


def backup_existing_keystore():
    """
    Moves existing keystore to old_keys without overwriting anything.
    """
    keystore_file = get_keystore_file()

    if not keystore_file.exists():
        return None

    ensure_dirs()

    backup_path = make_non_conflicting_path(
        OLD_KEYS_DIR,
        keystore_file.name,
    )

    print(f"Existing keystore found: {keystore_file}")
    print(f"Moving old keystore to: {backup_path}")

    shutil.move(str(keystore_file), str(backup_path))

    return backup_path

# =========================================================
# TOOL CHECKS
# =========================================================


def check_tools():
    print_header("Checking tools")

    gradlew = require_tool(GRADLEW, "Gradle wrapper")
    zipalign = require_tool(ZIPALIGN, "zipalign")
    apksigner = require_tool(APKSIGNER, "apksigner")
    keytool = require_tool(KEYTOOL, "keytool")

    print(f"[OK] Gradle wrapper: {gradlew}")
    print(f"[OK] zipalign      : {zipalign}")
    print(f"[OK] apksigner     : {apksigner}")
    print(f"[OK] keytool       : {keytool}")


# =========================================================
# KEYSTORE GENERATION
# =========================================================

def generate_keystore():
    """
    Generates a new keystore.

    If a keystore already exists, it is moved to:
      build_out/old_keys/

    Existing backups are never overwritten.
    """
    print_header("Generating keystore")

    ensure_dirs()

    config = get_keystore_config()

    keystore_file = config["storeFile"]
    key_alias = config["keyAlias"]
    keystore_pass = config["storePassword"]
    key_pass = config["keyPassword"]

    keytool = require_tool(KEYTOOL, "keytool")

    if keystore_file.exists():
        if BACKUP_EXISTING_KEYSTORE_ON_GENERATE:
            backup_existing_keystore()
        else:
            raise RuntimeError(
                f"Keystore already exists: {keystore_file}\n"
                "Set BACKUP_EXISTING_KEYSTORE_ON_GENERATE = True "
                "to move it to old_keys."
            )

    command = [
        keytool,
        "-genkeypair",
        "-v",
        "-keystore", str(keystore_file),
        "-storepass", keystore_pass,
        "-alias", key_alias,
        "-keypass", key_pass,
        "-keyalg", KEY_ALG,
        "-keysize", str(KEY_SIZE),
        "-validity", str(KEY_VALIDITY),
        "-dname", KEY_DNAME,
    ]

    run_command(command, cwd=PROJECT_ROOT)

    if not keystore_file.exists():
        raise RuntimeError(f"Keystore was not created: {keystore_file}")

    print()
    print("[DONE] Keystore generated successfully.")
    print(f"Keystore: {keystore_file}")


# =========================================================
# GRADLE BUILD
# =========================================================


def build_gradle_apk():
    print_header("Building APK with Gradle")

    gradlew = require_tool(GRADLEW, "Gradle wrapper")

    if RUN_CLEAN_BEFORE_BUILD:
        run_command([gradlew, "clean"], cwd=PROJECT_ROOT)

    run_command([gradlew, GRADLE_TASK], cwd=PROJECT_ROOT)

    apk = find_built_apk()

    print()
    print("[DONE] APK build finished.")
    print(f"APK: {apk}")

    return apk


def find_built_apk():
    if INPUT_APK_TO_SIGN:
        apk = Path(INPUT_APK_TO_SIGN)

        if not apk.is_absolute():
            apk = PROJECT_ROOT / apk

        if not apk.exists():
            raise RuntimeError(f"Input APK was not found: {apk}")

        return apk

    all_apks = []

    for search_dir in APK_SEARCH_DIRS:
        if search_dir.exists():
            all_apks.extend(search_dir.rglob("*.apk"))

    if not all_apks:
        raise RuntimeError(
            "No APK file was found after Gradle build.\n"
            "Set INPUT_APK_TO_SIGN manually in the config section."
        )

    unsigned_apks = [
        apk for apk in all_apks
        if "unsigned" in apk.name.lower()
    ]

    if unsigned_apks:
        return max(unsigned_apks, key=lambda p: p.stat().st_mtime)

    newest_apk = max(all_apks, key=lambda p: p.stat().st_mtime)

    print()
    print("[WARN] No APK with 'unsigned' in the file name was found.")
    print(f"[WARN] The newest APK will be used: {newest_apk}")
    print(
        "[WARN] If this APK is already signed by Gradle, signing may fail "
        "or produce an unwanted result."
    )

    return newest_apk


# =========================================================
# APK ALIGNING AND SIGNING
# =========================================================

def align_apk(input_apk):
    print_header("Aligning APK")

    ensure_dirs()

    zipalign = require_tool(ZIPALIGN, "zipalign")
    aligned_apk = OUTPUT_DIR / ALIGNED_APK_NAME

    if aligned_apk.exists():
        aligned_apk.unlink()

    command = [
        zipalign,
        "-p",
        "-f",
        "4",
        str(input_apk),
        str(aligned_apk),
    ]

    run_command(command, cwd=PROJECT_ROOT)

    if not aligned_apk.exists():
        raise RuntimeError(f"Aligned APK was not created: {aligned_apk}")

    print()
    print("[DONE] APK aligned successfully.")
    print(f"Aligned APK: {aligned_apk}")

    return aligned_apk


def sign_apk(input_apk=None):
    """
    Signs an existing APK with the existing keystore.

    This function does not generate a keystore.
    """
    print_header("Signing APK")

    ensure_dirs()

    config = get_keystore_config()

    keystore_file = config["storeFile"]
    key_alias = config["keyAlias"]
    keystore_pass = config["storePassword"]
    key_pass = config["keyPassword"]

    if not keystore_file.exists():
        raise RuntimeError(
            f"Keystore was not found: {keystore_file}\n"
            f"Generate a keystore first using the menu or:\n"
            f"  python android_sign_cli.py genkey"
        )

    if input_apk is None:
        input_apk = find_built_apk()

    aligned_apk = align_apk(input_apk)

    apksigner = require_tool(APKSIGNER, "apksigner")
    signed_apk = OUTPUT_DIR / SIGNED_APK_NAME

    if signed_apk.exists():
        signed_apk.unlink()

    command = [
        apksigner,
        "sign",
        "--ks", str(keystore_file),
        "--ks-key-alias", key_alias,
        "--ks-pass", f"pass:{keystore_pass}",
        "--key-pass", f"pass:{key_pass}",
        "--out", str(signed_apk),
        str(aligned_apk),
    ]

    run_command(command, cwd=PROJECT_ROOT)

    if not signed_apk.exists():
        raise RuntimeError(f"Signed APK was not created: {signed_apk}")

    print()
    print("[DONE] APK signed successfully.")
    print(f"Signed APK: {signed_apk}")

    if DO_VERIFY_AFTER_SIGN:
        verify_apk(signed_apk)

    return signed_apk


def verify_apk(apk):
    print_header("Verifying APK signature")

    apksigner = require_tool(APKSIGNER, "apksigner")

    if not Path(apk).exists():
        raise RuntimeError(f"APK was not found: {apk}")

    command = [
        apksigner,
        "verify",
        "--verbose",
        str(apk),
    ]

    run_command(command, cwd=PROJECT_ROOT)

    print()
    print("[DONE] APK signature verified successfully.")


# =========================================================
# FULL PIPELINE
# =========================================================

def full_build_align_sign():
    """
    Full pipeline:

      Gradle build -> zipalign -> apksigner

    Important:
      This function does not generate a keystore.
      The keystore must already exist.
    """
    print_header("Full pipeline: build -> align -> sign")

    keystore_file = get_keystore_file()

    if not keystore_file.exists():
        raise RuntimeError(
            f"Keystore was not found: {keystore_file}\n"
            f"Full pipeline does not generate keys.\n"
            f"Generate a keystore first using:\n"
            f"  python android_sign_cli.py genkey"
        )

    apk = build_gradle_apk()
    signed_apk = sign_apk(apk)

    print_header("DONE")
    print(f"Input APK : {apk}")
    print(f"Keystore  : {keystore_file}")
    print(f"Signed APK: {signed_apk}")


# =========================================================
# MENU
# =========================================================

def print_menu():
    print()
    print("Android Gradle APK Signing CLI")
    print("-" * 45)
    print("1. Check tools")
    print("2. Generate keystore")
    print("3. Build APK with Gradle")
    print("4. Sign existing APK")
    print("5. Full pipeline: build, align and sign")
    print("6. Verify signed APK")
    print("0. Exit")
    print("-" * 45)


def menu_loop():
    while True:
        print_menu()

        choice = input("Select option: ").strip()

        try:
            if choice == "1":
                check_tools()

            elif choice == "2":
                generate_keystore()

            elif choice == "3":
                build_gradle_apk()

            elif choice == "4":
                sign_apk()

            elif choice == "5":
                full_build_align_sign()

            elif choice == "6":
                signed_apk = OUTPUT_DIR / SIGNED_APK_NAME
                verify_apk(signed_apk)

            elif choice == "0":
                print("Exit.")
                return

            else:
                print("Unknown menu option.")

        except Exception as error:
            print()
            print(f"[ERROR] {error}")

        input("\nPress Enter to continue...")


# =========================================================
# CLI ENTRYPOINT
# =========================================================

def main():
    """
    Menu mode:

      python android_sign_cli.py

    Direct commands:

      python android_sign_cli.py check
      python android_sign_cli.py genkey
      python android_sign_cli.py build
      python android_sign_cli.py sign
      python android_sign_cli.py full
      python android_sign_cli.py verify
    """
    if len(sys.argv) <= 1:
        menu_loop()
        return

    command = sys.argv[1].lower().strip()

    try:
        if command == "check":
            check_tools()

        elif command == "genkey":
            generate_keystore()

        elif command == "build":
            build_gradle_apk()

        elif command == "sign":
            sign_apk()

        elif command == "full":
            full_build_align_sign()

        elif command == "verify":
            verify_apk(OUTPUT_DIR / SIGNED_APK_NAME)

        else:
            print(f"Unknown command: {command}")
            sys.exit(1)

    except Exception as error:
        print()
        print(f"[ERROR] {error}")
        sys.exit(1)


if __name__ == "__main__":
    main()

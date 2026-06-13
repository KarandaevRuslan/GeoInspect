from __future__ import annotations

import subprocess
from pathlib import Path
from typing import Final


OPENSSL_COMMAND: Final[str] = "openssl"

KEY_FILE: Final[Path] = Path("key.pem")
CERT_FILE: Final[Path] = Path("cert.pem")
KEYSTORE_FILE: Final[Path] = Path("keystore.p12")
SAN_CONFIG_FILE: Final[Path] = Path("san.cnf")

RSA_BITS: Final[int] = 2048
CERT_VALID_DAYS: Final[int] = 365
PKCS12_ALIAS: Final[str] = "springboot"

MENU_TITLE: Final[str] = "HTTPS Certificate CLI"
MENU_OPTION_GENERATE_CERT: Final[str] = "1"
MENU_OPTION_GENERATE_KEYSTORE: Final[str] = "2"
MENU_OPTION_INSPECT_CERT: Final[str] = "3"
MENU_OPTION_RUN_ALL: Final[str] = "4"
MENU_OPTION_EXIT: Final[str] = "0"


def run_command(command: list[str]) -> bool:
    """Run a shell command safely without shell=True."""
    print("\nRunning command:")
    print(" ".join(command))
    print()

    try:
        subprocess.run(command, check=True)
        print("\nDone.\n")
        return True
    except FileNotFoundError:
        print(
            f"\nError: '{OPENSSL_COMMAND}' was not found. Make sure "
            f"OpenSSL is installed and added to PATH.\n")
        return False
    except subprocess.CalledProcessError as error:
        print(f"\nError: command failed with exit code {error.returncode}.\n")
        return False


def generate_certificate() -> bool:
    """Generate a private key and a self-signed certificate using san.cnf."""
    command: list[str] = [
        OPENSSL_COMMAND,
        "req",
        "-x509",
        "-newkey",
        f"rsa:{RSA_BITS}",
        "-keyout",
        str(KEY_FILE),
        "-out",
        str(CERT_FILE),
        "-days",
        str(CERT_VALID_DAYS),
        "-nodes",
        "-config",
        str(SAN_CONFIG_FILE),
    ]

    return run_command(command)


def generate_keystore() -> bool:
    """Generate a PKCS12 keystore from cert.pem and key.pem."""
    print("OpenSSL will ask you to enter and confirm the export password.")
    print(
        "Use the same password later in Spring Boot as "
        "'server.ssl.key-store-password'."
    )

    command: list[str] = [
        OPENSSL_COMMAND,
        "pkcs12",
        "-export",
        "-in",
        str(CERT_FILE),
        "-inkey",
        str(KEY_FILE),
        "-out",
        str(KEYSTORE_FILE),
        "-name",
        PKCS12_ALIAS,
    ]

    return run_command(command)


def inspect_certificate() -> bool:
    """Print certificate details."""
    command: list[str] = [
        OPENSSL_COMMAND,
        "x509",
        "-in",
        str(CERT_FILE),
        "-text",
        "-noout",
    ]

    return run_command(command)


def run_all_steps() -> bool:
    """Run all certificate steps sequentially."""
    print("\nRunning all steps in order:")
    print("1. Generate private key and certificate")
    print("2. Generate PKCS12 keystore")
    print("3. Inspect certificate\n")

    if not generate_certificate():
        print("Stopped: certificate generation failed.\n")
        return False

    if not generate_keystore():
        print("Stopped: keystore generation failed.\n")
        return False

    if not inspect_certificate():
        print("Stopped: certificate inspection failed.\n")
        return False

    print("All steps completed successfully.\n")
    return True


def print_menu() -> None:
    """Print the main CLI menu."""
    print(f"=== {MENU_TITLE} ===")
    print(f"{MENU_OPTION_GENERATE_CERT}. Generate private key and certificate")
    print(f"{MENU_OPTION_GENERATE_KEYSTORE}. Generate PKCS12 keystore")
    print(f"{MENU_OPTION_INSPECT_CERT}. Inspect certificate")
    print(f"{MENU_OPTION_RUN_ALL}. Run all steps")
    print(f"{MENU_OPTION_EXIT}. Exit")


def handle_menu_choice(choice: str) -> bool:
    """
    Handle a menu choice.

    Returns:
        True if the application should continue running, False otherwise.
    """
    if choice == MENU_OPTION_GENERATE_CERT:
        generate_certificate()
        return True

    if choice == MENU_OPTION_GENERATE_KEYSTORE:
        generate_keystore()
        return True

    if choice == MENU_OPTION_INSPECT_CERT:
        inspect_certificate()
        return True

    if choice == MENU_OPTION_RUN_ALL:
        run_all_steps()
        return True

    if choice == MENU_OPTION_EXIT:
        print("Goodbye.")
        return False

    print("Invalid option. Please try again.\n")
    return True


def main() -> None:
    """Run the CLI application."""
    should_continue: bool = True

    while should_continue:
        print_menu()
        choice: str = input("Select an option: ").strip()
        should_continue = handle_menu_choice(choice)


if __name__ == "__main__":
    main()

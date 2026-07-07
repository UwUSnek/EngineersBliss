#!/usr/bin/env python3
"""
replace_textures.py

Replaces every value inside the top-level "textures" object of each
*.json file in the current directory with the literal string "#texture".

Example:
    "textures": {
        "1": "engineers-bliss:block/copper_golem_statues/vanilla/normal",
        "particle": "engineers-bliss:block/copper_golem_statues/vanilla/normal"
    }
becomes:
    "textures": {
        "1": "#texture",
        "particle": "#texture"
    }

Usage:
    python3 replace_textures.py            # process and overwrite all *.json here
    python3 replace_textures.py --dry-run  # show what would change, don't write
    python3 replace_textures.py --backup   # save a .bak copy before overwriting
"""

import argparse
import glob
import json
import shutil
import sys


def replace_textures_in_file(path: str, dry_run: bool = False, backup: bool = False) -> bool:
    """Load a JSON file, replace values in its 'textures' object, write it back.
    Returns True if the file was (or would be) changed."""
    try:
        with open(path, "r", encoding="utf-8") as f:
            data = json.load(f)
    except (json.JSONDecodeError, OSError) as e:
        print(f"  skip {path}: {e}")
        return False

    textures = data.get("textures")
    if not isinstance(textures, dict) or not textures:
        print(f"  no 'textures' object in {path}, skipping")
        return False

    changed = False
    for key, value in textures.items():
        if value != "#texture":
            print(f"  {path}: \"{key}\": \"{value}\" -> \"#texture\"")
            textures[key] = "#texture"
            changed = True

    if not changed:
        print(f"  {path}: already up to date")
        return False

    if dry_run:
        return True

    if backup:
        shutil.copy2(path, path + ".bak")

    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent="\t")
        f.write("\n")

    return True


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--dry-run", action="store_true", help="preview changes without writing")
    parser.add_argument("--backup", action="store_true", help="write a .bak copy before overwriting")
    args = parser.parse_args()

    files = sorted(glob.glob("*.json"))
    if not files:
        print("No .json files found in the current directory.")
        sys.exit(0)

    changed_count = 0
    for path in files:
        if replace_textures_in_file(path, dry_run=args.dry_run, backup=args.backup):
            changed_count += 1

    verb = "would be" if args.dry_run else "were"
    print(f"\n{changed_count}/{len(files)} file(s) {verb} updated.")


if __name__ == "__main__":
    main()

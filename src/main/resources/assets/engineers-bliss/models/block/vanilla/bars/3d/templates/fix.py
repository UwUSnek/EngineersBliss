# Removes texture paths from all json files in the current directory, replacing them with a "#texture" reference

import glob
import json
import shutil
import sys


def replace_textures_in_file(path: str) -> bool:
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
        return False

    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, indent="\t")
        f.write("\n")

    return True


def main():

    files = sorted(glob.glob("*.json"))
    if not files:
        print("No .json files found in the current directory.")
        sys.exit(0)

    changed_count = 0
    for path in files:
        if replace_textures_in_file(path):
            changed_count += 1

    print(f"\n{changed_count}/{len(files)} file(s) were updated.")


if __name__ == "__main__":
    main()

#!/usr/bin/env python3
import json
from pathlib import Path

CREDIT = "UwU_Snek"

for path in Path(".").rglob("*.json"):
    try:
        data = json.loads(path.read_text(encoding="utf-8"))
    except json.JSONDecodeError:
        print(f"skipped {path}: bad json")
        continue

    if isinstance(data, dict):
        data.pop("credit", None)
        data = {"credit": CREDIT, **data}
        path.write_text(json.dumps(data, indent=2) + "\n", encoding="utf-8")
        print(f"updated: {path}")
    else:
        print(f"skipped {path}: bad json")

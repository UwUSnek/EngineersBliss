#!/usr/bin/env python3
import json
from pathlib import Path

CREDIT = "UwU_Snek"








def is_scalar(x):
    return isinstance(x, (str, int, float, bool)) or x is None




def format_json(obj, indent=0, indent_size=2):
    pad = " " * (indent * indent_size)
    pad_inner = " " * ((indent + 1) * indent_size)


    # Format Objects:
    #   - Align members, but only if every value in the scope spans a single line
    if isinstance(obj, dict):
        if not obj:
            return "{}"
        keys   = [ json.dumps(k) for k in obj.keys() ]
        values = [ format_json(v, indent + 1, indent_size) for v in obj.values() ]

        if all("\n" not in v for v in values):
            width = max(len(k) for k in keys)
            items = [
                f'{ pad_inner }{ k.ljust(width) } : { v }'
                for k, v in zip(keys, values)
            ]
        else:
            items = [
                f'{ pad_inner }{ k } : { v }'
                for k, v in zip(keys, values)
            ]
        return "{\n" + ",\n".join(items) + "\n" + pad + "}"


    # Format Arrays:
    #   - If all elements of the array are scalar, place them on a single line
    if isinstance(obj, list):
        if not obj:
            return "[]"
        if all(is_scalar(x) for x in obj):
            return "[" + ", ".join(json.dumps(x) for x in obj) + "]"
        items = [ f'{ pad_inner }{ format_json(x, indent + 1, indent_size) }' for x in obj ]
        return "[\n" + ",\n".join(items) + "\n" + pad + "]"

    # Return formatted JSON
    return json.dumps(obj)








for path in Path(".").rglob("*.json"):

    # Read JSON file
    try:
        data = json.loads(path.read_text(encoding="utf-8"))
    except json.JSONDecodeError:
        print(f"skipped { path }: bad json")
        continue

    # Set credits and format the file, then write it back to disk
    if isinstance(data, dict):
        data.pop("credit", None)
        data = { "credit": CREDIT, **data }
        path.write_text(format_json(data) + "\n", encoding="utf-8")
        print(f"updated: { path }")
    else:
        print(f"skipped { path }: bad json")
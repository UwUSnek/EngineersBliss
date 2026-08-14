#!/usr/bin/env python3

import os
import re




SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))


# Tokenizer
TOKEN_RE = re.compile(
    r'("(?:\\.|[^"\\])*"'     # String
    r'|\'(?:\\.|[^\'\\])*\''  # Characters
    r'|//[^\n]*'              # Single line comments
    r'|/\*.*?\*/)',           # Multiline comments
    re.DOTALL,
)


REPLACEMENTS = [
    (re.compile(r'\bif\s+\('), 'if('),
    (re.compile(r'\bwhile\s+\('), 'while('),
    (re.compile(r'\bfor\s+\('), 'for('),
    (re.compile(r'\bcatch\s+\('), 'catch('),
    (re.compile(r'\{\s+\}'), '{}'),
    (re.compile(r'\)\{'), ') {'),
]




def fix_code_part(code):
    for pattern, repl in REPLACEMENTS:
        code = pattern.sub(repl, code)
    return code


def fix_source(text):
    parts = TOKEN_RE.split(text)
    for i in range(0, len(parts), 2):
        parts[i] = fix_code_part(parts[i])
    return ''.join(parts)


def main():
    changed_files = 0

    for root, _, files in os.walk(SCRIPT_DIR):
        for name in files:
            if not name.endswith(('.java', '.fsh', '.vsh', '.glsl')):
                continue
            path = os.path.join(root, name)
            with open(path, 'r', encoding='utf-8') as f:
                original = f.read()

            fixed = fix_source(original)

            if fixed != original:
                changed_files += 1
                rel = os.path.relpath(path, SCRIPT_DIR)
                print(f'fixed: { rel }')
                with open(path, 'w', encoding='utf-8') as f:
                    f.write(fixed)

    print(f'\nDone. { changed_files } file(s) changed.')




if __name__ == '__main__':
    main()
#!/usr/bin/env bash
set -euo pipefail



rm -rf ./1_trimmed
rm -rf ./2_converted

echo -e "\n\n------------------------ TRIMMING --------------------------\n"
python3 ./1_trim.py

echo -e "\n\n------------------- CONVERTING TO ATLAS --------------------\n"
python3 ./2_convert.py

echo -e "\n\n------------------------------------------------------------\n"
echo "Done."

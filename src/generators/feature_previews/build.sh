#!/usr/bin/env bash
set -euo pipefail



rm -rf ./1_trimmed
rm -rf ./2_even
rm -rf ./3_converted

echo -e "\n\n------------------------ TRIMMING --------------------------\n"
python3 ./1_trim.py

echo -e "\n\n-------------------- EVENING DURATIONS ---------------------\n"
python3 ./2_even.py

echo -e "\n\n------------------- CONVERTING TO ATLAS --------------------\n"
python3 ./3_convert.py

echo -e "\n\n------------------------------------------------------------\n"
echo "Done."
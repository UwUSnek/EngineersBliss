#!/usr/bin/env bash
set -euo pipefail



rm -rf ./1_trimmed
rm -rf ./2_even
rm -rf ./3_converted

echo "Trimming"
python3 ./1_trim.py

echo "Evening durations"
python3 ./2_even.py

echo "Converting to ATIF"
python3 ./3_convert.py

echo "Done."
#!/bin/sh

rm ./*.ogg
for f in *.wav; do ffmpeg -i "$f" -c:a libvorbis -q:a 10 "${f%.wav}.ogg"; done

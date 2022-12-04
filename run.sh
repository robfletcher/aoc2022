#!/bin/bash
for f in ./*.kts; do
  eval "kotlin -J-ea $f"
done

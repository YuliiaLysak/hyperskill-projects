#!/usr/bin/env bash

echo "Enter a message:"
read -r input
regex='^[A-Z ]+$'
if [[ $input =~ $regex ]]; then
  echo "This is a valid message!"
else
  echo "This is not a valid message!"
fi
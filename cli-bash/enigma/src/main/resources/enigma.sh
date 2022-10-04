#!/usr/bin/env bash

stage_1() {
  echo "Enter a message:"
  read -r input
  regex='^[A-Z ]+$'
  if [[ $input =~ $regex ]]; then
    echo "This is a valid message!"
  else
    echo "This is not a valid message!"
  fi
}

stage_2() {
  echo "Enter an uppercase letter:"
  read -r letter
  echo "Enter a key:"
  read -r key
  letter_regex='^[A-Z]$'
  key_regex='^[0-9]$'
  if [[ $letter =~ $letter_regex ]] && [[ $key =~ $key_regex ]]; then
    value=$(ord "$letter")
    shifted_value=$((value + key))
    if [[ $shifted_value -ge 90 ]]; then
      shifted_value=$((shifted_value - 26))
    fi

    encrypted_letter=$(chr "$shifted_value")
    echo "Encrypted letter: $encrypted_letter"
  else
    echo "Invalid key or letter!"
  fi
}

# get the ASCII value of a character (e.g. A = 65, Z = 90)
ord() {
  printf "%d\n" "'$1"
}

# get the ASCII character of a value (e.g. 65 = A, 90 = Z)
chr() {
  printf "%b\n" "$(printf "\\%03o" "$1")"
}

#stage_1
stage_2

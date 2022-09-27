#!/usr/bin/bash

validate() {
  array=("$@")
  array_length="${#array[@]}"
  if [[ "$array_length" -eq 2 ]]; then
      return
  else
      output_error
  fi
}

check() {
  definition="$1"
  regex="$2"
  if [[ "$definition" =~ $regex ]]; then
    return
  fi
  output_error
}

output_error() {
  echo "The definition is incorrect!"
  exit
}

echo "Enter a definition:"
read -r -a input
validate "${input[@]}"
definition="${input[0]}"
constant="${input[1]}"
definition_regex="^[a-zA-Z]+_to_[a-zA-Z]+$"
constant_regex="^-?[0-9]+\.?[0-9]*$"
check "$definition" "$definition_regex"
check "$constant" "$constant_regex"
echo "The definition is correct!"
#!/usr/bin/bash

validate_array() {
  array=("$@")
  array_length="${#array[@]}"
  if [[ "$array_length" -eq 2 ]]; then
    return
  else
    output_error
  fi
}

validate_value() {
  value="$1"
  regex="$2"
  if [[ "$value" =~ $regex ]]; then
    is_valid_value=true
  else
    echo "Enter a float or integer value!"
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
validate_array "${input[@]}"
definition="${input[0]}"
constant="${input[1]}"
definition_regex='^[a-zA-Z]+_to_[a-zA-Z]+$'
constant_regex='^-?[0-9]+\.?[0-9]*$'
check "$definition" "$definition_regex"
check "$constant" "$constant_regex"

echo "Enter a value to convert:"
while true; do
  is_valid_value=false
  read -r value
  validate_value "$value" "$constant_regex"
  if $is_valid_value; then
    break
  fi
done

#result=$(echo "scale=2; $constant * $value" | bc -l)
#printf "Result: %s\n" "$result"
echo "Result: $(bc -l <<<"$value"*"$constant")"
#!/usr/bin/bash

file_name="definitions.txt"
definition_regex='^[a-zA-Z]+_to_[a-zA-Z]+$'
constant_regex='^-?[0-9]+\.?[0-9]*$'

print_menu() {
  echo -e "\nSelect an option"
  echo "0. Type '0' or 'quit' to end program"
  echo "1. Convert units"
  echo "2. Add a definition"
  echo "3. Delete a definition"
}

process_option() {
  case $1 in
  "0"|"quit")
    quit=true
    ;;
  "1")
    echo "Not implemented!"
    ;;
  "2")
    add_definition
    ;;
  "3")
    delete_definition
    ;;
  *)
    echo "Invalid option!"
    ;;
  esac
}

add_definition() {
  while true; do
    echo "Enter a definition:"
    read -r -a input
    correctDefinition=false
    check_definition "${input[@]}"
    if $correctDefinition; then
      definition="${input[0]}"
      constant="${input[1]}"
      echo "$definition $constant" >> "$file_name"
      break
    fi
  done
}

#validate_value() {
#  value="$1"
#  regex="$2"
#  if [[ "$value" =~ $regex ]]; then
#    is_valid_value=true
#  else
#    echo "Enter a float or integer value!"
#  fi
#}

check_definition() {
  array=("$@")
  array_length="${#array[@]}"
  definition="${array[0]}"
  constant="${array[1]}"
  if [[ "$array_length" -eq 2 ]] && [[ "$definition" =~ $definition_regex ]] && [[ "$constant" =~ $constant_regex ]]; then
    correctDefinition=true
  else
    echo "The definition is incorrect!"
  fi
}

delete_definition() {
  if [ -s "$file_name" ]; then
    process_file_content
  else
    echo "Please add a definition first!"
  fi
}

process_file_content() {
  output_file_content
  while true; do
    read -r user_line_number
    if [[ $user_line_number == '0' ]]; then
      break
    elif [[ $user_line_number =~ $constant_regex ]] && [[ $user_line_number -ge 1 ]] && [[ $user_line_number -le $total_lines ]]; then
      # for passing hyperskill stage
      #sed -i "${user_line_number}d" "$file_name"

      # for checking locally on macOs (replace -i with -i '')
      sed -i '' "${user_line_number}d" "$file_name"
      break
    else
      echo "Enter a valid line number!"
    fi
  done
}

output_file_content() {
  echo "Type the line number to delete or '0' to return"
  # read info about file (lineCount, wordCount, bytes) | get rid of repeated spaces | split by space and take lineCount
  total_lines=$(wc "$file_name" | tr -s "[:space:]" | cut -d ' ' -f 2)
  line_number=1
  while read -r line; do
    echo "$line_number. $line";
    ((line_number++))
  done < "$file_name"
}


echo "Welcome to the Simple converter!"
while true; do
  print_menu
  read -r option
  quit=false
  process_option "$option"
  if $quit; then
      break
  fi
done

echo "Goodbye!"

#echo "Enter a value to convert:"
#while true; do
#  is_valid_value=false
#  read -r value
#  validate_value "$value" "$constant_regex"
#  if $is_valid_value; then
#    break
#  fi
#done

#result=$(echo "scale=2; $constant * $value" | bc -l)
#printf "Result: %s\n" "$result"
#echo "Result: $(bc -l <<<"$value"*"$constant")"
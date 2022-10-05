#!/usr/bin/env bash

constant_key=3
message_regex='^[A-Z ]+$'
filename_regex='^[A-Za-z.]+$'

stage_1() {
  echo "Enter a message:"
  read -r input
  if [[ $input =~ $message_regex ]]; then
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

stage_3() {
  echo "Type 'e' to encrypt, 'd' to decrypt a message:"
  echo "Enter a command:"
  read -r command
  case $command in
  "e")
    process_input
    encrypt "$message"
    ;;
  "d")
    process_input
    decrypt "$message"
    ;;
  *)
    echo "Invalid command!"
    ;;
  esac
}

process_input() {
  echo "Enter a message:"
  read -r message
  is_valid_message=$(check_message "$message")
  if ! $is_valid_message; then
    echo "This is not a valid message!"
    exit
  fi
}

check_message() {
  if [[ $1 =~ $message_regex ]]; then
    echo "true"
  else
    echo "false"
  fi
}

encrypt() {
  # Convert string to array
  message_length=${#1}
  for ((i = 0; i < message_length; i++)); do
    array+=("${1:i:1}") ## use array indexing for individual chars
  done

  array_length="${#array[@]}"
  for ((i = 0; i < array_length; i++)); do
    value=$(ord "${array[i]}")
    # check for space (' ' = 32)
    if [[ $value -eq 32 ]]; then
      shifted_value=$value
    else
      shifted_value=$((value + constant_key))
    fi

    # start with A after Z
    if [[ $shifted_value -gt 90 ]]; then
      shifted_value=$((shifted_value - 26))
    fi

    encrypted_letter=$(chr "$shifted_value")
    result+=$encrypted_letter
    # TODO: uncomment next line for check at hyperskill.org
    #echo ""
  done
  echo "Encrypted message:"
  echo "$result"
}

decrypt() {
  # Convert string to array
  message_length=${#1}
  for ((i = 0; i < message_length; i++)); do
    array+=("${1:i:1}") ## use array indexing for individual chars
  done

  array_length="${#array[@]}"
  for ((i = 0; i < array_length; i++)); do
    value=$(ord "${array[i]}")
    # check for space (' ' = 32)
    if [[ $value -eq 32 ]]; then
      shifted_value=$value
    else
      shifted_value=$((value - constant_key))
    fi

    # start with Z after A
    if [[ $shifted_value -lt 65 ]] && [[ $shifted_value -ne 32 ]]; then
      shifted_value=$((shifted_value + 26))
    fi

    decrypted_letter=$(chr "$shifted_value")
    result+=$decrypted_letter
    # TODO: uncomment next line for check at hyperskill.org
    #echo ""
  done
  echo "Decrypted message:"
  echo "$result"
}

stage_4() {
  echo "Welcome to the Enigma!"
  while true; do
    print_menu
    read -r option
    case $option in
    "0")
      echo "See you later!"
      exit
      ;;
    "1")
      echo "Enter the filename:"
      read -r filename
      is_valid_filename=$(check_filename $filename)
      if $is_valid_filename; then
        echo "Enter message:"
        read -r message
        touch "$filename"
        echo "$message" >> "$filename"
      else
        echo "File name can contain letters and dots only!"
      fi
      ;;
    "2")
      echo "Enter the filename:"
      read -r filename
      if [ -s "$filename" ]; then
        read -r file_content < "$filename"
        echo "$file_content"
      else
        echo "File not found!"
      fi
      ;;
    "3")
      echo "Not implemented!"
      ;;
    "4")
      echo "Not implemented!"
      ;;
    *)
      echo "Invalid option!"
      ;;
    esac
  done
}

print_menu() {
  echo -e "\n0. Exit"
  echo "1. Create a file"
  echo "2. Read a file"
  echo "3. Encrypt a file"
  echo "4. Decrypt a file"
  echo "Enter an option:"
}

check_filename() {
  if [[ $1 =~ $filename_regex ]]; then
    echo "true"
  else
    echo "false"
  fi
}

#stage_1
#stage_2
#stage_3
stage_4

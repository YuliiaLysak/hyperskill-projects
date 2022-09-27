#!/usr/bin/env bash

regex='^(-?[0-9]+\.?[0-9]*) [-+*\/^] (-?[0-9]+\.?[0-9]*)$'
echo "Welcome to the basic calculator!" | tee operation_history.txt
while true; do
  echo "Enter an arithmetic operation or type 'quit' to quit:" | tee -a operation_history.txt
  read -r input
  echo "$input" >> operation_history.txt
  if [[ "$input" =~ "quit" ]]; then
    echo "Goodbye!" | tee -a operation_history.txt
    break
  elif [[ "$input" =~ $regex ]]; then
    arithmetic_result=$(echo "scale=2; $input" | bc -l)
    printf "%s\n" "$arithmetic_result" | tee -a operation_history.txt
  else
    echo "Operation check failed!" | tee -a operation_history.txt
  fi
done
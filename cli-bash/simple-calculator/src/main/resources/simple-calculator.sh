#!/usr/bin/env bash

#calculate() {
#  if [ "$operator" = "+" ]; then
#    arithmetic_result=$((number1 + number2))
#  elif [ "$operator" = "-" ]; then
#    arithmetic_result=$((number1 - number2))
#  elif [ "$operator" = "*" ]; then
#    arithmetic_result=$((number1 * number2))
#  elif [ "$operator" = "/" ]; then
#    arithmetic_result=$((number1 / number2))
#  fi
#}

echo "Enter an arithmetic operation:"
read number1 operator number2
number_regex='^-?[-0-9]+$'
operator_regex='^[-+*/]$'
if [[ "$number1" =~ $number_regex ]] && [[ "$number2" =~ $number_regex ]] && [[ "$operator" =~ $operator_regex ]]; then
#  calculate
  arithmetic_result=$(( $number1 $operator $number2))
  printf "%s\n" "$arithmetic_result"
else
  echo "Operation check failed!"
fi
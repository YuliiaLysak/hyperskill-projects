#!/usr/bin/env bash

echo "Enter an arithmetic operation:"
#read input
#re='^[-0-9]+ [-,+,*,/] [-0-9]+$'
#if [[ "$input" =~ $re ]]; then
#    echo "Operation check passed!"
#else
#    echo "Operation check failed!"
#fi

read number1 operator number2
number_regex='^-?[-0-9]+$'
operator_regex='^[-+*/]$'
if [[ "$number1" =~ $number_regex ]] && [[ "$number2" =~ $number_regex ]] && [[ "$operator" =~ $operator_regex ]]; then
    echo "Operation check passed!"
else
    echo "Operation check failed!"
fi
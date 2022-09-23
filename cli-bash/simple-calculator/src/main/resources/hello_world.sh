#!/usr/bin/env bash

personal_data() {
    echo "You provided $# facts about yourself!"
    echo "Your name is $1"
    echo "Your age is $2"
}

#if [ 2 -ne 3 ]; then
#  personal_data "Amy" 28
#fi

#echo "Enter password: "
#read input
#if [ $input = "Pass!@#" ]; then
#    echo "Correct password entered"
#else
#    echo "Incorrect password entered"
#fi

#$num1 = 20;
#$num2 = 10;
#
#if [ "$num1" -eq "$num2" ]; then
#    echo "Numbers are equal"
#elif [ "$num1" -gt "$num2" ]; then
#    echo "First number is greater"
#else
#    echo "Second number is greater"
#fi


#solve() {
#    # add your solution here
#    if [ $(( $1 + $2 + $3 )) -eq 180 ]; then
#        echo "Yes"
#    else
#        echo "No"
#    fi
#}

solve() {
    # add your solution here
    if [ "$1" -ge 500 ] -a [ "$1" -le 1000 ]; then
        echo "Good Job!"
    else
        echo "Revise the essay"
    fi
}

solve 350
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
    if [ $1 -ge 500 ] && [ $1 -le 1000 ]; then
        echo "Good Job!"
    else
        echo "Revise the essay"
    fi
}

loop() {
  for i in 1 2 3 4 5; do
    echo $i;
  done
}

loop2() {
  for i in John Jack Mary; do
      echo "Here is $i";
  done
}

loop_with_sequence() {
  for i in $(seq 1 2 5); do  # seq [start] [incr] [stop]
      echo "Number: $i";
  done
}

loop_while() {
  i=1
  while [ $i -le 3 ]; do
    echo "I is $i";
    i=$(($i+1));
  done
}

loop_infinite() {
  while true; do
    echo "To stop execution of a loop, use CTRL+C"
  done
}



solve 350
#loop
#loop2
#seq 4
#seq -f %f 5 # "-f" is for formatting (default "%g")
#seq -s " | " 5 # "-s" is a separator (default "\n")
#loop_with_sequence

#loop_while
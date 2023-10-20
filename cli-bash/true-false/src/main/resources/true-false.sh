#!/usr/bin/env bash

print_menu() {
  echo "0. Exit
        1. Play a game
        2. Display scores
        3. Reset scores
        Enter an option:"
}

print_success_response() {
  responses=("Perfect!" "Awesome!" "You are a genius!" "Wow!" "Wonderful!")
  idx=$((RANDOM % 5))
  echo "${responses[$idx]}"
}

save_score_to_file() {
  echo "User: $1, Score: $2, Date: $(date '+%Y-%m-%d')" >> $file_name
}

retrieve_question() {
  curl --request GET -sL \
       --url 'http://127.0.0.1:8000/game'\
       --silent \
       --cookie "$(cat cookie.txt)"

  item="$?"
  question=$(echo "$item" | sed 's/.*"question": *"\{0,1\}\([^,"]*\)"\{0,1\}.*/\1/')
  answer=$(echo "$item" | sed 's/.*"answer": *"\{0,1\}\([^,"]*\)"\{0,1\}.*/\1/')
  echo "$question"
  echo "True or False?"
  read -r player_answer
  if [[ $player_answer =~ $answer ]]; then
    print_success_response
    correct_answers=$((correct_answers+1))
    score=$((score+10))
    save_score_to_file $1 $score
  else
    echo "Wrong answer, sorry!"
    echo "$1 you have $correct_answers correct answer(s)."
    echo "Your score is $score points."
    return
  fi
}

play_game() {
  echo "What is your name?"
  read -r player_name
  while true; do
    retrieve_question "$player_name"
  done
}

print_score() {
  if [ ! -f $file_name ]; then
    echo "File not found or no scores in it!"
  else
    echo "Player scores"
    cat $file_name
  fi
}

reset_score() {
  if [ ! -f $file_name ]; then
    echo "File not found or no scores in it!"
  else
    rm $file_name
    echo "File deleted successfully!"
  fi
}

read_input() {
  read -r input
  case $input in
    "0")
      echo "See you later!"
      exit
      ;;
    "1")
      echo "Playing game"
      play_game
      ;;
    "2")
      echo "Displaying scores"
      print_score
      ;;
    "3")
      echo "Resetting scores;"
      reset_score
      ;;
    *)
      echo "Invalid option!"
      ;;
  esac
}


RANDOM=$RANDOM
score=0
file_name="scores.txt"
echo "Welcome to the True or False Game!"
while true; do
  print_menu
  read_input
done

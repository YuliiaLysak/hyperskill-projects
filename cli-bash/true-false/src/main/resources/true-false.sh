#!/usr/bin/env bash

print_menu() {
  echo "0. Exit
        1. Play a game
        2. Display scores
        3. Reset scores
        Enter an option:"
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
      ;;
    "2")
      echo "Displaying scores"
      ;;
    "3")
      echo "Resetting scores;"
      ;;
    *)
      echo "Invalid option!"
      ;;
  esac
}

echo "Welcome to the True or False Game!"
while true; do
  print_menu
  read_input
done

#curl --request GET -sL \
#     --url 'http://127.0.0.1:8000/download/file.txt'\
#     --silent \
#     --output 'ID_card.txt'
#
#cat 'ID_card.txt'
#
#curl --request GET -sL \
#     --url 'http://127.0.0.1:8000/login'\
#     --silent \
#     --output 'cookie.txt' \
#     --cookie-jar "$(cat cookie.txt)" \
#     --user "$(cat ID_card.txt)"
#
#echo "Login message: $?"
#
#curl --request GET -sL \
#     --url 'http://127.0.0.1:8000/game'\
#     --silent \
#     --cookie "$(cat cookie.txt)"
#
#echo "Response: $?;"

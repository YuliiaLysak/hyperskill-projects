#!/usr/bin/env bash

echo "Welcome to the True or False Game!"
curl --request GET -sL \
     --url 'http://127.0.0.1:8000/download/file.txt'\
     --silent \
     --output 'ID_card.txt'

cat 'ID_card.txt'

curl --request GET -sL \
     --url 'http://127.0.0.1:8000/login'\
     --silent \
     --output 'cookie.txt' \
     --cookie-jar "$(cat cookie.txt)" \
     --user "$(cat ID_card.txt)"

echo "Login message: $?"

# adding new information to a diary
#echo "Also my favorite things are ..." >> diary.txt

# rewriting the file content
#echo "My new everlasting love is ..." > secret.txt

# run in terminal using "bash examples.sh 1> log.txt  2> error.txt"
# 0> is stdin, 1> is stdout, 2> is stderr
#echo "Just a normal message"
#echo "ERROR!" >&2

# command 1 | command 2 | command 3 ...
# The first command takes data as input and processes it.
# Then the result of the first command is taken as the input by the second one, and so on.
#echo -e "Hmm...\nBrr...\nMmm..." | grep "m"

#answers=("a" "d" "c" "a" "a")
#total_score=0
#
#for (( i = 0; i < 5; i++));
#do
#    case "${1}" in
#
#        ${answers[i]})
#            ((total_score++))
#            ;;
#        "-"|"--")
#            ;;
#        *)
#            ((total_score--))
#            ;;
#
#    esac
#    shift 1
#done
#
#echo "Total score is: $total_score"

#display some basic text statistics
echo "Hello world" | wc

#cut out parts of text
echo "The sky is blue" | cut -d ' ' -f 1-2
cut -d ':' -f 1 seasons.txt

#translate, squeeze, and delete characters
echo lalala | tr a o #replace characters "a" with "o"
echo 'Linux Ubuntu' | tr -d 'u' #delete all the "u" characters
echo 'Linux Ubuntu' | tr -dc 'u' #keep only the character you deleted
echo 'Repeated  spaces in  line' | tr -s [:space:] #removes duplicates. For example, with this option we can get rid of repeated spaces
package edu.lysak.tictactoe.example_stage5;

public class Hard extends Opponent {
    char[][] playingField;
    char letter;
    char opponentLetter;

    public Hard(char[][] playingField, char letter) {
        this.playingField = playingField;
        this.letter = letter;
        opponentLetter = letter == 'X' ? 'O' : 'X';
    }

    @Override
    void makeMove() {
        if (isPlayingFieldEmpty()) {
            playingField[1][1] = letter;
            return;
        }
        int[] indexBox = new int[2];
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < playingField.length; i++) {
            for (int j = 0; j < playingField[i].length; j++) {
                if (playingField[i][j] == '_') {
                    playingField[i][j] = letter;
                    int score = miniMax(playingField, opponentLetter);
                    playingField[i][j] = '_';
                    if (score > bestScore) {
                        indexBox[0] = i;
                        indexBox[1] = j;
                        bestScore = score;
                    }
                }
            }
        }
        playingField[indexBox[0]][indexBox[1]] = letter;
        System.out.println("Making move level \"hard\"");
    }


    private int miniMax(char[][] playingField, char playerLetter) {
        char result = whoIsWinner(playingField);
        if (result == 'D') {
            return 0;
        } else if (result == letter) {
            return 10;
        } else if (result == opponentLetter) {
            return -10;
        }

        int bestScore;
        if (playerLetter == letter) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < playingField.length; i++) {
                for (int j = 0; j < playingField[i].length; j++) {
                    if (playingField[i][j] == '_') {
                        playingField[i][j] = letter;
                        int score = miniMax(playingField, opponentLetter);
                        playingField[i][j] = '_';
                        bestScore = Math.max(bestScore, score);
                    }
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < playingField.length; i++) {
                for (int j = 0; j < playingField[i].length; j++) {
                    if (playingField[i][j] == '_') {
                        playingField[i][j] = opponentLetter;
                        int score = miniMax(playingField, letter);
                        playingField[i][j] = '_';
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }
        }
        return bestScore;

    }

    private char whoIsWinner(char[][] playingField) {
        char winner = 'N';

        for (char[] chars : playingField) {
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == '_') {
                    continue;
                }
                if (chars[0] == chars[1] && chars[0] == chars[2]) {
                    winner = chars[0];
                    return winner;
                }
                if (playingField[0][j] == playingField[1][j] && playingField[0][j] == playingField[2][j]) {
                    winner = playingField[0][j];
                    return winner;
                }
            }
        }
        if (((playingField[0][0] == playingField[1][1] && playingField[0][0] == playingField[2][2]) ||
            (playingField[0][2] == playingField[1][1] && playingField[0][2] == playingField[2][0])) && playingField[1][1] != '_') {
            winner = playingField[1][1];
            return winner;
        }

        for (char[] chars : playingField) {
            for (char aChar : chars) {
                if (aChar == '_') {
                    return winner;
                }
            }
        }
        winner = 'D';
        return winner;
    }

    private boolean isPlayingFieldEmpty() {
        int count = 0;
        for (char[] chars : playingField) {
            for (char aChar : chars) {
                if (aChar == '_') count++;
            }
        }
        return count == 9;
    }
}

package edu.lysak.tictactoe.example_stage5;

import java.util.Random;

public class Medium extends Opponent {
    char[][] playingField;
    char letter;

    public Medium(char[][] playingField, char letter) {
        this.playingField = playingField;
        this.letter = letter;
    }


    @Override
    void makeMove() {
        Random rndX = new Random();
        Random rndY = new Random();
        if (canIWin(playingField)) {
            return;
        } else if (canOpponentWin(playingField)) {
            return;
        } else {
            int x = rndX.nextInt(3);
            int y = rndY.nextInt(3);
            if (playingField[x][y] == '_') {
                System.out.println("Making move level \"medium\"");
                playingField[x][y] = letter;
            } else {
                try {
                    Thread.sleep(10);
                    makeMove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean canIWin(char[][] playingField) {
        if ((playingField[1][1] == letter && playingField[0][0] == playingField[1][1] && playingField[2][2] == '_') ||
            (playingField[1][1] == letter && playingField[2][2] == playingField[1][1] && playingField[0][0] == '_') ||
            (playingField[1][1] == '_' && playingField[2][2] == letter && playingField[2][2] == playingField[0][0])) {
            for (int i = 0; i < 3; i++) {
                if (playingField[i][i] == '_') {
                    System.out.println("Making move level \"medium\"");
                    this.playingField[i][i] = letter;
                    break;
                }
            }
            return true;
        } else if ((playingField[1][1] == letter && playingField[0][2] == playingField[1][1]) && playingField[2][0] == '_' ||
            (playingField[1][1] == letter && playingField[2][0] == playingField[1][1] && playingField[0][2] == '_') ||
            (playingField[1][1] == '_' && playingField[2][0] == letter && playingField[2][0] == playingField[0][2])) {
            for (int i = 0; i < 3; i++) {
                if (playingField[i][2 - i] == '_') {
                    System.out.println("Making move level \"medium\"");
                    this.playingField[i][2 - i] = letter;
                    break;
                }
            }
            return true;
        } else {
            for (int i = 0; i < playingField.length; i++) {
                for (int j = 0; j < playingField[i].length; j++) {
                    if (playingField[i][j] == '_') {
                        continue;
                    }
                    if ((playingField[i][0] == letter && playingField[i][0] == playingField[i][1] && playingField[i][2] == '_') ||
                        (playingField[i][1] == letter && playingField[i][1] == playingField[i][2] && playingField[i][0] == '_') ||
                        (playingField[i][0] == letter && playingField[i][0] == playingField[i][2] && playingField[i][1] == '_')) {
                        for (int k = 0; k < 3; k++) {
                            if (playingField[i][k] == '_') {
                                System.out.println("Making move level \"medium\"");
                                this.playingField[i][k] = letter;
                                break;
                            }
                        }
                        return true;
                    } else if ((playingField[0][j] == letter && playingField[0][j] == playingField[1][j] && playingField[2][j] == '_') ||
                        (playingField[1][j] == letter && playingField[1][j] == playingField[2][j]) && playingField[0][j] == '_' ||
                        (playingField[0][j] == letter && playingField[0][j] == playingField[2][j] && playingField[1][j] == '_')) {
                        for (int k = 0; k < 3; k++) {
                            if (playingField[k][j] == '_') {
                                System.out.println("Making move level \"medium\"");
                                this.playingField[k][j] = letter;
                                break;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canOpponentWin(char[][] playingField) {
        if ((playingField[1][1] != letter && playingField[0][0] == playingField[1][1] && playingField[1][1] != '_' && playingField[2][2] == '_') ||
            (playingField[1][1] != letter && playingField[2][2] == playingField[1][1] && playingField[1][1] != '_' && playingField[0][0] == '_') ||
            (playingField[2][2] != letter && playingField[2][2] == playingField[0][0] && playingField[2][2] != '_' && playingField[1][1] == '_')) {
            for (int i = 0; i < 3; i++) {
                if (playingField[i][i] == '_') {
                    System.out.println("Making move level \"medium\"");
                    this.playingField[i][i] = letter;
                    break;
                }
            }
            return true;
        } else if ((playingField[1][1] != letter && playingField[0][2] == playingField[1][1] && playingField[1][1] != '_' && playingField[2][0] == '_') ||
            (playingField[1][1] != letter && playingField[2][0] == playingField[1][1] && playingField[1][1] != '_' && playingField[0][2] == '_') ||
            (playingField[2][0] != letter && playingField[2][0] == playingField[0][2] && playingField[2][0] != '_' && playingField[1][1] == '_')) {
            for (int i = 0; i < 3; i++) {
                if (playingField[i][2 - i] == '_') {
                    System.out.println("Making move level \"medium\"");
                    this.playingField[i][2 - i] = letter;
                    break;
                }
            }
            return true;
        } else {
            for (int i = 0; i < playingField.length; i++) {
                for (int j = 0; j < playingField[i].length; j++) {
                    if ((playingField[i][0] != letter && playingField[i][0] == playingField[i][1] && playingField[i][0] != '_' && playingField[i][2] == '_') ||
                        (playingField[i][1] != letter && playingField[i][1] == playingField[i][2] && playingField[i][1] != '_' && playingField[i][0] == '_') ||
                        (playingField[i][0] != letter && playingField[i][0] == playingField[i][2] && playingField[i][0] != '_' && playingField[i][1] == '_')) {
                        for (int k = 0; k < 3; k++) {
                            if (playingField[i][k] == '_') {
                                System.out.println("Making move level \"medium\"");
                                this.playingField[i][k] = letter;
                                break;
                            }
                        }
                        return true;
                    } else if ((playingField[0][j] != letter && playingField[0][j] == playingField[1][j] && playingField[0][j] != '_'&& playingField[2][j] == '_') ||
                        (playingField[1][j] != letter && playingField[1][j] == playingField[2][j] && playingField[1][j] != '_'&& playingField[0][j] == '_') ||
                        (playingField[0][j] != letter && playingField[0][j] == playingField[2][j] && playingField[0][j] != '_'&& playingField[1][j] == '_')) {
                        for (int k = 0; k < 3; k++) {
                            if (playingField[k][j] == '_') {
                                System.out.println("Making move level \"medium\"");
                                this.playingField[k][j] = letter;
                                break;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

package day02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day02 {
    public static void main(String[] args) throws FileNotFoundException {
//        File file = new File("src/day02/day02-test.txt");
        File file = new File("src/day02/day02-real.txt");
        Scanner input = new Scanner(file);

        int totalPoints = 0;
        while (input.hasNext()) {
            String line = input.nextLine();
            String[] round = line.split(" ");
            totalPoints += getPoints(round[0].charAt(0), round[1].charAt(0));
        }
        
        // Part 1: how many points do you get following the strategy guide
        System.out.println(totalPoints);
    }

    public static int getPoints(char encryptedMoveOpponent, char encryptedMoveYou) {
        final int DRAW = 3;
        final int WIN = 6;

        /**
         * Convert encrypted moves to specific numbers, the number of points you get for using a certain move:
         *  A == X == rock == 1
         *  B == Y == paper == 2
         *  C == Z == scissors == 3
         */
        int moveOpponent = encryptedMoveOpponent - 'A' + 1;
        int moveYou = encryptedMoveYou - 'X' + 1;

        /**
         * Add points for a WIN or DRAW (losing gives you no extra points).
         * Use "moveYou" as a "total points this round"-variable, because why not?
         * If both players have the same move, it's a DRAW.
         * If after applying some maths both players have the same move, you WIN.
         * The maths is used, because I didn't want to write out multiple conditionals for each move combination.
         */
        if (moveOpponent == moveYou) {
            moveYou += DRAW;
        } else if (moveOpponent % 3 + 1 == moveYou) {
            moveYou += WIN;
        }

        return moveYou;
    }
}

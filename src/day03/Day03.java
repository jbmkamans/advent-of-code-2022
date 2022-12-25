package day03;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day03 {
    public static void main(String[] args) throws FileNotFoundException {
//        File file = new File("src/day03/day03-test.txt");
        File file = new File("src/day03/day03-real.txt");
        Scanner input = new Scanner(file);

        int totalPriorities = 0; // answer of part A
        while (input.hasNext()) {
            char[] line = input.nextLine().toCharArray();

            // ==============================================================
            // I'm gonna use a 64bit datatype in my own way.
            // I'm gonna fill it with letters by setting the "correct" bit.
            // When I want to save an "A", I set the last bit,
            // when I want to save a "B", I set the bit before the last, etc.
            // For this, I can just use the ASCII table with some maths.
            // It is very handy that the letters a-z are close to A-Z in the ASCII table,
            // there are only 6 letters between them (and I'm not gonna use them).
            // So I can fit all the a-z and A-Z (26 + 26 = 52) bits in a 64bit data type.

            // These are the letters in their corresponding bit place:
            // ......zyxwvutsrqponmlkjihgfedcba......ZYXWVUTSRQPONMLKJIHGFEDCBA
            // okay, let's start!
            // ==============================================================

            // determine the half
            int halfSize = line.length / 2;
            long first = 0; // long = 64bit, gonna fill it with letters
            long second = 0; // long = 64bit, gonna fill it with letters

            // set correct bits in both first and second compartments
            for (int i = 0; i < halfSize; i++) {
                first |= (1L << line[i] - 'A');
                second |= (1L << line[i + halfSize] - 'A');
            }

            // bitwise AND, so you keep a binary string with only one 1.
            // to get the position of this "1"-bit, just get the length of the binary string,
            // cause leading zeros are stripped.
            String binaryString = Long.toBinaryString(first & second);
            int positionOfSetBit = binaryString.length();

            char letter = (char) (positionOfSetBit - 1 + 'A');
            // alternative way to determine letter:
            // char letter = (char) (Math.log(first & second) / Math.log(2) + 'A');

            // calculate "priority"
            int priority;
            if (letter <= 'Z') {
                // letters A-Z have priority 27-52
                priority = positionOfSetBit + 26;
            } else {
                // letters a-z have priority 1-26
                // substract 26 (because of A-Z), substract 6 (because of unused bit spaces)
                priority = positionOfSetBit - 26 - 6;
            }

            System.out.println(letter);
            System.out.println(priority);

            // Part 1: sum priorities
            totalPriorities += priority;
        }
        System.out.printf("Sum of priorities is: %d", totalPriorities);
    }
}

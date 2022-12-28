package day03;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.BitSet;
import java.util.Scanner;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;

public class Day03 {
    public static void main(String[] args) throws FileNotFoundException {
//        File file = new File("src/day03/day03-test.txt");
        File file = new File("src/day03/day03-real.txt");
        Scanner input = new Scanner(file);

        int totalPriorities = 0; // answer of part A
        while (input.hasNext()) {
            String line = input.nextLine();

            // ==============================================================
            // I'm gonna save letters in a java.util.BitSet in a special way.
            // Each letter (either a-z or A-Z) corresponds to one bit in the bitset.
            // Index 0-25 are used for letters A-Z (index counting from left to right, i.e. LSB 0),
            // index 26-31 are unused,
            // index 32-57 are used for letters a-z,
            // index 58-63 are unused.
            // Index 0 = 'A', index 1 = 'B', etc.
            // Index 32 = 'a', index 33 = 'b', etc.
            // Those indices are chosen, so I can use the ASCII table numbering.
            // ==============================================================

            // determine the half, need it to split the input in a first and second compartment
            int halfSize = line.length() / 2;

            // function to get a bitset with one set bit at the correct index (see big comment at top)
            IntFunction<BitSet> toBitsetWithCorrectLetterIndex = (c) -> BitSet.valueOf(new long[]{1L << (c - 'A')});

            // need this, because the BitSet.or is void instead of returning the resulting bitset...
            BinaryOperator<BitSet> bitwiseOr = (accumulator, element) -> {
                accumulator.or(element);
                return accumulator;
            };

            // get first compartment as a special bitset
            BitSet first = line.chars()
                    .limit(halfSize) // take first half of input string
                    .mapToObj(toBitsetWithCorrectLetterIndex) // map to my special bitset
                    .reduce(new BitSet(), bitwiseOr); // take bitwise or

            // get second compartment as a special bitset
            BitSet second = line.chars()
                    .skip(halfSize) // skip first half, i.e. take second half of input string
                    .mapToObj(toBitsetWithCorrectLetterIndex) // map to my special bitset
                    .reduce(new BitSet(), bitwiseOr); // take bitwise or

            // keep only the common letter by using bitwise AND (i.e. the whole reason I use BitSet)
            first.and(second);

            // get the actual letter
            char letter = (char) (first.nextSetBit(0) + 'A');

            // calculate "priority"
            int priority;
            if (letter <= 'Z') {
                // letters A-Z have priority 27-52
                priority = letter - 'A' + 1 + 26;
            } else {
                // letters a-z have priority 1-26
                priority = letter - 'a' + 1;
            }

            System.out.println(letter);
            System.out.println(priority);

            // Part 1: sum priorities
            totalPriorities += priority;
        }
        System.out.printf("Sum of priorities is: %d", totalPriorities);
    }
}

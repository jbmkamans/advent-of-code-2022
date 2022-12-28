package day04;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day04 {
    public static void main(String[] args) throws FileNotFoundException {
//        File file = new File("src/day04/day04-test.txt");
        File file = new File("src/day04/day04-real.txt");
        Scanner input = new Scanner(file);
        input.useDelimiter("[,\\-\\p{javaWhitespace}]+");

        int countFullOverlaps = 0;
        int countOverlaps = 0;
        while (input.hasNext()) {
            int startElf1 = input.nextInt();
            int endElf1 = input.nextInt();
            int startElf2 = input.nextInt();
            int endElf2 = input.nextInt();

            if (hasFullOverlap(startElf1, endElf1, startElf2, endElf2)) {
                countFullOverlaps++;
            }
            if (hasOverlap(startElf1, endElf1, startElf2, endElf2)) {
                countOverlaps++;
            }
        }
        System.out.printf("# full overlaps: %d%n", countFullOverlaps);
        System.out.printf("# overlaps: %d%n", countOverlaps);
    }

    public static boolean hasFullOverlap(int start1, int end1, int start2, int end2) {
        return (start1 == start2 || end1 == end2) // trivial
                || (start1 < start2 && end2 < end1) // start2-end2 is included in start1-end1
                || (start2 < start1 && end1 < end2); // start1-end1 is included in start2-end2
    }

    public static boolean hasOverlap(int start1, int end1, int start2, int end2) {
        return (start1 >= start2 && start1 <= end2) // start1 between start2-end2
                || (end1 >= start2 && end1 <= end2)  // end1 between start2-end2
                || (start2 >= start1 && start2 <= end1) // start2 between start1-end1
                || (end2 >= start1 && end2 <= end1); // end2 between start1-end1
    }
}

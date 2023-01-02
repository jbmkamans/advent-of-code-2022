package day08;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day08 {
    public static int MAX_HEIGHT = 9; // max height of a tree

    public static void main(String[] args) throws FileNotFoundException {
        // get input as a int[][]
        int[][] forest = getForest();

        // make a "shadow" array, where each "true" means that tree is visible, and "false" means invisible
        boolean[][] shadowForest = new boolean[forest.length][forest.length];

        // forest edges are always visible
        Arrays.fill(shadowForest[0], true);
        Arrays.fill(shadowForest[forest.length - 1], true);

        // look from left to right, then right to left
        int[] maxHeightPerRow = lookFromLeftToRight(forest, shadowForest);
        lookFromRightToLeft(forest, shadowForest, maxHeightPerRow);

        // look from top to bottom, then bottom to top
        int[] maxHeightPerClmn = lookFromTopToBottom(forest, shadowForest);
        lookFromBottomToTop(forest, shadowForest, maxHeightPerClmn);

        // answer part 1: number of visible trees, i.e. count the number of "true" in the shadow forest
        long numberOfVisibleTrees = Stream.of(shadowForest).mapToLong(
                forestRow -> IntStream.range(0, forestRow.length) // use an IntStream, because a BooleanStream doesn't exist
                        .mapToObj(i -> forestRow[i]) // get the booleans from the forestRow
                        .filter(b -> b) // filter out "false", so keep "true"
                        .count() // how many times does this row have "true", i.e. how many visible trees are in this row?
        ).sum(); // sum all the rows, i.e. how many visible trees does the forest have?

        System.out.printf("Visible trees: %d%n", numberOfVisibleTrees);

        // print forests, debug purposes
        for (int row = 0; row < shadowForest.length; row++) {
            // print only the visible trees from the forest
            for (int clmn = 0; clmn < shadowForest[row].length; clmn++) {
                System.out.print(shadowForest[row][clmn] ? forest[row][clmn] : "_");
            }

            // print "shadow" forest
            System.out.print("\t\t");
            for (int clmn = 0; clmn < shadowForest[row].length; clmn++) {
                System.out.print(shadowForest[row][clmn] ? '1' : '0');
            }

            // print forest
            System.out.print("\t\t");
            for (int clmn = 0; clmn < shadowForest[row].length; clmn++) {
                System.out.print(forest[row][clmn]);
            }

            System.out.println();
        }
    }

    private static int[][] getForest() throws FileNotFoundException {
//        File file = new File("src/day08/day08-test.txt");
        File file = new File("src/day08/day08-real.txt");
        Scanner input = new Scanner(file);

        int[][] forest = null; // cannot initialize, because size is not known.

        int rowToFill = 0; // need this index to know which row in forest to fill
        while (input.hasNext()) {
            String line = input.nextLine();

            if (forest == null) {
                int size = line.length();
                forest = new int[size][size]; // assumption: forest is square, so same width and length
            }

            // fill this row of the forest, convert string to int[]
            forest[rowToFill++] = line.chars().map(i -> i - '0').toArray();
        }

        return forest;
    }

    /**
     * Looks from left to right at the forest, and notes which trees are visible.
     *
     * @return an array with the max height found per row, needed for optimizing looking right to left
     */
    private static int[] lookFromLeftToRight(int[][] forest, boolean[][] shadowForest) {
        int[] maxHeightPerRow = new int[forest.length];

        // go through all the rows, skip rows 0 and forest.length-1, those are always visible and already handled
        for (int row = 1; row < forest.length - 1; row++) {
            // keep track of current max height in this row, so loop below can be skipped if max height is reached
            int currentMaxHeight = -1;

            // in each row, go through all the columns, i.e. the individual trees
            for (int clmn = 0; clmn < forest[row].length; clmn++) {
                if (currentMaxHeight == MAX_HEIGHT) {
                    break; // stop processing this row, every other tree is not higher
                }

                int tree = forest[row][clmn];

                if (tree > currentMaxHeight) {
                    shadowForest[row][clmn] = true;
                    currentMaxHeight = tree;
                }
            }
            // save max height of this row
            maxHeightPerRow[row] = currentMaxHeight;
        }

        return maxHeightPerRow;
    }

    /**
     * Looks from right to left at the forest, and notes which trees are visible.
     *
     * @param maxHeightPerRow an int[] with the max height found per forest row, i.e. the return value of 'lookFromLeftToRight'
     */
    private static void lookFromRightToLeft(int[][] forest, boolean[][] shadowForest, int[] maxHeightPerRow) {
        // see "lookFromLeftToRight" for comments, this code is almost identical
        // only the column-loop is reversed, and we can use maxHeightPerRow instead of MAX_HEIGHT, a (small) optimization

        for (int row = 1; row < forest.length - 1; row++) {
            int currentMaxHeight = -1;
            for (int clmn = forest[row].length - 1; clmn >= 0; clmn--) {
                if (currentMaxHeight == maxHeightPerRow[row]) {
                    break; // stop processing this row, every other tree is not higher
                }

                int tree = forest[row][clmn];

                if (tree > currentMaxHeight) {
                    shadowForest[row][clmn] = true;
                    currentMaxHeight = tree;
                }
            }
        }
    }

    private static int[] lookFromTopToBottom(int[][] forest, boolean[][] shadowForest) {
        // identical to "lookFromLeftToRight", only now from top to bottom so the loops for row & column are reversed

        int[] maxHeightPerClmn = new int[forest.length];

        for (int clmn = 1; clmn < forest[0].length - 1; clmn++) {
            int currentMaxHeight = -1;
            for (int row = 0; row < forest.length; row++) {
                if (currentMaxHeight == MAX_HEIGHT) {
                    break; // stop processing this column, every other tree is not higher
                }

                int tree = forest[row][clmn];

                if (tree > currentMaxHeight) {
                    shadowForest[row][clmn] = true;
                    currentMaxHeight = tree;
                }
            }
            maxHeightPerClmn[clmn] = currentMaxHeight;
        }
        return maxHeightPerClmn;
    }

    private static void lookFromBottomToTop(int[][] forest, boolean[][] shadowForest, int[] maxHeightPerClmn) {
        // identical to "lookFromRightToLeft", only now from bottom to top so the loops for row & column are reversed

        for (int clmn = 1; clmn < forest.length - 1; clmn++) {
            int currentMaxHeight = -1;
            for (int row = forest.length - 1; row >= 0; row--) {
                if (currentMaxHeight == maxHeightPerClmn[clmn]) {
                    break; // stop processing this column, every other tree is not higher
                }

                int tree = forest[row][clmn];

                if (tree > currentMaxHeight) {
                    shadowForest[row][clmn] = true;
                    currentMaxHeight = tree;
                }
            }
        }
    }
}

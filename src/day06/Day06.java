package day06;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Day06 {
    public static void main(String[] args) throws IOException {
//        File file = new File("src/day06/day06-test.txt");
        File file = new File("src/day06/day06-real.txt");
        Scanner input = new Scanner(file);

        // while-loop only needed for test input file, because the real input file is only one line.
        while (input.hasNext()) {
            String line = input.nextLine();

            System.out.println("\nPART 1\n");
            int sequenceSize = 4;
            firstIdea_readWithStringReader(line, sequenceSize);
            secondIdea_readAsCharArray_useIndexToRead(line, sequenceSize);
            thirdIdea_readAsCharArray_useListSizeInsteadOfIndexToRead(line, sequenceSize);
            fourthIdea_readWithScannerNextChar(line, sequenceSize);
            fifthIdea_justWriteItOut(line);
            sixthIdea_justWriteItOut_withRecursiveFunction(line, sequenceSize);

            System.out.println("\nPART 2\n");
            sequenceSize = 14;
            firstIdea_readWithStringReader(line, sequenceSize);
            secondIdea_readAsCharArray_useIndexToRead(line, sequenceSize);
            thirdIdea_readAsCharArray_useListSizeInsteadOfIndexToRead(line, sequenceSize);
            fourthIdea_readWithScannerNextChar(line, sequenceSize);
            sixthIdea_justWriteItOut_withRecursiveFunction(line, sequenceSize);
        }

    }

    public static void firstIdea_readWithStringReader(String line, final int SEQUENCE_SIZE) throws IOException {
        StringReader reader = new StringReader(line); // reader that reads a string per character
        int valueRead; // the value currently read
        List<Integer> charsRead = new ArrayList<>(); // container for all the read characters up to that point

        if ((valueRead = reader.read()) != -1) { // can we read? and if "yes", read it.
            List<Integer> lastDistinctChars = Collections.emptyList();
            do {
                // count the number of successive distinct chars from the previous iteration
                final int amountOfSuccessiveDistinctChars = lastDistinctChars.size();

                // from the tail of charsRead, get the last successive distinct characters
                lastDistinctChars = charsRead.subList(charsRead.size() - amountOfSuccessiveDistinctChars, charsRead.size());

                // if the value read is present, update "lastDistinctChars" so it contains only distinct chars
                if (lastDistinctChars.contains(valueRead)) {
                    int indexDuplicateChar = lastDistinctChars.indexOf(valueRead);
                    lastDistinctChars = lastDistinctChars.subList(indexDuplicateChar + 1, amountOfSuccessiveDistinctChars);
                }

                // a bit strange, to add the value to the sublist, but if we use "charsRead.add" we get an
                // ConcurrentModificationException when calling a method on "lastDistinctChars"...
                // also, this really works, i.e. it updates the backing list ("charsRead")
                lastDistinctChars.add(valueRead);
            } while ((valueRead = reader.read()) != -1 && lastDistinctChars.size() < SEQUENCE_SIZE);
        }

        System.out.println("firstIdea_readWithStringReader");
        System.out.printf("\tanswer: %d, charsRead: %s%n", charsRead.size(), charsRead);
    }

    public static void secondIdea_readAsCharArray_useIndexToRead(String line, final int SEQUENCE_SIZE) {
        // use an array of chars instead of a StringReader
        // downside is I need an "indexToRead" to know which char to read next,
        // and with the StringReader I can just read the next char (but I do need a variable for the read value).
        char[] chars = line.toCharArray();
        int indexToRead = 0;

        List<Character> charsRead = new ArrayList<>();
        List<Character> lastDistinctChars = new ArrayList<>(SEQUENCE_SIZE);

        while (lastDistinctChars.size() < SEQUENCE_SIZE && indexToRead < chars.length) {
            char currentChar = chars[indexToRead++]; // get next char

            int indexDuplicate = lastDistinctChars.indexOf(currentChar);
            if (indexDuplicate >= 0) {
                lastDistinctChars.subList(0, indexDuplicate + 1).clear();
            }

            lastDistinctChars.add(currentChar);
            charsRead.add(currentChar);
        }

        System.out.println("secondIdea_readAsCharArray_useIndexToRead");
        System.out.printf("\tanswer: %d, charsRead: %s%n", indexToRead, charsRead);
    }

    public static void thirdIdea_readAsCharArray_useListSizeInsteadOfIndexToRead(String line, final int SEQUENCE_SIZE) {
        // same as second idea, but I don't actually need an "indexToRead",
        // I can just use the size of the "charsRead" to know which char to read next.
        char[] chars = line.toCharArray();

        List<Character> signalStart = new ArrayList<>(SEQUENCE_SIZE);
        List<Character> charsRead = new ArrayList<>();

        while (signalStart.size() < SEQUENCE_SIZE && charsRead.size() < chars.length) {
            char currentChar = chars[charsRead.size()]; // get next char

            int indexDuplicate = signalStart.indexOf(currentChar);
            if (indexDuplicate >= 0) {
                signalStart.subList(0, indexDuplicate + 1).clear();
            }

            signalStart.add(currentChar);
            charsRead.add(currentChar);
        }

        System.out.println("thirdIdea_readAsCharArray_useListSizeInsteadOfIndexToRead");
        System.out.printf("\tanswer: %d, charsRead: %s%n", charsRead.size(), charsRead);
    }

    public static void fourthIdea_readWithScannerNextChar(String line, final int SEQUENCE_SIZE) throws IOException {
        // Same idea as firstIdea, but use Scanner with "nextChar" instead of StringReader.
        // I used StringReader in the first place, because I wanted to read the input character for character,
        // and Scanner does not have a nextChar method. Apparently it sort of does, by using a delimiter of "".
        // I also found that .next() accepts a pattern to get the next "token" that matches that pattern, but using
        // the default delimiter with .next(".") does not work (gives an InputMismatchException)...

        Scanner reader = new Scanner(line);
        reader.useDelimiter(""); // use "" as delimiter, so .next() only returns one character

        List<Character> charsRead = new ArrayList<>();
        List<Character> lastDistinctChars = Collections.emptyList();

        while (reader.hasNext() && lastDistinctChars.size() < SEQUENCE_SIZE) {
            char valueRead = reader.next().charAt(0); // get next char

            final int amountOfSuccessiveDistinctChars = lastDistinctChars.size();

            lastDistinctChars = charsRead.subList(charsRead.size() - amountOfSuccessiveDistinctChars, charsRead.size());

            if (lastDistinctChars.contains(valueRead)) {
                int indexDuplicateChar = lastDistinctChars.indexOf(valueRead);
                lastDistinctChars = lastDistinctChars.subList(indexDuplicateChar + 1, amountOfSuccessiveDistinctChars);
            }

            lastDistinctChars.add(valueRead);
        }

        System.out.println("fourthIdea_readWithScannerNextChar");
        System.out.printf("\tanswer: %d, charsRead: %s%n", charsRead.size(), charsRead);
    }

    public static void fifthIdea_justWriteItOut(String line) {
        // just write it out
        // quite inefficient, because it compares the same letters multiple times

        char[] chars = line.toCharArray();

        int charsRead = 3;
        boolean foundStart = false;

        while (charsRead < chars.length && !foundStart) {
            charsRead++;

            // don't check chars[charsRead] because charsRead is one-based and the array indices are zero-based!
            if (chars[charsRead - 4] != chars[charsRead - 3]
                    && chars[charsRead - 4] != chars[charsRead - 2]
                    && chars[charsRead - 4] != chars[charsRead - 1]
                    && chars[charsRead - 3] != chars[charsRead - 2]
                    && chars[charsRead - 3] != chars[charsRead - 1]
                    && chars[charsRead - 2] != chars[charsRead - 1]
            ) {
                foundStart = true;
            }
        }

        List<Character> charactersRead = IntStream.range(0, charsRead).mapToObj(i -> chars[i]).toList();
        System.out.println("fifthIdea_justWriteItOut");
        System.out.printf("\tanswer: %d, charsRead: %s%n", charsRead, charactersRead);
    }

    public static void sixthIdea_justWriteItOut_withRecursiveFunction(String line, final int SEQUENCE_SIZE) {
        // just write it out
        // quite inefficient, because it compares the same letters multiple times

        int charsRead = SEQUENCE_SIZE - 1;
        boolean foundStart = false;

        while (charsRead < line.length() && !foundStart) {
            charsRead++;
            char[] chars = line.substring(charsRead - SEQUENCE_SIZE, charsRead).toCharArray();
            foundStart = sixthIdea_helperFunction_allDistinctChars(chars);
        }

        List<Character> charactersRead = line.chars().limit(charsRead).mapToObj(c -> (char) c).toList();
        System.out.println("sixthIdea_justWriteItOut_withRecursiveFunction");
        System.out.printf("\tanswer: %d, charsRead: %s%n", charsRead, charactersRead);
    }

    public static boolean sixthIdea_helperFunction_allDistinctChars(char[] chars) {
        if (chars.length == 2) {
            return chars[0] != chars[1];
        }

        char head = chars[0];
        char[] tail = new char[chars.length - 1];
        System.arraycopy(chars, 1, tail, 0, tail.length);

        boolean allDistinct = true;
        for (int i = 1; i < chars.length; i++) {
            allDistinct &= head != chars[i];
        }

        return allDistinct & sixthIdea_helperFunction_allDistinctChars(tail);
    }
}

package day05;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day05Part2 {
    public static void main(String[] args) throws FileNotFoundException {
//        File file = new File("src/day05/day05-test.txt");
        File file = new File("src/day05/day05-real.txt");
        Scanner input = new Scanner(file);

        // lines with crate letters start either with a [ or with multiple spaces
        String crateLettersRegex = "^(\\[|\\p{javaWhitespace}{2,})";

        HashMap<Integer, Deque<Character>> crateStacks = new HashMap<>();
        while (input.hasNext()) {
            String line = input.nextLine();

            if (line.startsWith("[") || line.startsWith("  ")) {
                // determine crate letter per 4 characters: "[X] "
                // assumption: a crate consists of only one letter
                for (int i = 0; i < line.length() / 4.0; i++) {
                    int stackNumber = i + 1;

                    // initialize Stack if not yet known
                    if (!crateStacks.containsKey(stackNumber)) {
                        crateStacks.put(stackNumber, new ArrayDeque<>());
                    }

                    // assumption: a crate consists of one one letter
                    char crate = line.charAt(i * 4 + 1);

                    if (crate == ' ') {
                        continue; // don't add an empty crate
                    }

                    // add crate to correct stack
                    // always add to tail, because we read the input from top to bottom
                    crateStacks.get(stackNumber).addLast(crate);
                }

            } else if (line.startsWith("move")) {
                String[] parts = line.split(" ");
                int amountOfCratesToMove = Integer.parseInt(parts[1]);
                int fromStack = Integer.parseInt(parts[3]);
                int toStack = Integer.parseInt(parts[5]);

                // introduce a helper to temporarily contain popped crates,
                // and later pop the crates back to the correct stack,
                // so the order of the crates stays the same.
                Stack<Character> helper = new Stack<>();
                while (amountOfCratesToMove-- > 0) {
                    // remove top crate from fromStack
                    char crate = crateStacks.get(fromStack).pop();

                    // add crate to helper
                    helper.push(crate);
                }

                // add crates from helper to correct stack
                while (!helper.isEmpty()) {
                    crateStacks.get(toStack).push(helper.pop());
                }
            }
        }

        System.out.print("Answer part 2: ");
        for (int i = 0; i < crateStacks.size(); i++) {
            System.out.print(crateStacks.get(i + 1).peek());
        }
    }
}

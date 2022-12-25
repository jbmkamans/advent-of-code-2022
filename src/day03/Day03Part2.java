package day03;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day03Part2 {
    public static void main(String[] args) throws FileNotFoundException {
//        File file = new File("src/day03/day03-test.txt");
        File file = new File("src/day03/day03-real.txt");
        Scanner input = new Scanner(file);

        int totalPriorities = 0; // answer of part B
        while (input.hasNext()) {
            // get contents of group 1, 2 and 3 as sets (no duplicates)
            Set<Character> group1 = input.nextLine().chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
            Set<Character> group2 = input.nextLine().chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
            Set<Character> group3 = input.nextLine().chars().mapToObj(c -> (char) c).collect(Collectors.toSet());

            // only keep the contents (= letters) which each group shares with each other
            group1.retainAll(group2);
            group1.retainAll(group3);

            // there should be only one letter left in group 1, so just get it.
            char letter = group1.stream().findFirst().orElseThrow();

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

            // Part 2: sum priorities
            totalPriorities += priority;
        }
        System.out.printf("Sum of priorities is: %d", totalPriorities);
    }
}

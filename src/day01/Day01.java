package day01;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Day01 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("src/day01/day01-real.txt"));

        ArrayList<Elf> elves = new ArrayList<>();

        // add the first elf, keep reference to current elf, needed for upcoming loop
        Elf currentElf = new Elf();
        elves.add(currentElf);

        while (input.hasNext()) {
            String line = input.nextLine();

            if (line.isBlank()) {
                // add new elf
                currentElf = new Elf();
                elves.add(currentElf);
            } else {
                // add calorie item to current elf
                currentElf.add(Integer.valueOf(line));
            }
        }

        // Part 1: how many calories does the elf with the most calories have?
        int maxCalories = elves.stream().map(Elf::getTotalCalories).max(Integer::compareTo).get();
        System.out.println(maxCalories);

        // Part 2: how many calories do the top three elves with the most calories have?
        int totalCaloriesTopThree = elves.stream()
                .map(Elf::getTotalCalories)
                .sorted(Comparator.reverseOrder()) // reverse order, so top = most calories
                .limit(3) // get top three
                .mapToInt(i -> i) // convert Stream<Integer> to IntStream, so we have access to .sum()
                .sum();
        System.out.println(totalCaloriesTopThree);
    }
}

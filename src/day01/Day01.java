package day01;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day01 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("src/day01/day01-test.txt"));

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

        int maxCalories = elves.stream().map(Elf::getTotalCalories).max(Integer::compareTo).get();
        System.out.println(maxCalories);
    }
}

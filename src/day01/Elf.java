package day01;

import java.util.ArrayList;

/**
 * Wrapper class, represents a list of food items (represented only by having calories).
 */
public class Elf {
    private ArrayList<Integer> food = new ArrayList<>();

    public void add(int calories) {
        food.add(calories);
    }

    public int getTotalCalories() {
        return food.stream().mapToInt(a -> a).sum();
    }
}

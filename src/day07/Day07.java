package day07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Day07 {
    public static void main(String[] args) throws FileNotFoundException {
//        File file = new File("src/day07/day07-test.txt");
        File file = new File("src/day07/day07-real.txt");
        Scanner input = new Scanner(file);

        FileSystem fileSystem = new FileSystem();

        while (input.hasNext()) {
            fileSystem.parseLine(input.nextLine());
        }

        fileSystem.listSizes(fileSystem.root);

        // Part A: what is the total size of all 100k directories?
        int totalSize = fileSystem.sum100kDirectories(fileSystem.root);
        System.out.printf("Size of 100k directories: %d%n", totalSize);

        // Part B: what is the minimum size of the directory to delete, to free up just enough space?
        int sizeRoot = fileSystem.root.getSize();
        int spaceNow = 70_000_000 - sizeRoot;
        int spaceNeeded = 30_000_000 - spaceNow;

        List<Integer> sizes = fileSystem.getDirectorySizes(fileSystem.root);
        int minDeleteSize = sizes.stream()
                .mapToInt(s -> s) // convert to primitive int
                .filter(i -> i >= spaceNeeded) // keep large enough directories
                .min() // get the smallest one
                .getAsInt();
        System.out.printf("Deleted dir size: %d", minDeleteSize);
    }
}

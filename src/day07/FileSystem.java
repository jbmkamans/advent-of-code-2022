package day07;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileSystem {
    public Directory root;
    private Directory cwd; // current working directory

    public FileSystem() {
        root = new Directory("/");
        cwd = root;
    }

    public void parseLine(String line) {
        if (line.startsWith("$ ls") || line.startsWith("dir")) {
            return; // skip, only interested in "cd" and actual files with sizes
        }

        if (line.startsWith("$ cd")) { // yeah, cd!
            String dest = line.substring(5);
            cd(dest); // change directory to parsed destination
        } else { // yeah, file with size!
            String[] sizeAndName = line.split(" ");
            String name = sizeAndName[1];
            int size = Integer.parseInt(sizeAndName[0]);
            cwd.mkFile(name, size); // make file in cwd
        }
    }

    public void cd(String dest) {
        if (dest.equals("..")) { // go up one level
            cwd = cwd.parent;
            return;
        } else if (dest.equals("/")) { // go to root
            cwd = root;
            return;
        }

        // does cwd have a directory with this name?
        Optional<Directory> optional = cwd.contents.stream()
                .filter(f -> f.name.equals(dest)) // has correct name
                .filter(f -> f instanceof Directory) // is directory
                .map(f -> (Directory) f) // cast to correct data type
                .findAny();

        // either move to the found directory, or make it and then still move to it
        cwd = optional.orElseGet(() -> cwd.mkDir(dest));
    }

    // Handy, lists all the names and sizes of the given directory and its child directories
    public void listSizes(Directory dir) {
        System.out.printf("%s, %d%n", dir.name, dir.getSize());

        for (FileSystemItem content : dir.contents) {
            if (content instanceof Directory) {
                listSizes((Directory) content);
            }
        }
    }

    // Part A: sums all directory sizes with a size of at most 100_000.
    // Must be called with root as argument
    public int sum100kDirectories(Directory dir) {
        int size = dir.getSize();
        if (size > 100_000) {
            size = 0;
        }

        for (FileSystemItem content : dir.contents) {
            if (content instanceof Directory) {
                size += sum100kDirectories((Directory) content);
            }
        }

        return size;
    }

    // Part B: get sizes of directory and its child directories
    public List<Integer> getDirectorySizes(Directory dir) {
        List<Integer> sizes = new ArrayList<>();
        sizes.add(dir.getSize());

        for (FileSystemItem content : dir.contents) {
            if (content instanceof Directory) {
                sizes.addAll(getDirectorySizes((Directory) content));
            }
        }

        return sizes;
    }

    public class Directory extends FileSystemItem {
        public List<FileSystemItem> contents;

        public Directory(String name) {
            super(name);
            contents = new ArrayList<>();
        }

        public Directory mkDir(String name) {
            Directory dir = new Directory(name);
            dir.parent = this;
            this.contents.add(dir);
            return dir;
        }

        public void mkFile(String name, int size) {
            File file = new File(name, size);
            this.contents.add(file);
        }

        @Override
        public int getSize() {
            return contents.stream().mapToInt(FileSystemItem::getSize).sum();
        }
    }

    private class File extends FileSystemItem {
        private final int size;

        public File(String name, int size) {
            super(name);
            this.size = size;
        }

        @Override
        public int getSize() {
            return size;
        }
    }

    public abstract class FileSystemItem {
        public String name;
        public Directory parent;

        public FileSystemItem(String name) {
            this.name = name;
        }

        public abstract int getSize();
    }
}

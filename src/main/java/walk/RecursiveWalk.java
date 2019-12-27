package walk;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class RecursiveWalk extends Walk {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.printf("Need 2 arguments, but %d were given.\n", args.length);
        } else {
            (new RecursiveWalk()).run(args[0], args[1]);
        }
    }

    @Override
    protected void doPath(String fileName, PrintWriter printWriter) {
        try {
            Path startPath = Paths.get(fileName);
            Files.walkFileTree(startPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                    int hash = FNV(path.toFile());
                    printWriter.format("%08x %s\n", hash, path.toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (InvalidPathException | NoSuchFileException e) {
            System.err.printf("Invalid path: %s\n", fileName);
            printWriter.format("%08x %s\n", 0, fileName);
        } catch (IOException e) {
            System.err.println("IO error while walking file tree.");
        }
    }

}

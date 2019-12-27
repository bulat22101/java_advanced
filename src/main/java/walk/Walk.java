package walk;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Walk {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.printf("Need 2 arguments, but %d were given.\n", args.length);
        } else {
            (new Walk()).run(args[0], args[1]);
        }
    }

    protected int FNV(File file) {
        int x = 0b10000001000111001001110111000101;
        int p = 16777619;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            while (fileInputStream.available() > 0) {
                int d = fileInputStream.read();
                x = x * p ^ d;
            }
            return x;
        } catch (IOException e) {
            return 0;
        }
    }

    protected void doPath(String fileName, PrintWriter printWriter) {
        int hash = FNV(new File(fileName));
        printWriter.format("%08x %s\n", hash, fileName);
    }

    protected void run(String inputFileName, String outputFileName) {
        try (
                FileInputStream fileInputStream = new FileInputStream(inputFileName);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                PrintWriter printWriter = new PrintWriter(outputFileName, StandardCharsets.UTF_8);
        ) {
            for (String fileName = bufferedReader.readLine(); fileName != null; fileName = bufferedReader.readLine()) {
                doPath(fileName, printWriter);
            }
        } catch (IOException e) {
            System.err.println("Error while reading and/or writing given files.\n");
        }
    }
}
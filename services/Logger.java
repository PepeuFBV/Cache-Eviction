package services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Logger {

    private static final String defaultLogPath = "log/log.txt";
    private static Logger instance;

    private Logger() {
        try {
            createFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    private String getCurrentTime() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private void createFile() throws IOException {
        File logFile = new File(defaultLogPath);
        if (!logFile.exists()) {
            try (FileWriter fileWriter = new FileWriter(logFile)) {
                fileWriter.write("Log file created at " + java.time.LocalDateTime.now());
                System.out.println("Log file created: " + logFile.getAbsolutePath());
            } catch (IOException e) {
                throw new IOException("Failed to create log file, check the path and try again.");
            }
        }
    }

    /*private void createTreeFile() throws IOException {
        File treeFile = new File(defaultTreeLogPath);
        System.out.println("Creating a new tree file...");
        try (FileWriter fileWriter = new FileWriter(treeFile)) {
            fileWriter.write("Tree file created at " + java.time.LocalDateTime.now());
            System.out.println("Tree file created: " + treeFile.getAbsolutePath());
        } catch (IOException e) {
            throw new IOException("Failed to create tree file, check the path and try again.");
        }
    }*/

    public void log(String message) {
        try (FileWriter fileWriter = new FileWriter(defaultLogPath, true)) {
            fileWriter.write("\n" + message);
        } catch (IOException e) {
            System.out.println("Failed to write to log file");
        }
    }

    public String clearLog() throws IOException {
        System.out.println("Are you sure you want to clear the log? (y/n)");
        log("[" + getCurrentTime() + "] Log clearing requested");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            try (FileWriter fileWriter = new FileWriter(defaultLogPath)) {
                fileWriter.write("Log file created at " + java.time.LocalDateTime.now());
            }
            log(""); // log method is bugged on the first line
            log("\n\nStarting services at " + java.time.LocalDateTime.now());
            log("[" + getCurrentTime() + "] Log cleared");
            return "Log cleared\n";
        } else {
            log("[" + getCurrentTime() + "] Log clearing canceled");
            return "Operation canceled\n";
        }
    }

    /*public void saveTree(Node root) {
        try {
            createTreeFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (FileWriter fileWriter = new FileWriter(defaultTreeLogPath)) {
            fileWriter.write(root.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
package services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Logger {

    private static final String defaultLogPath = "Log/log.txt";
    private static Logger instance;

    private Logger() {
        try {
            createFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    private void createFile() throws IOException {
        File logFile = new File(defaultLogPath);
        File logDir = logFile.getParentFile();
        if (!logDir.exists()) {
            if (logDir.mkdirs()) {
                System.out.println("Log directory created: " + logDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create log directory: " + logDir.getAbsolutePath());
            }
        }
        if (!logFile.exists()) {
            try (FileWriter fileWriter = new FileWriter(logFile)) {
                fileWriter.write("");
                System.out.println("Log file created: " + logFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void log(String message) {
        try (FileWriter fileWriter = new FileWriter(defaultLogPath, true)) {
            fileWriter.write("\n" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearLog() {
        System.out.println("Are you sure you want to clear the log? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            try (FileWriter fileWriter = new FileWriter(defaultLogPath)) {
                fileWriter.write("");
                System.out.println("Log cleared.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Operation canceled.");
        }
    }
}
package service.log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    private static boolean fileStarted = false;
    private String path = "log/log.txt";
    private LogOrigin origin = LogOrigin.LOGGER;

    public enum LogOrigin {
        CACHE,
        DATABASE,
        CLIENT,
        SERVICE,
        LOGGER
    }

    public Logger(LogOrigin origin) throws RuntimeException {
        try {
            if (!fileStarted) { // start the log file starting message only once (also creates log file if needed)
                createFile();
                fileStarted = true;
            }
            this.origin = origin;
            log("Started " + origin.toString());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // fileName must also contain the file extension (e.g. ".txt")
    public Logger(LogOrigin origin, String path, String fileName) throws RuntimeException {
        try {
            if (!fileStarted) { // start the log file starting message only once (also creates log file if needed)
                createFile();
                fileStarted = true;
            }
            this.path = path + fileName;
            this.origin = origin;
            log("Started " + origin.toString());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static String getCurrentTime() {
        return "(" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + ")";
    }

    private String getCurrentOrigin() {
        return "[" + origin + "]";
    }

    private void createFile() throws IOException {
        File logFile = new File(path);
        if (!logFile.exists()) {
            try (FileWriter fileWriter = new FileWriter(logFile)) {
                log("Log file created at " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
            } catch (IOException e) {
                throw new IOException("Failed to create log file, check the path and try again.");
            }
        }
    }

    public void log(String message) throws RuntimeException {
        try (FileWriter fileWriter = new FileWriter(path, true)) {
            fileWriter.write("\n" + getCurrentTime() + " " + getCurrentOrigin() + " " + message);
        } catch (IOException e) {
            throw new RuntimeException("Failed to log message, check the path and try again.");
        }
    }

    public void clearLog() throws IOException {
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write("Log file created at " + java.time.LocalDateTime.now());
            log(""); // log method is bugged on the first line
            log("\nStarting services at " + java.time.LocalDateTime.now()); // extra \n to separate the log
            log("[" + getCurrentTime() + "] Log cleared");
        } catch (IOException e) {
            throw new IOException("Failed to clear log file, check the path and try again.");
        }
    }

    public String findOperations(int id) {
        String addPattern = "Adding Service Order with ID " + id;
        String removePattern = "Removing Service Order with ID " + id;
        List<String> operations = new ArrayList<>();
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (kmpSearch(line, addPattern) || kmpSearch(line, removePattern)) {
                    operations.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read log file, check the path and try again.");
        }

        for (String operation : operations) {
            result.append(operation).append("\n");
        }

        return result.toString();
    }

    private boolean kmpSearch(String text, String pattern) {
        int[] lps = computeLPSArray(pattern);
        int i = 0, j = 0;
        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == pattern.length()) {
                return true;
            } else if (i < text.length() && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return false;
    }

    private int[] computeLPSArray(String pattern) {
        int length = 0;
        int i = 1;
        int[] lps = new int[pattern.length()];
        lps[0] = 0;
        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

}

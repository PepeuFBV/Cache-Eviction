package services;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    private List<String> logs = new ArrayList<>();
    private static final String defaultLogPath = "/Log/log.txt";
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
        try (FileWriter fileWriter = new FileWriter(Logger.defaultLogPath)) {
            fileWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String message) {
        logs.add("[" + java.time.LocalDateTime.now() + "] - " + message);
    }

    private void writeInFile(String path) throws IOException {
        try (FileWriter fileWriter = new FileWriter(path)) {
            for (String log : logs) {
                fileWriter.write(log + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLogsToDefaultPath() throws IOException {
        writeInFile(defaultLogPath);
    }

    public void resetLogs() {
        logs.clear();
    }
}
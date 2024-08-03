import java.util.ArrayList;
import java.util.List;

public class Logger {

    private List<String> logs = new ArrayList<>();
    private static String defaultLogPath = "../Log/log.txt";
    private static Logger instance;

    public void log(String message) {
        logs.add(message);
    }
}
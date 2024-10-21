package compression;

import java.util.HashMap;
import java.util.Map;

public class KMPFrequency {

    public static Map<Character, Integer> calculateFrequencies(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

}

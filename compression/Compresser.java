package compression;

import java.util.HashMap;
import java.util.Map;

public class Compresser {

    private final KMP kmp;

    public Compresser(String pattern) {
        kmp = new KMP(pattern);
    }

    public String compress(String text) {
        Map<String, String> dictionary = new HashMap<>();
        StringBuilder response = new StringBuilder(); // compressed message
        int i = 0;

        while (i < text.length()) {
            int matchI = kmp.search(text.substring(i));
            if (matchI == -1) { // no match found
                String match = text.substring(i, i + 1);
                if (!dictionary.containsKey(match)) { // add new match to dictionary
                    dictionary.put(match, Integer.toString(dictionary.size()));
                }
                response.append(dictionary.get(match));
                i += match.length();
            } else { // match found
                response.append(text.charAt(i));
                i++;
            }
        }

        return response.toString();
    }

    public String decompress(String text, Map<String, String> dictionary) {
        StringBuilder response = new StringBuilder(); // decompressed message

        for (char c : text.toCharArray()) {
            String k = Character.toString(c);
            response.append(dictionary.getOrDefault(k, k)); // add match from dictionary or character itself
        }

        return response.toString();
    }
    
}

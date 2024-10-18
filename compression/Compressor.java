package compression;

public class Compressor {

    public String compress(String text) {
        int[] lps = computeLPSArray(text);
        StringBuilder compressed = new StringBuilder();
        int i = 0;
        while (i < text.length()) {
            int len = lps[i];
            if (len > 0) {
                compressed.append("(").append(text.substring(i - len + 1, i + 1)).append(")");
                i += len;
            } else {
                compressed.append(text.charAt(i));
                i++;
            }
        }
        return compressed.toString();
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

    public String decompress(String compressedText) {
        StringBuilder decompressed = new StringBuilder();
        int i = 0;
        while (i < compressedText.length()) {
            if (compressedText.charAt(i) == '(') {
                int j = i + 1;
                while (compressedText.charAt(j) != ')') {
                    j++;
                }
                String repeatedPattern = compressedText.substring(i + 1, j);
                decompressed.append(repeatedPattern);
                i = j + 1;
            } else {
                decompressed.append(compressedText.charAt(i));
                i++;
            }
        }
        return decompressed.toString();
    }
    
}
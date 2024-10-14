package compression;

public class KMP {

    private final String pattern; // pattern to search
    private final int[] lps; // longest Prefix Suffix array

    public KMP(String pattern) {
        this.pattern = pattern;
        this.lps = new int[pattern.length()];
        computeLPS();
    }

    // find the lps array values used in the search method
    private void computeLPS() {
        int length = 0;
        int i = 0;
        lps[0] = 0;

        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(length)) { // if characters match, increment length and store in lps
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) { // set length to previous lps value
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
    }

    public int search(String text) {
        int i = 0;
        int j = 0;

        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == pattern.length()) {
                return i - j;
            } else if (i < text.length() && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return -1;
    }

}

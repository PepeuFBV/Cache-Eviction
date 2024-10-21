package compression;

import java.util.Map;

public class Compressor {

    private final HuffmanTree huffmanTree;

    public Compressor() {
        this.huffmanTree = new HuffmanTree();
    }

    public String compress(String text) {
        Map<Character, Integer> frequencyMap = KMPFrequency.calculateFrequencies(text);
        huffmanTree.buildTree(frequencyMap);
        return huffmanTree.compress(text);
    }

    public String decompress(String compressedText) {
        return huffmanTree.decompress(compressedText);
    }

}

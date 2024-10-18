package compression;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree {

    private final Map<Character, String> huffmanCodeMap = new HashMap<>();
    private HuffmanNode root;

    public void buildTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>((a, b) -> a.frequency - b.frequency);

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            assert right != null;
            HuffmanNode newNode = new HuffmanNode('\0', left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;
            priorityQueue.add(newNode);
        }

        root = priorityQueue.poll();
        buildCodeMap(root, "");
    }

    private void buildCodeMap(HuffmanNode node, String code) {
        if (node == null) {
            return;
        }
        if (node.character != '\0') {
            huffmanCodeMap.put(node.character, code);
        }
        buildCodeMap(node.left, code + "0");
        buildCodeMap(node.right, code + "1");
    }

    public String compress(String text) {
        StringBuilder compressed = new StringBuilder();
        for (char c : text.toCharArray()) {
            compressed.append(huffmanCodeMap.get(c));
        }
        return compressed.toString();
    }

    public String decompress(String compressedText) {
        StringBuilder decompressed = new StringBuilder();
        HuffmanNode currentNode = root;
        for (char bit : compressedText.toCharArray()) {
            currentNode = (bit == '0') ? currentNode.left : currentNode.right;
            assert currentNode != null;
            if (currentNode.left == null && currentNode.right == null) {
                decompressed.append(currentNode.character);
                currentNode = root;
            }
        }
        return decompressed.toString();
    }

}

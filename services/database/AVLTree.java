package services.database;

import entities.OS;
import exceptions.treeExceptions.DuplicateEntryException;
import exceptions.treeExceptions.NonExistentEntryException;

public class AVLTree {

    private Node root;

    private int height(Node node) {
        if (node == null) {
            return -1;
        }
        return node.height;
    }

    private int bigger(int a, int b) {
        return a > b ? a : b;
    }

    private int balanceFactor(Node root) {
        return height(root.left) - height(root.right);
    }

    private Node rightRotation(Node root) {
        Node left = root.left;
        root.left = left.right; // moves the right node from the left subtree to the left node of the root tree
        left.right = root; // makes the old root to be the right subtree of the new root, the old left subtree

        root.height = 1 + bigger(height(root.left), height(root.right));
        left.height = 1 + bigger(height(left.left), height(left.right));

        // left becomes the new root
        return left;
    }

    private Node leftRotation(Node root) {
        Node right = root.right;
        root.right = right.left; // moves the left node from the right subtree to the right node of the root tree
        right.left = root; // makes the old root to be the right subtree of the new root, the old right subtree

        root.height = 1 + bigger(height(root.left), height(root.right));
        right.height = 1 + bigger(height(right.left), height(right.right));

        // right becomes the new root
        return right;
    }

    private Node balanceTree(Node root) {
        int balanceFactor = balanceFactor(root);
        int balanceFactorLeft = balanceFactor(root.left);
        int balanceFactorRight = balanceFactor(root.right);

        if (balanceFactor > 1) { // right rotation needed
            if (balanceFactorLeft >= 0) { // simple right rotation
                return rightRotation(root);
            } else { // double right rotation
                root.left = leftRotation(root.left);
                return rightRotation(root);
            }
        } else if (balanceFactor < -1) {
            if (balanceFactorRight <= 0) {
                return leftRotation(root);
            } else {
                root.right = rightRotation(root.right);
                return leftRotation(root);
            }
        }

        return root;
    }

    public void insert(OS serviceOrder) {
        try {
            root = insert(root, serviceOrder);
        } catch (DuplicateEntryException e) {
            System.out.println(e.getMessage());
        }
    }

    private Node insert(Node root, OS serviceOrder) throws DuplicateEntryException {
        // root is null, the tree was empty before, so set new Node as tree's root
        if (root == null) {
            return new Node(serviceOrder);
        }

        if (serviceOrder.getId() < root.getOSId()) {
            root.left = insert(root.left, serviceOrder);
        } else if (serviceOrder.getId() > root.getOSId()) {
            root.right = insert(root.right, serviceOrder);
        } else {
            throw new DuplicateEntryException("OS with ID " + serviceOrder.getId() + " already exists in the tree.");
        }

        root.height = 1 + bigger(height(root.left), height(root.right));

        return balanceTree(root);
    }

    public void remove(OS serviceOrder) {
        try {
            root = remove(root, serviceOrder);
        } catch (NonExistentEntryException e) {
            System.out.println(e.getMessage());
        }
    }

    private Node remove(Node root, OS serviceOrder) throws NonExistentEntryException {
        // TODO: implement
        return null;
    }

}
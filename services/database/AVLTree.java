package services.database;

import entities.OS;
import exceptions.treeExceptions.DuplicateEntryException;
import exceptions.treeExceptions.NonExistentEntryException;

public class AVLTree {

    private Node root;
    private DefaultToStringBehavior defaultToStringBehavior = DefaultToStringBehavior.IN_ORDER;
    public  enum DefaultToStringBehavior {
        IN_ORDER, PRE_ORDER, POST_ORDER
    }

    public int getHeight() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) {
            return -1;
        }
        return node.height;
    }

    private int bigger(int a, int b) {
        return a > b ? a : b;
    }

    private Node minValueNode(Node root) {
        if (root == null) {
            return null;
        }
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    public int getHighestId() {
        return getHighestId(root);
    }

    private int getHighestId(Node root) {
        if (root == null) {
            return -1;
        }
        while (root.right != null) {
            root = root.right;
        }
        return root.getOSId();
    }

    private int balanceFactor(Node root) {
        if (root == null) return 0;
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

        if (balanceFactor > 1) { // right rotation needed
            if (balanceFactor(root.left) >= 0) { // simple right rotation
                return rightRotation(root);
            } else { // double right rotation
                root.left = leftRotation(root.left);
                return rightRotation(root);
            }
        } else if (balanceFactor < -1) {
            if (balanceFactor(root.right) <= 0) {
                return leftRotation(root);
            } else {
                root.right = rightRotation(root.right);
                return leftRotation(root);
            }
        }

        return root;
    }

    public OS searchOS(int id) {
        Node foundNode = search(root, id);
        return foundNode != null ? foundNode.getOS() : null;
    }

    public boolean search(int id) {
        Node foundNode = search(root, id);
        return foundNode != null;
    }

    private Node search(Node root, int id) {
        if (root == null) {
            return null;
        }

        if (id < root.getOSId()) {
            return search(root.left, id);
        } else if (id > root.getOSId()) {
            return search(root.right, id);
        } else {
            return root;
        }
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

    public void remove(int id) throws NonExistentEntryException {
        Node foundNode = search(root, id);
        if (foundNode == null) {
            throw new NonExistentEntryException("OS with ID " + id + " does not exist in the tree.");
        }
        remove(foundNode.getOS());
    }

    public void remove(OS serviceOrder) {
        try {
            root = remove(root, serviceOrder);
        } catch (NonExistentEntryException e) {
            System.out.println(e.getMessage());
        }
    }

    private Node remove(Node root, OS serviceOrder) throws NonExistentEntryException {
        if (root == null) {
            throw new NonExistentEntryException("OS with ID " + serviceOrder.getId() + " does not exist in the tree.");
        }

        if (serviceOrder.getId() < root.getOSId()) {
            root.left = remove(root.left, serviceOrder);
        } else if (serviceOrder.getId() > root.getOSId()) {
            root.right = remove(root.right, serviceOrder);
        } else { // found node to be removed
            if (root.left == null && root.right == null) { // current root is leaf
                return null;
            } else if (root.left == null) {
                root = root.right;
            } else if (root.right == null) {
                root = root.left;
            } else { // has nodes to both its right and left
                Node minValueNode = minValueNode(root.right);
                root.setOS(minValueNode.getOS()); // moves the new service order to the root
                root.right = remove(root.right, minValueNode.getOS()); // removes the old copy of the new root
            }
        }

        root.height = 1 + bigger(height(root.left), height(root.right));

        return balanceTree(root);
    }

    public String inOrder() {
        return inOrder(this.root);
    }

    private String inOrder(Node root) {
        StringBuilder sb = new StringBuilder();
        if (root != null) {
            sb.append(inOrder(root.left));
            sb.append(root.getOS().toString()).append("\n");
            sb.append(inOrder(root.right));
        }
        return sb.toString();
    }

    public String preOrder() {
        return preOrder(this.root);
    }

    private String preOrder(Node root) {
        StringBuilder sb = new StringBuilder();
        if (root != null) {
            sb.append(root.getOS().toString()).append("\n");
            sb.append(preOrder(root.left));
            sb.append(preOrder(root.right));
        }
        return sb.toString();
    }

    public String postOrder() {
        return postOrder(this.root);
    }

    private String postOrder(Node root) {
        StringBuilder sb = new StringBuilder();
        if (root != null) {
            sb.append(postOrder(root.left));
            sb.append(postOrder(root.right));
            sb.append(root.getOS().toString()).append("\n");
        }
        return sb.toString();
    }

    public void changeDefaultToStringBehavior(DefaultToStringBehavior behavior) {
        switch (behavior) {
            case IN_ORDER:
                this.defaultToStringBehavior = DefaultToStringBehavior.IN_ORDER;
                break;
            case PRE_ORDER:
                this.defaultToStringBehavior = DefaultToStringBehavior.PRE_ORDER;
                break;
            case POST_ORDER:
                this.defaultToStringBehavior = DefaultToStringBehavior.POST_ORDER;
                break;
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public String toString() {
        return switch (this.defaultToStringBehavior) {
            case PRE_ORDER -> preOrder();
            case POST_ORDER -> postOrder();
            default -> inOrder();
        };
    }

}
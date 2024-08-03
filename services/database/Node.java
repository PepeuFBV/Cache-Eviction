package services.database;

import entities.OS;

public class Node {
    
    private OS serviceOrder; // private to protect the data better
    int height = 0;
    Node left = null, right = null;

    public Node(OS serviceOrder) {
        setOS(serviceOrder);
    }

    public void setOS(OS serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public OS getOS() {
        return this.serviceOrder;
    }

    public int getOSId() {
        return this.serviceOrder.getId();
    }

}

package services;

import services.database.AVLTree;

public class Service {

    private final Cache cache;
    private final Logger logger;
    private final AVLTree avlTree;

    public Service() {
        this.logger = Logger.getInstance();
        logger.log("\nStarting services at " + java.time.LocalDateTime.now());
        this.cache = new Cache();
        logger.log("Empty cache created");
        this.avlTree = new AVLTree();
        logger.log("Empty AVL tree created");
    }

    public void stopServices() {
        try {
            logger.log("Stopping services at " + java.time.LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

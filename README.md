# Cache Implementation Simulation

Client/server simulation for storing service orders, whilst using a cache to speed up the search for the service orders in the database.

This is only a client usage simulation, as the client side isn't implemented yet. The server side is implemented in fully in pure Java.

## How to run

1. Clone the repository

```bash
git clone https://github.com/PepeuFBV/CacheImpl
```

2. Run the `Main` class

## How it works

The client has a console options to interact with the service orders on the server. The server has a database implemented with AVL Tree structure and a cache implemented with a simple FIFO algorithm.

```
[ 1 ] - Create a new Service Order
[ 2 ] - Search for a Service Order
[ 3 ] - List all Service Orders
[ 4 ] - Alter a Service Order
[ 5 ] - See the cache
[ 6 ] - Remove a Service Order
[ 7 ] - Clear the log
[ 8 ] - Exit

->
```

## How to use

Choose an option and follow the instructions on the console. There is also a log file that will be created (_/log/log.txt_) with all the operations made by the client, as well as the cache status, tree height and the rotations made by the AVL Tree.

## About the AVL Tree

The AVL Tree is a self-balancing binary search tree. It was chosen because it has a good balance between search and insert operations. The tree is implemented in the `AVLTree` class.

## Extra information

The cache has a fixed size of 20 elements, but it can be changed in the `Cache` class. The AVL Tree has no fixed size. The insert, search and alter Service Order operations all add the Service Order to the cache. The remove operation removes the Service Order from the tree, and if it's in the cache, it will be removed from there too.

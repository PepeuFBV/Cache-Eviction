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

The client has a console options to interact with the service orders on the server. The server has a database and cache, both implemented through a Hash Table.

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

Choose an option and follow the instructions on the console. There is also a log file that will be created (_/log/log.txt_) with all the operations made by the client, as well as the cache status, tree height and the rotations made by the AVL Tree. Make sure to clear the log file if you want to start fresh, if the log file creation is triggering an error, create the log directory manually.

## About the Hash Tables

The implemented Hash Tables use the simple division hash function, and the collision resolution is done through the usage of a linked list. The hash table in the cache has a fixed size of 20 elements, and the hash table in the database has a starting size of 127 elements, but it can grow dynamically, if turned on in the a `Service` class method. The growth is through the finding of the next prime number before the new size desired.

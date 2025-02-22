# Cache Implementation Simulation

Client/server simulation for storing service orders, whilst using a cache to speed up the search for the service orders in the database.

This is only a client usage simulation, as the client side isn't implemented yet. The server side is implemented in fully in pure Java.

## How to run

1. Clone the repository

```bash
git clone https://github.com/PepeuFBV/CacheImpl.git
cd CacheImpl
```

2. Run the `Main` class

```bash
javac Main.java
java Main
```

## How it works

The client has a console options to interact with the service orders on the server. The server has a database and cache, both implemented through a HashTable. The cache is used to speed up the search for service orders, and the database is used to store all the service orders. The cache has a fixed size of 20 elements, and the database has a dynamic size and can grow if needed.

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

The implemented HashTables use the simple division hash function, and the collision resolution in the cache is done through linear probing. The hash table in the cache has a fixed size of 20 elements, and the hash table in the database has a starting size of 7 elements, but it can grow dynamically if turned on in a HashTable class method (on by default). The growth is through finding the next prime number before the new size desired, old size * 2 + 1; and rehashing all the elements to the new table.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

Compile:
javac ServerInterface.java
javac Server.java
javac Client.java

Command to run Server
java Server <host> <port> <number_of_nodes>

Command to run Client
java Server <server_ip> <server_port>

Commands supported at Client end:
1. add_edge <node1> <node2>
2. shortest_distance <node1> <node2>
3. get_graph
Compile:
javac ServerInterface.java<br>
javac Server.java<br>
javac Client.java<br>

Command to run Server<br>
java Server <host> <port> <number_of_nodes>

Command to run Client<br>
java Server <server_ip> <server_port>

Commands supported at Client end:<br>
1. add_edge <node1> <node2>
2. shortest_distance <node1> <node2>
3. get_graph

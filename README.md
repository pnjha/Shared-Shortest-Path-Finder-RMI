Compile:<br>
javac ServerInterface.java<br>
javac Server.java<br>
javac Client.java<br>

Command to run Server<br>
java Server host port

Command to run Client<br>
java Client server_ip server_port

Commands supported at Client end:<br>
1. add_edge graph_name(empty for default graph) node1 node2
2. shortest_distance graph_name(empty for default graph) node1 node2
3. get_graph graph_name(empty for default graph)

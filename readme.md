Author: Rafał Surdej

## Basic description:
A simple multiplayer game designed for two people. Both players control a tank and 
their goal is to destroy the opponent. It uses web sockets and TCP protocol
to stream data between players in real time. It implements multithreading and 
synchronization between those threads. It has a primitive pop-up menu serving as
a simple GUI before the actual game. It is made with Java and Swing library.

## How to run:
Be sure you have JDK or JRE installed.

1. Download the file named 'Tanks.jar' from /build folder.
2. Open a terminal and navigate to the folder containing the file mentioned above.
3. Type 'java -jar Tanks.jar' in terminal.

It requires two players to run the game so either you have to connect to other
device with the application running or repeat steps 2 and 3 on your machine.

If you try to play on two different machines, be sure that both have a public IP address.
You can acquire this by either establishing VPN (e.g. Hamachi) or doing the port forwarding
in the routers.

If you run the game on a single machine then in the IP address field type '127.0.0.1'.
If you run on different computers then type their actual IP addresses.

The port is always 10110 .
   
   

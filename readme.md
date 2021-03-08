Authors: Rafał Surdej, Jakub Jurasz

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

Screenshots:

![t1](https://user-images.githubusercontent.com/51239039/110344657-01ae8e80-802e-11eb-84f0-3e5a1f041fbf.PNG)
![t2](https://user-images.githubusercontent.com/51239039/110344667-0410e880-802e-11eb-86c3-68f18cfefd94.PNG)
![t3](https://user-images.githubusercontent.com/51239039/110344716-112dd780-802e-11eb-84a7-7f90a61acc4c.PNG)
![t6](https://user-images.githubusercontent.com/51239039/110344767-20148a00-802e-11eb-92e0-090bc40bebfe.png)
![t7](https://user-images.githubusercontent.com/51239039/110344833-33275a00-802e-11eb-840f-c33f09e5ed76.png)
![t8](https://user-images.githubusercontent.com/51239039/110344835-34588700-802e-11eb-9a66-fb80e9916d46.png)
![t9](https://user-images.githubusercontent.com/51239039/110344838-34f11d80-802e-11eb-97ce-e5c29b91e3b2.png)
![t10](https://user-images.githubusercontent.com/51239039/110344841-3589b400-802e-11eb-9100-67d5ee56e369.png)
![t11](https://user-images.githubusercontent.com/51239039/110344843-36224a80-802e-11eb-8b94-986e83464e63.PNG)


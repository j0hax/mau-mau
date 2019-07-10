# Transfer Protocol
#### In pseudo-code:

| Clients     |               | Server |
| ---------- |:-------------:| -----:|
| Socket(IP,Port)  | &rarr; | ServerSocket.accept() |
| Transmitter.send(new Connection(username))   | &rarr;      | gameIoHandler.receive() |
| Transmitter.receive()  | &larr; | playerXYZ.send(Boolean.TRUE / Boolean.FALSE) |
| | IF players.size() = GAMESIZE: &darr; ELSE: GOTO START | |
| Transmitter.receive()  | &larr; | allPlayers.send(new Game(playerNames, hand)) |
| | GAME LOOP: &darr; | |
| Transmitter.send(data...)  | &rarr; | gameIOHandler.receive(Boolean.TRUE / Boolean.FALSE) |
| Transmitter.receive()  | &larr; | allPlayers.send(new GameState(...)) |
| | Disconnect: in progress  | |

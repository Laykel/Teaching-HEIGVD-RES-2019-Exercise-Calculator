# Client-server calculator protocol
## Transport protocol (What transport protocol do we use?)
TCP.

## Addresses and ports (How does the client find the server (addresses and ports)?)
- Server address : 10.192.105.92 (or 10.192.107.31)
- Server port : 9998

## Who speaks first?
The client first addresses the server.

## Sequence of messages exchanged (What is the sequence of messages exchanged by the client and the server?)
1. (Client - Is the server up?)
2. (Server - Yes, what computation? / No)
3. Client - Addition / Subtraction / Multiplication / Division
4. Server - What values?
5. Client - Here are the two values.
6. Server - Here is the result.
7. Server - Closing connection.

## Responses (What happens when a message is received from the other party?)
The party does an action or not depending on what it just received.

## Messages syntax (What is the syntax of the messages? How we generate and parse them?)
1. (UP?)
2. (YES / NO)
3. {ADD / SUB / MULT / DIV} 4 7
5. 11
6. END

<calculType> <lhs> <rhs>
<calculType> <lhs> <rhs> is <result>

## Who closes the connection and when?
The server closes it after sending the computation's result.

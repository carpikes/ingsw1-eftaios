#!/bin/sh

cd target/classes # this one BEFORE ALL OTHER COMMANDS!

# rmiregistry&
# TODO: usare nanohttpd (in java) invece di python
(python2 -m SimpleHTTPServer)&

sleep 2
java -classpath . -Djava.rmi.server.codebase=file:http://localhost:8000/ it.polimi.ingsw.server.Main


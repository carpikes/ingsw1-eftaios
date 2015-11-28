Escape From the Aliens in Outer Space
=====

Software Engineering 1 Project, a Java remake of this board game:
http://www.escapefromthealiensinouterspace.com/


Build
-----

First of all, install all dependencies:

#### 1. Fetch all dependencies (Ubuntu)

    sudo apt-get install git maven openjdk-7-jdk

#### 2. Clone

    git clone https://github.com/carpikes/ingsw1-eftaios.git

#### 3. Compile and Package

    cd ingsw1-eftaios/ingsw
    ./build_package.sh

Now inside `target/` folder there will be `server.jar` and `client.jar`.

Run
-----

#### Server 
    
    cd target
    java -jar server.jar

#### Client

    cd target
    java -jar client.jar

Screenshots
-----

![GUI Welcome screen](http://i.imgur.com/OfPKfvb.jpg)
![GUI Choose position](http://i.imgur.com/nLTaLXf.jpg)
![CLI Welcome screen](http://i.imgur.com/7W8wopR.jpg)
![CLI Map rendering](http://i.imgur.com/sshP1DK.jpg)
![CLI Choose position](http://i.imgur.com/bmX7mMj.jpg)

License
-----

    Escape From the Aliens In Outer Space is a trademark of Santa Ragione S.r.l.
    
    This software is released under MIT license.
    
    Copyright (C) 2015 Alain Carlucci, Michele Albanese
    
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.

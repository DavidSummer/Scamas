## Motivation

This code aims at illustrating/experimenting design pattern for
behavioural approach of distributed problem solving.

## Use case

We are interested in well-known problems in Computer Science and
Social Theory, e.g. Stable Marriage Problem (\SM). Considering
two communities in which each member has some preferences on the
potential partners, the goal is to make pairs taking into account
their preferences. This abstract problem has many applications. From a
multiagent approach, the seminal Gale-Shapley algorithm solves the SM 
problem by distinguishing two agent behaviors: a community of proposers 
and a community of responders. The negotiations between agents lead to
a stable solution.

## Implementation

The implementation is based on:

- the programming language Scala;

- the open-source toolkit [Akka](http://akka.io/).

## Requirements

In order to run the demonstration you need: the Java virtual machine
[JVM
1.8.0_60](http://www.oracle.com/technetwork/java/javase/downloads/index.html).

In order to compile the code you need:
- the programming language [Scala 2.11.8](http://www.scala-lang.org/download/);
- the interactive build tool [SBT 0.13](http://www.scala-sbt.org/download.html).

## Tests

    sbt run

The programm prints a CSV file nbwWorkers,speedup.

## Installation

    sbt compile

and eventually 

    sbt assembly


## Contributors

Copyright (C) David Summer 2016

## License

This program is free software: you can redistribute it and/or modify it under the terms of the 
GNU General Public License as published by the Free Software Foundation, either version 3 of the License, 
or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  
If not, see <http://www.gnu.org/licenses/>.
## Motivation

The spread of Cloud Computing and multi-core microprocessors requires
a convenient programming paradigm to fully take advantage of such
resources in order to enable the scalability of applications which
needs load balancing to processing a deluge of data. Contrary to
shared-state concurrency (thread or transactional), message-passing
concurrency is suitable for both distribution levels. 

In particular, the Actor model, based on the principle of locality,
consider agents as independent programs with a behavior that send and
receive asynchronous messages [(Hewitt et al., 91)](http://dl.acm.org/citation.cfm?id=1624775.1624804). 
Multiagent systems are an addition abstraction to solve social decision problem such as
resources/tasks allocation or matching. 

The main difficulties in designing such multiagent systems are that:

- agents do not share the same clock, i.e. a resolution can reach
  different results from executions that start at the same internal
  configuration;
  
- agents do not share the same variables, i.e. they do not
   directly synchronize via a common spatial environment but
   indirectly with their own acquaintances through communication
   channels;
   
- agent do not share a global state, i.e. they have only partial
   knowledge of it. The stable properties of multi-agent algorithms,
   as their termination, must be detected.

## Content

This code aims at illustrating/experimenting design pattern for
behavioural approach of distributed problem solving. It includes 
a catalog of reusable modules that reduces the size of the
application code. We focus in particular:

- the modeling of composable behavioral traits defined as finite
  state machine and the consideration of business-level
  acknowledgements within the multiagent protocols (e.g. bargaining,
  auctions);
  
- the termination detection superimposed on the underlying
  multiagent algorithms;

-the observation of the agents and communications channels during
  a decentralized execution of the multiagent systems;

## Use case

In order to test and validate our proposal, our catalog is applied to
two toys problems: a stable assignment problem and a problem of fair
tasks allocation.

We are interested in well-known problems in Computer Science and
Social Theory, e.g. Stable Marriage Problem SM. Considering
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
// Copyright (C) Dialectics 2016
package org.scamas.smp

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import org.scamas.core.{Halt, Start}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Main app to test centralized Gale-Shapley solver
  * */
object Main {
  val debug= true
  val system = ActorSystem("SMPDemonstration")//The Actor system
  val TIMEOUTVALUE=50 seconds// default timeout of a run
  implicit val timeout = Timeout(TIMEOUTVALUE)// TODO make default Duration.Inf

  /**
    * Run the Actor system
    */
  def main(args: Array[String]): Unit ={

    val x1= new Individual("x1",Array("y2","y1","y3"))
    val x2= new Individual("x2",Array("y3","y2","y1"))
    val x3= new Individual("x3",Array("y1","y3","y2"))
    val men= List(x1,x2,x3)
    val y1= new Individual("y1",Array("x2","x1","x3"))
    val y2= new Individual("y2",Array("x3","x2","x1"))
    val y3= new Individual("y3",Array("x1","x3","x2"))
    val women= List(y1,y2,y3)
    // Launch a new system
    val solver= new SMPSolver(men, women)
    solver.run()
    if (debug) println(solver.solution)
    println("That's all folk ! ")
  }
}

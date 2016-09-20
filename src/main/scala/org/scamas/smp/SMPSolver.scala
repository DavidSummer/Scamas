// Copyright (C) Dialectics 2016
package org.scamas.smp

/**
  * mutable Profile of an "agent" manipulated for the SMP
  * @param individual
  */
class Puppet(val individual : Individual){
  var stateOfMind =  new StateOfMind(StateOfMind.PHANTOM,0)
  //Rename the properties
  def myName() = individual.myName
  def preferences() = individual.preferences
  def partner() = stateOfMind.partner
  def concessionLevel() = stateOfMind.concessionLevel
  def free() = stateOfMind.free
  def target()= individual.preferences(this.concessionLevel)
  def isPhantom() = { myName() == StateOfMind.PHANTOM}
}



/**
  * Solver for the stable marriage problem
  *
  * @param men
  * @param women
  */
class SMPSolver(val men: Seq[Individual], val women: Seq[Individual]){
  val debug=true

  var solution : Matching = new Matching(Nil)// The solution to return

  var boys = Seq[Puppet]()
  var girls = Seq[Puppet]()
  init()// should be initialized

  //Initialize the solver
  def init()={
    for (man <- men){
      val boy = new Puppet(man)
      boys:+= boy
    }
    for (woman <- women){
      val girl = new Puppet(woman)
      girls:+= girl
    }
  }

  //Return the puppet corresponding to a name
  def getPuppet(name: String) : Puppet = {
    (boys++girls).find(puppet => puppet.myName == name) match {
      case Some(s) => s
      case None => new Puppet(new Individual(StateOfMind.PHANTOM, Array.empty[String]))
    }
  }


  def run() : Unit ={
    var proposers=boys
    //While there is a proposer who is free and not deseperated
    while (!proposers.isEmpty){
      if (debug) println("There is still a proposer who is free and not deseperated")
      for (proposer <- proposers){
        if (proposer.free){
          val disposer: Puppet = getPuppet(proposer.target)
          if (debug) println(proposer.myName+" proposes to "+disposer.myName)
          val cuckold : Puppet = getPuppet(disposer.partner)
          if (debug) println("The current partner of "+disposer.myName+" is "+cuckold.myName)

          if (disposer.individual.prefers(proposer.myName, cuckold.myName())){
            if (debug) println(proposer.myName+" and "+disposer.myName+" get married")

            proposer.stateOfMind.partner=disposer.myName
            disposer.stateOfMind.partner=proposer.myName

            solution.add(new Marriage(proposer.myName,disposer.myName))
            proposers=proposers.filterNot(p=>p==proposer)
            if (!cuckold.isPhantom){
              if (debug) println(cuckold.myName+" and "+disposer.myName+" get divorced")
              solution=solution.remove(cuckold.myName,disposer.myName)
              if (debug) println(cuckold.myName+" is free")
              proposers:+=cuckold
            }
            //depreciated
            //forall successors of the proposer in the disposer list
            //remove successor from the preference list of the disposer
            //Do not remove the disposer from the preference list of the successor
            //disposer.preferences=removeSuccessors(disposer,proposer.name,disposer.preferences)
          }else{// Go to the next partner
            if (debug) println(disposer.myName+" refuses "+proposer.myName)
            if (debug) println(proposer.myName+" concedes ")
            proposer.stateOfMind.concessionLevel+=1
          }
        }//Go to the next partner
        else proposers=proposers.filterNot(p=>p==proposer)
      }
    }
    if (debug) println("That's all folk !")
  }

  //Remove successors of partner in the preference list of an individual and reciprocally
  def removeSuccessors(ind: Individual, partner: String, preferences: List[String]): List[String] = {
    if (preferences.isEmpty) Nil
    if (preferences.head!= partner) preferences.head :: removeSuccessors(ind,partner,preferences.tail)
    else {
      //for (partner <- preferences.tail) partner.removePartner(ind)
      List(preferences.head)
    }
  }

}

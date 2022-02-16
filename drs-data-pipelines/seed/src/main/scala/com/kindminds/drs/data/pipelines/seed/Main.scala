package com.kindminds.drs.data.pipelines.seed


import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.config.ConfigFactory


object Main  {

  object RootBehavior {
    def apply(): Behavior[Nothing] = Behaviors.setup[Nothing] { context =>
      // Create an actor that handles cluster domain events
      context.spawn(ClusterListener(), "ClusterListener")

      Behaviors.empty
    }
  }

  def main(args: Array[String]): Unit = {

    //println(conf.getConfig("akka.cluster.seed-nodes"))

    if (args.isEmpty) {
      startSeed
    }else{
      startEuSeed

    }

  }

  def startSeed(): Unit ={

   // val system = ActorSystem("drsDP2" ,)


    // Create an Akka system
    ActorSystem[Nothing](RootBehavior(), "drsDP", com.kindminds.drs.data.pipelines.seed.DrsConfig)

  }

  def startEuSeed(): Unit ={
    //ActorSystem("drsDP" ,com.kindminds.drs.data.pipelines.seed.DrsEUonfig)
    ActorSystem[Nothing](RootBehavior(), "drsDP", com.kindminds.drs.data.pipelines.seed.DrsEUonfig)
  }










}

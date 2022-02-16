package com.kindminds.drs.data.pipelines

package object seed {

  val DrsConfig = com.typesafe.config.ConfigFactory.load("application.conf")


  val DrsEUonfig = com.typesafe.config.ConfigFactory.load("application_eu.conf")

}

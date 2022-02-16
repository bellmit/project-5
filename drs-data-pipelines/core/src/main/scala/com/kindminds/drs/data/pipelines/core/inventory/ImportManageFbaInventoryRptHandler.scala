package com.kindminds.drs.data.pipelines.core.inventory

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.kindminds.drs.api.message.command.amazon.report.RequestReport
import com.kindminds.drs.data.pipelines.api.message.{ImportFullManageFBAInventoryRpt, RegisterCommandHandler}
import com.kindminds.drs.data.pipelines.core.DrsDPCmdBus
import com.kindminds.drs.data.pipelines.core.util.HdfsHelper


object ImportManageFbaInventoryRptHandler {

  def props(drsCmdBus: ActorRef): Props =
    Props(new ImportManageFbaInventoryRptHandler(drsCmdBus))

}

class ImportManageFbaInventoryRptHandler(drsCmdBus: ActorRef)  extends Actor with ActorLogging {


  private val name = self.path.name

  drsCmdBus ! RegisterCommandHandler(name,classOf[ImportFullManageFBAInventoryRpt].getName ,self)

  override def receive: Receive = {

    case s: ImportFullManageFBAInventoryRpt =>

      println(s.path)

      val importer = new ManageFbaInventoryRptImporterImpl

      /*
      HdfsHelper.manageFBAInventoryPath.foreach( cc => {
        println("country: " + cc._1)
        println("path: " + cc._2)

        val listStatus = HdfsHelper.listFileStatus(cc._2).sorted.reverse

        println(listStatus(0).getPath)
        println(listStatus(0).getPath.getName)

       */

      val pathAry = s.path.split("/")
      val fileBytes = HdfsHelper.getFile(s.path)

      println(fileBytes.length)
      println(pathAry(pathAry.length-1))
      importer.importFbaInventoryFile(fileBytes,pathAry(pathAry.length-1))

      println("ZZZZZZZZZZZZZZ")

       // println(importer.importFbaInventoryFile(fileBytes, listStatus(0).getPath.getName))


      //})


  }

}
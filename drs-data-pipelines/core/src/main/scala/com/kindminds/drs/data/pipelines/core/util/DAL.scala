package com.kindminds.drs.data.pipelines.core.util

import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.ConnectionFactory


object DAL {


  val hostname = "kindminds01"
  //val hostname = "dev-vm"

  val hconf = HBaseConfiguration.create
  hconf.set("hbase.zookeeper.property.clientport", "2181")
  hconf.set("hbase.zookeeper.quorum",hostname)

  val conn = ConnectionFactory.createConnection(hconf)




}

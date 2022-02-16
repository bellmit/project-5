package com.kindminds.drs.data.pipelines.core.util



import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}

import com.kindminds.drs.data.pipelines.api.util.MD5
import com.kindminds.drs.data.pipelines.core.util.DAL
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put}
import org.apache.hadoop.hbase.util.Bytes


object ImportHelper2 {

  def process(fileName:String , fileModificationTime:String): Unit = {

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))


    val tableName = TableName.valueOf("amazon_report_import_result")
    val table = DAL.conn.getTable(tableName)

    val key = fileName + "#" + fileModificationTime
    val p = new Put(Bytes.toBytes(MD5.hash(key)))

    p.addColumn(Bytes.toBytes("result"),
      Bytes.toBytes("fileName"), Bytes.toBytes(fileName))

    p.addColumn(Bytes.toBytes("result"),
      Bytes.toBytes("fileModificationTime"), Bytes.toBytes(fileModificationTime))

    p.addColumn(Bytes.toBytes("result"),
      Bytes.toBytes("status"), Bytes.toBytes("0"))


    table.put(p)

    table.close()

  }


  def complete(fileName:String , fileModificationTime:String): Unit = {

    val tableName = TableName.valueOf("amazon_report_import_result")
    val table = DAL.conn.getTable(tableName)

    val key = fileName + "#" + fileModificationTime
    val p = new Put(Bytes.toBytes(MD5.hash(key)))

    p.addColumn(Bytes.toBytes("result"),
      Bytes.toBytes("fileName"), Bytes.toBytes(fileName))

    p.addColumn(Bytes.toBytes("result"),
      Bytes.toBytes("fileModificationTime"), Bytes.toBytes(fileModificationTime))

    p.addColumn(Bytes.toBytes("result"),
      Bytes.toBytes("status"), Bytes.toBytes("1"))


    table.put(p)

    table.close()

  }

  def fail(fileName:String , fileModificationTime:String): Unit = {

    val tableName = TableName.valueOf("amazon_report_import_result")
    val table = DAL.conn.getTable(tableName)

    val key = fileName + "#" + fileModificationTime
    val p = new Put(Bytes.toBytes(MD5.hash(key)))

    p.addColumn(Bytes.toBytes("result"),
      Bytes.toBytes("fileName"), Bytes.toBytes(fileName))

    p.addColumn(Bytes.toBytes("result"),
      Bytes.toBytes("fileModificationTime"), Bytes.toBytes(fileModificationTime))

    p.addColumn(Bytes.toBytes("result"),
      Bytes.toBytes("status"), Bytes.toBytes("2"))


    table.put(p)

    table.close()

  }



}


package com.kindminds.drs.data.pipelines.core.util.jooq

import java.sql.{Connection, DriverManager}

object ErpDriver {

  def getConnection():Connection = {
    Class.forName("org.postgresql.Driver")
    DriverManager.getConnection("jdbc:postgresql://drs.crghoygbfshk.ap-northeast-1.rds.amazonaws.com:5432/odoo?assumeMinServerVersion=9.0",
      "odoo", "MiB9lcro3Om")
  }
}

object DrsDriver {

  def getConnection():Connection = {
    Class.forName("org.postgresql.Driver")
    DriverManager.getConnection("jdbc:postgresql://drs.crghoygbfshk.ap-northeast-1.rds.amazonaws.com:5432/drs?assumeMinServerVersion=9.0",
      "drs", "phi42PrlGo6p")
  }


}
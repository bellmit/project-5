package com.kindminds.drs.data.pipelines.core


import com.kindminds.drs.service.util.DrsBizCoreAnnotationConfig
import org.springframework.context.annotation.AnnotationConfigApplicationContext

object BizCoreCtx {

  //todo arthur
  val springCtx: AnnotationConfigApplicationContext =
    new AnnotationConfigApplicationContext(classOf[DrsBizCoreAnnotationConfig])

  def get(): AnnotationConfigApplicationContext = {

    springCtx

  }

}

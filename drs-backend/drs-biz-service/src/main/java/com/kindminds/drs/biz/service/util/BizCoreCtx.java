package com.kindminds.drs.biz.service.util;

import com.kindminds.drs.service.util.DrsBizCoreAnnotationConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BizCoreCtx {



    private static  AnnotationConfigApplicationContext springCtx  =
            new AnnotationConfigApplicationContext(DrsBizCoreAnnotationConfig.class);


    public static AnnotationConfigApplicationContext get() {
        return springCtx;
    }



}
package com.kindminds.drs.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringAppCtx {

    private static ApplicationContext ctx;

    @Autowired
    public SpringAppCtx(ApplicationContext applicationContext) {
        SpringAppCtx.ctx = applicationContext;
    }

    public static ApplicationContext get() {
        return ctx;
    }
}
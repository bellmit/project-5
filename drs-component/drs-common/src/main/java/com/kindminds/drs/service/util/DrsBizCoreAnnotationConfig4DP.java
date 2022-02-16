package com.kindminds.drs.service.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/*
 * Use it to init spring application context without web.xml
 * */

@Configuration
@ImportResource( { "classpath*:META-INF/spring/dp-app-context.xml" } )
public class DrsBizCoreAnnotationConfig4DP {

}

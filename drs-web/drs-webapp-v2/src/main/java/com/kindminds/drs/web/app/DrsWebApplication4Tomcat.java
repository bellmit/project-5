package com.kindminds.drs.web.app;


import akka.actor.ActorSystem;

import javax.servlet.http.HttpSessionEvent;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.kindminds.drs.service.security.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;


//@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class ,
//       MongoDataAutoConfiguration.class , ErrorMvcAutoConfiguration.class})
//@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class ,
//       MongoDataAutoConfiguration.class })
//@ImportResource({"classpath*:META-INF/spring/app-context.xml" , "classpath:app-context.xml"})
//@ComponentScan(
//        basePackages = {"com.kindminds.drs.web.security","com.kindminds.drs.web.config",
//            "com.kindminds.drs.service.security",
//            "com.kindminds.drs.persist",
//            "com.kindminds.drs.service",
//            "com.kindminds.drs.adapter","com.kindminds.drs.web"})
//@SpringBootApplication
public class DrsWebApplication4Tomcat extends SpringBootServletInitializer {


    public static void main(String[] args) {
        //System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");
        SpringApplication.run(DrsWebApplication4Tomcat.class, args);
    }
 
    @Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder application)  {
		return application.sources(DrsWebApplication4Tomcat.class);
	}

    @Autowired
    private UserService securityManager;

    @Bean
    public ServiceProperties serviceProperties()  {
        ServiceProperties serviceProperties = new ServiceProperties();
        //serviceProperties.setService("http://localhost:9000/login/cas");

        //serviceProperties.setService("http://10.0.0.253:8080/login/cas");
        //serviceProperties.setService("http://localhost:8080/login/cas");

        serviceProperties.setService("https://access.drs.network/login/cas");

        //serviceProperties.setService("http://localhost:8080/demo-2/login/cas");
        serviceProperties.setSendRenew(false);

        return serviceProperties;
    }

    @Bean
    @Primary
    public AuthenticationEntryPoint authenticationEntryPoint(ServiceProperties sP)  {

        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();

        entryPoint.setLoginUrl("https://access.drs.network/cas/login");
       // entryPoint.setLoginUrl("http://localhost:8080/cas/login");
        // entryPoint.setLoginUrl("http://localhost:8080/demo-2/cas/login");

        entryPoint.setServiceProperties(sP);
        return entryPoint;
    }


    @Bean
    public TicketValidator ticketValidator()  {
        return new Cas30ServiceTicketValidator(
                //return new Cas20ServiceTicketValidator(
                "https://access.drs.network/cas");
        //		 "http://localhost:8080/cas");
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {

        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(serviceProperties());


        provider.setTicketValidator(ticketValidator());

        provider.setAuthenticationUserDetailsService(this.authenticationUserDetailsService());


        /*
 
	    provider.setUserDetailsService(
	      s -> new User("casuser", "Mellon", true, true, true, true,
	        AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
	    */

        //provider.setKey("CAS_PROVIDER_LOCALHOST_9000");
        provider.setKey("an_id_for_this_auth_provider_only");
	
        return provider;
    }


    @Bean
    public  AuthenticationUserDetailsService<CasAssertionAuthenticationToken>
    authenticationUserDetailsService(){
        return new UserDetailsByNameServiceWrapper(this.securityManager);
    }
    


	/*
    @Bean
    open fun authenticationUserDetailsService(): UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> {

        return UserDetailsByNameServiceWrapper(this.securityManager)
    }*/


    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(
                "https://access.drs.network/cas/logout", securityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl("/logout/cas");
        return logoutFilter;
    }

    @Bean
   public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix("https://access.drs.network/cas");
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    @EventListener
    public  SingleSignOutHttpSessionListener singleSignOutHttpSessionListener(HttpSessionEvent event) {
        return new SingleSignOutHttpSessionListener();
    }
	
	@Bean
	public   FilterRegistrationBean<CharacterEncodingFilter> filterRegistrationBean() {

        FilterRegistrationBean<CharacterEncodingFilter> registrationBean =
                new FilterRegistrationBean<CharacterEncodingFilter>();
        CharacterEncodingFilter characterEncodingFilter = new  CharacterEncodingFilter();
		  characterEncodingFilter.setForceEncoding(false);
		  characterEncodingFilter.setEncoding("UTF-8");
    	registrationBean.setFilter(characterEncodingFilter);
		
		return registrationBean;
	}

    @Bean
    @Qualifier("actorSystem")
    public ActorSystem actorSystem() {

        ActorSystem actorSystem = ActorSystem.create("drs");
        return actorSystem;
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
            connector.setProperty("relaxedPathChars", "[]|{}()\\/:*?\"<>");
            connector.setProperty("relaxedQueryChars", "|{}[]()\\/:*?\"<>");
        });
        return factory;
    }


    /*
    @Bean
public FilterRegistrationBean filterRegistrationBean() {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
    characterEncodingFilter.setForceEncoding(true);
    characterEncodingFilter.setEncoding("UTF-8");
    registrationBean.setFilter(characterEncodingFilter);
    return registrationBean;
}*/




}
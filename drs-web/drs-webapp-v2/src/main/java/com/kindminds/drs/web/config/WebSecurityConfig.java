package com.kindminds.drs.web.config;

import java.util.Arrays;

import javax.servlet.http.HttpSessionEvent;

import com.kindminds.drs.service.security.UserService;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import com.kindminds.drs.web.security.DrsAuthenticationSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DrsAuthenticationSuccessHandler drsAuthenticationSuccessHandler  = null;

    private AuthenticationProvider authenticationProvider ;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private SingleSignOutFilter singleSignOutFilter;
    private LogoutFilter logoutFilter;



    @Autowired
    public WebSecurityConfig(CasAuthenticationProvider casAuthenticationProvider,
                             AuthenticationEntryPoint eP,
                             LogoutFilter lF
            , SingleSignOutFilter ssF
    ) {

        this.authenticationProvider = casAuthenticationProvider;
        this.authenticationEntryPoint = eP;
        this.logoutFilter = lF;
        this.singleSignOutFilter = ssF;

    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {


           /*
		 http
	        .authorizeRequests()
	        .regexMatchers("/secured.*", "/login")
	        .authenticated()
	        .and()
	        .authorizeRequests()
	        .regexMatchers("/")
	        .permitAll()
	        .and()
	        .httpBasic()
	        .authenticationEntryPoint(this.authenticationEntryPoint(this.serviceProperties()));
	      */


        http.authorizeRequests()// authenticationProvider(this.casAuthenticationProvider()).
                // .authenticationEntryPoint(this.authenticationEntryPoint(this.serviceProperties()));
                //  .regexMatchers("/")
                //.antMatchers("/*")
                //.authenticated()
                //.anyRequest().authenticated()
                //.antMatchers("/flow*").permitAll()
                //todo Ralph for postman api
                //.antMatchers("/*").authenticated()
                //hasAnyRole("ADMIN","DRS_STAFF","SUPPLIER_CONSULTANT",
                // 			"DRS_ACCOUNTANT","SUPPLIER","ACER_ACCOUNT_ADMIN","ACER_MARKETING",
                //			"ACER_PRODUCT","ACER_CUSTOMER_CARE","ACER_LOGISTICS","ACER_ACCOUNTING",
                //		"DRS_CUSTOMER_CARE").
                //antMatchers("/*/*").authenticated()
                //antMatchers("/*/*/*").authenticated()
                //.antMatchers("/*/*/*/*").authenticated()
                // 	.antMatchers("/public/**").permitAll()
                //todo arthur kcode
                //.antMatchers("/resources/K2/*").hasAnyRole("ADMIN")
                //	.antMatchers("/*" ).hasAnyRole("ADMIN","DRS_STAFF","SUPPLIER_CONSULTANT",
                //		"DRS_ACCOUNTANT","SUPPLIER","ACER_ACCOUNT_ADMIN","ACER_MARKETING",
                //	"ACER_PRODUCT","ACER_CUSTOMER_CARE","ACER_LOGISTICS","ACER_ACCOUNTING",
                //"DRS_CUSTOMER_CARE")
                //.anyRequest().authenticated()
                //.and()
                //.formLogin().loginPage("/login").defaultSuccessUrl("/loginSuccess")
                //.formLogin()
                .and()
                //.logout().permitAll().logoutSuccessUrl("http://localhost:8080/cas/logout?service=http://localhost:8080/drs-sys-web")
                // .logout().permitAll().logoutSuccessUrl("https://access.drs.network/cas/logout?service=http://localhost:8080/")
                // .logout().permitAll().logoutSuccessUrl("https://access.drs.network/cas/logout?service=http://10.0.0.253:8080/")
                .logout().permitAll().logoutSuccessUrl("https://access.drs.network/cas/logout?service=https://access.drs.network/")
                //.logout().permitAll().logoutSuccessUrl("http://10.0.0.253:8080/cas/logout?service=http://localhost:8080/")
                .and()
                .httpBasic().authenticationEntryPoint(
                this.authenticationEntryPoint)
                .and().csrf().disable().headers().frameOptions().disable();

        //  .and().addFilterBefore(CasAuthenticationFilter.class);


    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(this.authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //@formatter:off
        super.configure(web);
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(this.authenticationProvider));
    }
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Bean(name ="casAuthenticationFilter")
    public CasAuthenticationFilter casAuthenticationFilter(ServiceProperties sP) throws Exception {


        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();

        SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler =
                new SimpleUrlAuthenticationFailureHandler();
        simpleUrlAuthenticationFailureHandler.setDefaultFailureUrl("/casfailed.jsp");

        casAuthenticationFilter.setServiceProperties(sP);

        casAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
        drsAuthenticationSuccessHandler.setDefaultTargetUrl("/");


        casAuthenticationFilter.setAuthenticationSuccessHandler(drsAuthenticationSuccessHandler);
        casAuthenticationFilter.setAuthenticationFailureHandler(simpleUrlAuthenticationFailureHandler);

        return casAuthenticationFilter;
    }





    /*

	private AuthenticationProvider authenticationProvider;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private SingleSignOutFilter singleSignOutFilter;
    private LogoutFilter logoutFilter;

    @Autowired
    public WebSecurityConfig2(CasAuthenticationProvider casAuthenticationProvider, AuthenticationEntryPoint eP,
                          LogoutFilter lF
                          , SingleSignOutFilter ssF
    ) {
        this.authenticationProvider = casAuthenticationProvider;
        this.authenticationEntryPoint = eP;

        this.logoutFilter = lF;
        this.singleSignOutFilter = ssF;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
      return new ProviderManager(Arrays.asList(authenticationProvider));
    }

    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(ServiceProperties sP) throws Exception {
      CasAuthenticationFilter filter = new CasAuthenticationFilter();
      filter.setServiceProperties(sP);
      filter.setAuthenticationManager(authenticationManager());
      return filter;
    }
    */


    /*
    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(
          "https://localhost:6443/cas/logout",
          securityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl("/logout/cas");
        return logoutFilter;
    }

    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix("https://localhost:6443/cas");
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    @EventListener
    public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener(
      HttpSessionEvent event) {
        return new SingleSignOutHttpSessionListener();
    }
    */


}
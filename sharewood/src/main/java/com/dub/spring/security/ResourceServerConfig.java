package com.dub.spring.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;


/**
* ResourceServerConfiguration used by Spring OAuth2 
* */

@Configuration
@EnableResourceServer
public class ResourceServerConfig
								extends ResourceServerConfigurerAdapter {

	@Value("${security.oauth2.resource.id}")
	private String resourceId;
	
	@Value("${security.oauth2.client.client-id}")
	private String clientId;

	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	@Value("${check-token-endpoint-url}")
	private String checkTokenEndpointUrl;
	
	//@Autowired// provided 
	//private DataSource dataSource;


	@Override
	public void configure(HttpSecurity http) 
								throws Exception {		
		
	    http.httpBasic().disable();
	
		http.antMatcher("/api/**").authorizeRequests()	
		.antMatchers(HttpMethod.GET, "/api/testResource")
		.access("#oauth2.hasScope('READ')")
			
		.antMatchers(HttpMethod.GET, "/api/photos/**")
		.access("#oauth2.hasScope('READ') and hasAuthority('USER')")
		
		.antMatchers(HttpMethod.POST, "/api/photos/**")
		.access("#oauth2.hasScope('READ') and #oauth2.hasScope('WRITE') and hasAuthority('USER')")
		
		.antMatchers(HttpMethod.PUT, "/api/photos/**")
		.access("#oauth2.hasScope('READ') and #oauth2.hasScope('WRITE') and hasAuthority('USER')")
		
		.antMatchers(HttpMethod.DELETE, "/api/photos/**")
		.access("#oauth2.hasScope('READ') and #oauth2.hasScope('DELETE') and hasAuthority('USER')");
				
	}



	@Primary
    @Bean
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(checkTokenEndpointUrl);
        		//"http://localhost:8080/authorization/oauth/check_token");
                //http://localhost:8080/authorization/oauth/decodeToken");
   
        tokenServices.setClientId(clientId);
        
     
        tokenServices.setClientSecret(clientSecret);
        
        return tokenServices;
    }
    
	
	
	
	
	@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
				
		resources.resourceId(resourceId);
		resources.accessDeniedHandler(new OAuth2AccessDeniedHandler());
		resources.expressionHandler(new OAuth2WebSecurityExpressionHandler());
		
		resources.tokenServices(tokenServices());
	
	}
	
	/*
	@Bean
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();	
		tokenServices.setTokenStore(new JdbcTokenStore(dataSource));
			
		return tokenServices;
	} 
	*/ 
	
}
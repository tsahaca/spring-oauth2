package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.context.annotation.Bean;
import javax.annotation.PostConstruct;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableResourceServer
public class ResourceServerApp {
	
	@Autowired
	private Environment environment;

    public static void main (String[] args) {
        ApplicationContext ctx = SpringApplication.run(ResourceServerApp.class,
                args);
        ctx.getApplicationName();
    }
  
    @PostConstruct
	public void initClientCert(){
    	String keyStorefile=environment.getProperty("client.cert.key-store");
        int indexOf = keyStorefile.indexOf(":");			
    	System.setProperty("javax.net.ssl.keyStore", keyStorefile.substring(indexOf+1));
		System.setProperty("javax.net.ssl.keyStorePassword", environment.getProperty("client.cert.key-store-password"));
		System.setProperty("javax.net.ssl.keyStoreType", environment.getProperty("client.cert.keyStoreType"));
	}
    
    @Bean
    @Primary
    public ResourceServerTokenServices tokenService() {
    	MyRemoteTokenServices tokenServices = new MyRemoteTokenServices();
        tokenServices.setClientId(environment.getProperty("security.oauth2.client.clientId"));
        tokenServices.setClientSecret(environment.getProperty("security.oauth2.client.clientSecret"));
        tokenServices.setCheckTokenEndpointUrl(environment.getProperty("security.oauth2.resource.tokenInfoUri"));
        return tokenServices;
    }

}

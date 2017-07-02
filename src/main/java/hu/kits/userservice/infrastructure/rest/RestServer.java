package hu.kits.userservice.infrastructure.rest;

import java.lang.invoke.MethodHandles;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.kits.userservice.domain.UserService;

public class RestServer {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private final Server server;
    
    public RestServer(UserService userService, int httpPort){
        
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(httpPort).build();
        ResourceConfig config = new ResourceConfig();
        config.register(new UserServiceRestEndpoint(userService));
        server = JettyHttpContainerFactory.createServer(baseUri, config, false);
        
        logger.info("CustomerWebServiceServer initialized on port " + httpPort);
    }
    
    public void start() throws Exception {
        server.start();
        logger.info("CustomerWebServiceServer started");
    }
    
}

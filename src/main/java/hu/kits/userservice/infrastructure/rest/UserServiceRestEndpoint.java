package hu.kits.userservice.infrastructure.rest;

import java.lang.invoke.MethodHandles;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.kits.userservice.domain.AuthenticationException;
import hu.kits.userservice.domain.User;
import hu.kits.userservice.domain.UserService;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserServiceRestEndpoint {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;

    public UserServiceRestEndpoint(UserService userService) {
        this.userService = userService;
    }
    
    @Context
    private UriInfo uriInfo;
    
    @Context
    private HttpServletRequest httpRequest;
    
    @POST
    @Path("{domain}/{userId}/authenticate")
    public JsonObject authenticateUser(
            @PathParam("domain") String domain,
            @PathParam("userId") String userId, 
            JsonObject jsonObject) {
        logger.info("Authentication request: {}", uriInfo.getRequestUri());
        try {
            String password = jsonObject.getString("password");
            User user = userService.authenticateUser(domain, userId, password);
            return mapToJson(user);
        } catch (AuthenticationException ex) {
            throw new WebApplicationException(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(mapToJson(ex.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build());
        }
    }
    
    @POST
    @Path("{domain}/{userId}/change-password")
    public void changePassword(
            @PathParam("domain") String domain,
            @PathParam("userId") String userId, 
            JsonObject jsonObject) {
        logger.info("Password change request: {}", uriInfo.getRequestUri());
        try {
            String oldPassword = jsonObject.getString("oldPassword");
            String newPassword = jsonObject.getString("newPassword");
            userService.changePassword(domain, userId, oldPassword, newPassword);
        } catch (AuthenticationException ex) {
            throw new WebApplicationException(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(mapToJson(ex.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build());
        }
    }
    
    private static JsonObject mapToJson(User user) {
        return Json.createObjectBuilder()
                .add("name", user.name)
                .add("role", user.role)
                .add("email", user.email)
                .add("phone", user.phone)
            .build();
    }
    
    private static JsonObject mapToJson(String errorMessage) {
        return Json.createObjectBuilder()
                .add("errorMessage", errorMessage)
            .build();
    }
}

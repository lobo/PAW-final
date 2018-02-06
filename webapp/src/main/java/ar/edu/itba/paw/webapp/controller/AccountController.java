package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ClanService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.clan.Clan;
import ar.edu.itba.paw.webapp.DTO.clans.ClanUsersDTO;
import ar.edu.itba.paw.webapp.DTO.users.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Random;

@Path("accounts")
@Component
public class AccountController {

    @Autowired
    private UserService us;
    @Autowired
    private ClanService cs;
    @Context
    private UriInfo uriInfo;

    private static class LoginQuery {
        public String username;
        public String password;
    }

    @POST
    @Path("/create")
    @Consumes(value = {MediaType.APPLICATION_JSON,})
    public Response createUser(LoginQuery query) {
        if (query == null || query.username == null || query.password == null) return Response.status(Response.Status.BAD_REQUEST).build();

        if(query.password.length() < 4 || query.password.length() > 30) return Response.status(Response.Status.BAD_REQUEST).entity("Password must be between 4 and 30 characters long").build();

        if(us.findByUsername(query.username) != null) return Response.status(Response.Status.CONFLICT).entity("User already exists").build();

        int imageID = Math.abs(new Random().nextInt() % 11);
        User newUser = us.create(query.username, query.password, imageID + ".jpg");

        if (newUser == null) return Response.status(Response.Status.CONFLICT).build();
        return Response.created(uriInfo.getBaseUri().resolve(String.format(UserDTO.url, newUser.getId()))).build();
    }

    @GET
    @Path("/user")
    @Consumes(value = {MediaType.APPLICATION_JSON,})
    public Response getAccountUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = us.findByUsername(username);
        if(user == null) return Response.status(Response.Status.UNAUTHORIZED).build();

        return Response.ok(new UserDTO(user, uriInfo.getBaseUri())).build();
    }

}


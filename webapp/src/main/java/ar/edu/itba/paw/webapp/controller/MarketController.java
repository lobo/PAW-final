package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.ResourceType;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.DTO.market.MarketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;

@Path("market")
@Component
public class MarketController {
    @Autowired
    private UserService us;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/prices")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listPrices() {
        return Response.ok(new MarketDTO()).build();

    }

    private static class MarketQuery {
        @XmlElement(name = "resource_type")
        public Integer resourceType;
        public Double amount;
    }

    //Money can't be purchased
    @POST
    @Path("/purchase")
    @Consumes(value = {MediaType.APPLICATION_JSON,})
    public Response purchaseResource(MarketQuery query) {
        if(query == null
        || query.resourceType == null
        || query.amount == null
        || Math.abs(query.amount - Math.floor(query.amount)) > 0.0001D) return Response.status(Response.Status.BAD_REQUEST).build();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = us.findByUsername(username);
        if(user == null) return Response.status(Response.Status.UNAUTHORIZED).build();

        query.amount = Math.floor(query.amount);
        if(query.amount<0 || ResourceType.fromId(query.resourceType) == null) return Response.status(Response.Status.BAD_REQUEST).build();

        if (us.purchaseResourceType(user.getId(),ResourceType.fromId(query.resourceType),query.amount)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    //Money can't be bought
    @POST
    @Path("/sell")
    @Consumes(value = {MediaType.APPLICATION_JSON,})
    public Response sellResource(MarketQuery query) {
        if(query == null || query.resourceType == null || query.amount == null) return Response.status(Response.Status.BAD_REQUEST).build();
        Double amount = query.amount;
        Integer resourceType = query.resourceType;

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = us.findByUsername(username);
        if(user == null) return Response.status(Response.Status.UNAUTHORIZED).build();

        if(amount<0 ||  ResourceType.fromId(resourceType) == null) return Response.status(Response.Status.BAD_REQUEST).build();

        if (us.sellResourceType(user.getId(),ResourceType.fromId(resourceType),amount)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
}
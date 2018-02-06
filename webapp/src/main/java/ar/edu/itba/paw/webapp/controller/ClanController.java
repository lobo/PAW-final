package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ClanService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.clan.Clan;
import ar.edu.itba.paw.model.clan.ClanBattle;
import ar.edu.itba.paw.model.packages.Paginating;
import ar.edu.itba.paw.webapp.DTO.PaginantingDTO;
import ar.edu.itba.paw.webapp.DTO.clans.ClanBattleDTO;
import ar.edu.itba.paw.webapp.DTO.clans.ClanDTO;
import ar.edu.itba.paw.webapp.DTO.clans.ClanRankDTO;
import ar.edu.itba.paw.webapp.DTO.clans.ClanUsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by juanfra on 18/06/17.
 */
@Path("clans")
@Component
public class ClanController {

    @Autowired
    private UserService us;
    @Autowired
    private ClanService cs;
    @Context
    private UriInfo uriInfo;


    @GET
    @Path("/all")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listClans(
            @QueryParam("page") @DefaultValue("1") final Integer page,
            @QueryParam("pageSize") @DefaultValue("20") final Integer pageSize) {

        if(page<=0 || pageSize<=0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final Paginating<Clan> allClans = cs.globalClans(page, pageSize);
        if(allClans!=null) {
            return Response.ok(
                    new PaginantingDTO<>(allClans, (Clan c)-> new ClanDTO(c, cs.getClanBattle(c.getId()), uriInfo.getBaseUri()),
                            uriInfo.getBaseUri().resolve(String.format(ClanDTO.url, "all/")))
            ).build();
        }else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response getById(@PathParam("id") final int id) {
        final Clan clan = cs.getClanById(id);
        if (clan != null) {
            return Response.ok(new ClanDTO(clan, cs.getClanBattle(clan.getId()), uriInfo.getBaseUri())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/users")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response getClanById(@PathParam("id") final int id) {
        final Clan clan = cs.getClanById(id);
        if (clan != null) {
            return Response.ok(new ClanUsersDTO(clan, uriInfo.getBaseUri())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/battle")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response getClanBattleById(@PathParam("id") final int id) {
        final Clan clan = cs.getClanById(id);
        if (clan != null) {
            ClanBattle battle = cs.getClanBattle(id);
            if(battle!=null) {
                ClanBattle opponent = null;
                if(battle.getVersus() != null) {
                    opponent = cs.getClanBattle(battle.getVersus().getId());
                }
                return Response.ok(new ClanBattleDTO(battle,opponent,uriInfo.getBaseUri())).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/rank")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response getClanRankById(@PathParam("id") final int id) {
        final Integer rank = cs.getGlobalRanking(id);
        if (rank != null) {
            return Response.ok(new ClanRankDTO(id, rank, uriInfo.getBaseUri())).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private static class JoinClanQuery {
        public Integer id;
    }

    @POST
    @Path("/join")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response joinClan(JoinClanQuery query) {

        if(query==null || query.id == null) return Response.status(Response.Status.BAD_REQUEST).build();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = us.findByUsername(username);
        if(user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        if(user.getClanId()!=null) return Response.status(Response.Status.CONFLICT).entity("User is already part of a clan.").build();

        final Clan clan = cs.addUserToClan(query.id,user.getId());
        if (clan != null) {
            return Response.ok(new ClanUsersDTO(clan, uriInfo.getBaseUri())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    static private class CreateClanQuery {
        public String name;
    }

    @POST
    @Path("/create")
    @Consumes(value = {MediaType.APPLICATION_JSON,})
    public Response createClan(CreateClanQuery query) {
        if(query ==null || query.name==null) return Response.status(Response.Status.BAD_REQUEST).build();
        String name = query.name;

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = us.findByUsername(username);
        if(user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        if(user.getClanId()!=null) return Response.status(Response.Status.CONFLICT).entity("User is already part of a clan.").build();
        if(cs.getClanByName(name)!=null) return Response.status(Response.Status.CONFLICT).entity("Clan with name " + name + " already exists").build();

        final Clan clan = cs.createClan(name);
        if (clan != null) {
            cs.addUserToClan(clan.getId(), user.getId());
            return Response.created(uriInfo.getBaseUri().resolve(String.format(ClanDTO.url, clan.getId()))).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/leave")
    public Response leaveClan() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = us.findByUsername(username);
        if(user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
        if(user.getClanId() == null) return Response.status(Response.Status.CONFLICT).entity("User is no part of a clan.").build();

        if (cs.deleteFromClan(user.getId())) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}

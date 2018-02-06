package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ClanService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.DTO.clans.ClanDTO;
import ar.edu.itba.paw.webapp.DTO.search.SearchDTO;
import ar.edu.itba.paw.webapp.DTO.search.SearchResultDTO;
import ar.edu.itba.paw.webapp.DTO.users.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.edu.itba.paw.webapp.DTO.search.SearchResultType.CLAN;
import static ar.edu.itba.paw.webapp.DTO.search.SearchResultType.USER;

@Path("search")
@Component
public class SearchController {

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService us;
    @Qualifier("clanServiceImpl")
    @Autowired
    private ClanService cs;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    @Path("/")
    public Response searchQuery(@QueryParam("query") final String query) {
        if (query == null || query.equals("")) return Response.status(Response.Status.BAD_REQUEST).build();

        Collection<User> users = us.findByKeyword(query);
        Collection<String> clans = cs.findByKeyword(query);

        Collection<SearchResultDTO> results = Stream.concat(
                users.stream().map((u) -> new SearchResultDTO(
                        USER,
                        uriInfo.getBaseUri().resolve(String.format(UserDTO.url, u.getId())),
                        u.getUsername(),
                        u.getId())),
                clans.stream().map(cs::getClanByName)
                        .map((c) -> new SearchResultDTO(
                        CLAN,
                        uriInfo.getBaseUri().resolve(String.format(ClanDTO.url, c.getId())),
                        c.getName(),
                        (long) c.getId())
                        )
        ).collect(Collectors.toList());

        return Response.ok(new SearchDTO(results)).build();
    }
}

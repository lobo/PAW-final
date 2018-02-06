package ar.edu.itba.paw.webapp.DTO;

import ar.edu.itba.paw.model.packages.Paginating;
import ar.edu.itba.paw.webapp.DTO.clans.ClanDTO;
import ar.edu.itba.paw.webapp.DTO.users.UserDTO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by juanfra on 22/08/17.
 */
@XmlRootElement
@XmlSeeAlso({UserDTO.class, ClanDTO.class})
public class PaginantingDTO<Item,DTOItem> {

    @XmlElement(name = "page")
    private int page;

    @XmlElement(name = "items_per_page")
    private int itemsPerPage;

    @XmlElement(name = "total_pages")
    private int totalPages;

    @XmlElement(name = "total_items")
    private int totalItems;

    @XmlElement(name = "elements")
    private List<DTOItem> items;

    @XmlElement(name = "next")
    private URI nextPage;

    @XmlElement(name = "previous")
    private URI previousPage;

    public PaginantingDTO() {}
    public PaginantingDTO(Paginating<Item> pages, DTOtransformer<Item,DTOItem> transformer, URI baseUri) {
        page = pages.getPage();
        itemsPerPage = pages.getItemsPerPage();
        totalPages = pages.getTotalPages();
        totalItems = pages.getTotalItems();
        items = pages.getItems().stream().map(transformer::transform).collect(Collectors.toList());
        int newPage  = pages.getPage();
        int totalPages  = pages.getTotalPages();
        if(newPage>1) {
            previousPage = baseUri.resolve(
                    "?page=" + (newPage-1) +
                    "&pageSize=" + pages.getItemsPerPage());
        }

        if(newPage<totalPages) {
            nextPage = baseUri.resolve(
                    "?page=" + (newPage+1) +
                            "&pageSize=" + pages.getItemsPerPage());
        }
    }



}

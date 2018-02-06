package ar.edu.itba.paw.model.packages;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by juanfra on 23/05/17.
 */
public class Paginating<Item> {

    private int page;
    private int itemsPerPage;
    private int totalPages;
    private int totalItems;

    private List<Item> items;

    public Paginating(int page, int itemsPerPage, int totalItems, int totalPages, @NotNull List<Item> items) {
        this.page = page;
        this.itemsPerPage = itemsPerPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.items = Collections.unmodifiableList(items);
    }

    public void sort(Comparator<Item> comp){
        items = Collections.unmodifiableList(items.stream().sorted(comp).collect(Collectors.toList()));
    }

    public int getPage() {
        return page;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public List<Item> getItems() {
        return items;
    }
}

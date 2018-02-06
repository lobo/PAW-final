package ar.edu.itba.paw.webapp.DTO.search;

public enum SearchResultType {
    CLAN("clan"),
    USER("user");

    String name;

    SearchResultType(String name) {
        this.name = name;
    }
}

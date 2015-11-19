package ru.spbau.mit.antonpp.deepshot.network.model;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class StyleItem {
    private String name;
    private long id;
    private String uri;

    public StyleItem(String name, long id, String uri) {
        this.name = name;
        this.id = id;
        this.uri = uri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

package ru.spbau.mit.antonpp.deepshot.network;

/**
 * @author antonpp
 * @since 27/10/15
 */
public class FilterItem {
    private String name;
    private String uri;

    public FilterItem(String name, String uri) {
        this.name = name;
        this.uri = uri;
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

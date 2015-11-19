package ru.spbau.mit.antonpp.deepshot.network.model;

/**
 * @author antonpp
 * @since 13/11/15
 */
public class ResultItem {
    private String name;
    private String uri;

    public ResultItem(String name, String uri) {
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
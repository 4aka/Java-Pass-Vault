package be.sec;

import java.io.Serializable;

public class Entry implements Serializable {

    private String login;
    private String password;
    private String url;

    public Entry(String login, String password, String url) {
        this.login = login;
        this.password = password;
        this.url = url;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}

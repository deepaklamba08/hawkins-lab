package org.stranger.common.model.user;

public class User {

    private final String name;
    private final String loginId;

    private final Credential credential;

    public User(String name, String loginId, Credential credential) {
        this.name = name;
        this.loginId = loginId;
        this.credential = credential;
    }

    public String getName() {
        return name;
    }

    public String getLoginId() {
        return loginId;
    }

    public Credential getCredential() {
        return credential;
    }
}

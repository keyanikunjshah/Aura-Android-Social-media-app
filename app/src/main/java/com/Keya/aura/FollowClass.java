package com.Keya.aura;

public class FollowClass {

    private String username;
    private String ProfileImageUrl;
    private String email;
    private String id;
    private String followers;
    private String following;
    private String password;
    private boolean isFirstLogin;

    public FollowClass() {

    }


    public FollowClass(String username, String ProfileImageUrl) {
        this.username = username;
        this.ProfileImageUrl = ProfileImageUrl;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUrl() {
        return ProfileImageUrl;
    }

    public void ProfileImageUrl(String ProfileImageUrl) {
        this.ProfileImageUrl = ProfileImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }


}

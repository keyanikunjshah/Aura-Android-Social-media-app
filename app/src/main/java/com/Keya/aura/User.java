package com.Keya.aura;

public class User {
    private String id;
    private String username;
    private String ProfileImageUrl;
    private String postImageUrl;


    public User() {}

    public User(String id, String username, String ProfileImageUrl,String postImageUrl) {
        this.id = id;
        this.username = username;
        this.ProfileImageUrl = ProfileImageUrl;

        this.postImageUrl=postImageUrl;

    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getProfileImageUrl() { return ProfileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.ProfileImageUrl = profileImageUrl; }

    public String getPostImageUrl() {
        return postImageUrl;
    }
    public void setPostImageUrl(String postImageUrl) { this.postImageUrl = postImageUrl; }


}

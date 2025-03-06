package com.example.triolingo_mobile.Model;

public class UserEntity {
    private int id;
    private String fullNamel;
    private String email;
    private String password;
    private String avatarUrl;
    private int status;
    private String note;

    public UserEntity() {
    }

    public UserEntity(int id, String fullNamel, String email, String password, String avatarUrl, int status, String note) {
        this.id = id;
        this.fullNamel = fullNamel;
        this.email = email;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.status = status;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullNamel() {
        return fullNamel;
    }

    public void setFullNamel(String fullNamel) {
        this.fullNamel = fullNamel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", fullNamel='" + fullNamel + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", status=" + status +
                ", note='" + note + '\'' +
                '}';
    }
}

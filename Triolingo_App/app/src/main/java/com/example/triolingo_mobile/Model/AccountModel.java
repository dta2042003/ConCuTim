package com.example.triolingo_mobile.Model;

public class AccountModel {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String avatarUrl;
    private int status;
    private String note;

    public AccountModel() {
    }

    public AccountModel(int id, String fullName, String email, String password, String avatarUrl, int status, String note) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.status = status;
        this.note = note;
    }

    public AccountModel(String fullName, String email, String password, String avatarUrl, int status, String note) {
        this.fullName = fullName;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        return "AccountModel{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", status=" + status +
                ", note='" + note + '\'' +
                '}';
    }
}

package com.example.triolingo_mobile.Model;

public class UserNote {
    private boolean intro;
    private String language;

    public UserNote() {
    }

    public UserNote(boolean intro, String language) {
        this.intro = intro;
        this.language = language;
    }

    public boolean isIntro() {
        return intro;
    }

    public void setIntro(boolean intro) {
        this.intro = intro;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

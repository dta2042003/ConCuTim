package com.example.con_cu_tim.Model;

public class UserNote {
    private String language;
    private String level;
    private Boolean intro;

    public UserNote() {}

    public UserNote(String language, String level, Boolean intro) {
        this.language = language;
        this.level = level;
        this.intro = intro;
    }

    // Getter & Setter
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Boolean getIntro() {
        return intro;
    }

    public void setIntro(Boolean intro) {
        this.intro = intro;
    }
}


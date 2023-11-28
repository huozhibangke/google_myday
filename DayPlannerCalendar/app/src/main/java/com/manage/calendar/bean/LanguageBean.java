package com.manage.calendar.bean;

public class LanguageBean {

    private int languageType;
    private String languageName;


    public LanguageBean(int languageType, String languageName) {
        this.languageType = languageType;
        this.languageName = languageName;
    }

    public int getLanguageType() {
        return languageType;
    }

    public void setLanguageType(int languageType) {
        this.languageType = languageType;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
}

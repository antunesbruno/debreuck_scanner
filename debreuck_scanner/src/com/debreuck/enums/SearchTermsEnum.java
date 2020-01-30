package com.debreuck.enums;

public enum SearchTermsEnum {

    START_RENDERING("Executing request startRendering with arguments"),
    UNIQUE_ID("Service startRendering returned"),
    GET_RENDERING("getRendering");

    private String term;

    SearchTermsEnum(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }
}



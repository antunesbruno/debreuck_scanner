package com.debreuck.models;

import java.util.ArrayList;

public class ReportRenderingModel {

    private Integer document;
    private Integer page;
    private String uid;
    private ArrayList<String> TStampsStartRendering;
    private ArrayList<String> TStampsGetRendering;

    public Integer getDocument() {
        return document;
    }

    public void setDocument(Integer document) {
        this.document = document;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<String> getTStampsStartRendering() {
        return TStampsStartRendering;
    }

    public void setTStampsStartRendering(ArrayList<String> TStampsStartRendering) {
        this.TStampsStartRendering = TStampsStartRendering;
    }

    public ArrayList<String> getTStampsGetRendering() {
        return TStampsGetRendering;
    }

    public void setTStampsGetRendering(ArrayList<String> TStampsGetRendering) {
        this.TStampsGetRendering = TStampsGetRendering;
    }
}

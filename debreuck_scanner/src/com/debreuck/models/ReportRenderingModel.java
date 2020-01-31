package com.debreuck.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReportRenderingModel {

    private String Thread;
    private Integer DocumentId;
    private Integer Page;
    private String UUID;
    private ArrayList<LocalDateTime> TStampsStartRendering;
    private ArrayList<LocalDateTime> TStampsGetRendering;

    public Integer getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(Integer documentId) {
        DocumentId = documentId;
    }

    public Integer getPage() {
        return Page;
    }

    public void setPage(Integer page) {
        Page = page;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public ArrayList<LocalDateTime> getTStampsStartRendering() {
        return TStampsStartRendering;
    }

    public void setTStampsStartRendering(ArrayList<LocalDateTime> TStampsStartRendering) {
        this.TStampsStartRendering = TStampsStartRendering;
    }

    public ArrayList<LocalDateTime> getTStampsGetRendering() {
        return TStampsGetRendering;
    }

    public void setTStampsGetRendering(ArrayList<LocalDateTime> TStampsGetRendering) {
        this.TStampsGetRendering = TStampsGetRendering;
    }

    public String getThread() {
        return Thread;
    }

    public void setThread(String thread) {
        Thread = thread;
    }
}

package com.debreuck.models;

public class ReportSummaryModel {

    private Integer count;
    private Integer duplicates;
    private Integer unnecessary;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getDuplicates() {
        return duplicates;
    }

    public void setDuplicates(Integer duplicates) {
        this.duplicates = duplicates;
    }

    public Integer getUnnecessary() {
        return unnecessary;
    }

    public void setUnnecessary(Integer unnecessary) {
        this.unnecessary = unnecessary;
    }
}

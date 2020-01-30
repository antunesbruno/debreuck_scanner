package com.debreuck.models;

public class ReportSummaryModel {

    private Integer NumberOfRenderings;
    private Integer NumberOfDoubleRenderings;
    private Integer NumberRenderingsWithoutGet;

    public Integer getNumberOfRenderings() {
        return NumberOfRenderings;
    }

    public void setNumberOfRenderings(Integer numberOfRenderings) {
        NumberOfRenderings = numberOfRenderings;
    }

    public Integer getNumberOfDoubleRenderings() {
        return NumberOfDoubleRenderings;
    }

    public void setNumberOfDoubleRenderings(Integer numberOfDoubleRenderings) {
        NumberOfDoubleRenderings = numberOfDoubleRenderings;
    }

    public Integer getNumberRenderingsWithoutGet() {
        return NumberRenderingsWithoutGet;
    }

    public void setNumberRenderingsWithoutGet(Integer numberRenderingsWithoutGet) {
        NumberRenderingsWithoutGet = numberRenderingsWithoutGet;
    }
}

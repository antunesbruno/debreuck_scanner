package com.debreuck.models;

import java.util.ArrayList;

public class ReportModel {
    private ArrayList<ReportRenderingModel> ReportingRenderingList;
    private ReportSummaryModel Summary;

    public ArrayList<ReportRenderingModel> getReportingRenderingList() {
        return ReportingRenderingList;
    }

    public void setReportingRenderingList(ArrayList<ReportRenderingModel> reportingRenderingList) {
        ReportingRenderingList = reportingRenderingList;
    }

    public ReportSummaryModel getSummary() {
        return Summary;
    }

    public void setSummary(ReportSummaryModel summary) {
        Summary = summary;
    }
}

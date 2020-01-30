package com.debreuck.analysis;

import com.debreuck.enums.SearchTermsEnum;
import com.debreuck.models.ReportModel;
import com.debreuck.models.ReportRenderingModel;
import com.debreuck.models.ReportSummaryModel;
import com.debreuck.utils.Constants;
import com.debreuck.utils.RegularExpression;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LogAnalysis {

    private RegularExpression regex;
    private ReportModel ReportModel;

    public com.debreuck.models.ReportModel getReportModel() {
        return ReportModel;
    }

    public void setReportModel(com.debreuck.models.ReportModel reportModel) {
        ReportModel = reportModel;
    }

    public LogAnalysis()
    {
        //initialize fields
        ReportModel = new ReportModel();
        ReportModel.setSummary(new ReportSummaryModel());
        ReportModel.setReportingRenderingList(new ArrayList<ReportRenderingModel>());
    }

    public void IterateLog(BufferedReader br) {
        try {

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                ConvertLineToModel(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ConvertLineToModel(String line)
    {
        //instance model
        ReportRenderingModel renderingModel = new ReportRenderingModel();

        //convert line using regular expression
        regex = new RegularExpression();
        ArrayList<String> regexGroups = regex.Convert(line, Constants.LOG_EXPRESSION);

        //if expression convert right then load model
        if(regexGroups.size() == 5)
        {
            //fill the model
            renderingModel = ConvertToRenderingModel(regexGroups);

            //if model has id then add
            if(renderingModel.getDocumentId() != null)
                ReportModel.getReportingRenderingList().add(renderingModel);
        }
    }

    private ReportRenderingModel ConvertToRenderingModel(ArrayList<String> groups) {

        //instance model
        ReportRenderingModel renderingModel = new ReportRenderingModel();

        //instance fields
        renderingModel.setTStampsGetRendering(new ArrayList<LocalDateTime>());
        renderingModel.setTStampsStartRendering(new ArrayList<LocalDateTime>());

        //convert documentId
        ConvertDocumentId(renderingModel, groups.get(4));

        return renderingModel;

    }

    private ReportRenderingModel ConvertDocumentId(ReportRenderingModel model, String lineBlock) {

        //if has the term
        if (lineBlock.contains(SearchTermsEnum.START_RENDERING.getTerm())) {
            ArrayList<String> groups = regex.Convert(lineBlock, "\\[([^\\]]*)\\]");

            if (groups.size() > 0) {

                String[] result = groups.get(0).split(",");
                model.setDocumentId(Integer.parseInt(result[0].trim()));
                model.setPage(Integer.parseInt(result[1].trim()));

                System.out.println(groups.get(0));
            }
        }

        return model;
    }

}

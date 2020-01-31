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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class LogAnalysis {

    private RegularExpression regex;
    private ReportModel ReportModel;
    private Integer _renderingModelIndexOf = 0;
    private ReportRenderingModel _renderingModel;

    public com.debreuck.models.ReportModel getReportModel() {
        return ReportModel;
    }

    public void setReportModel(com.debreuck.models.ReportModel reportModel) {
        ReportModel = reportModel;
    }

    /**
     * Constructor
     */
    public LogAnalysis() {
        //initialize fields
        ReportModel = new ReportModel();
        ReportModel.setSummary(new ReportSummaryModel());
        ReportModel.setReportingRenderingList(new ArrayList<ReportRenderingModel>());
    }

    /**
     * Iterate the log file
     */
    public void IterateLog(BufferedReader br) {
        try {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                ConvertLineToModel(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert the line just read by default log regex
     */
    public void ConvertLineToModel(String line) {
        //convert line using regular expression
        regex = new RegularExpression();
        ArrayList<String> regexGroups = regex.Convert(line, Constants.LOG_EXPRESSION);

        //if expression convert right then load model
        if (regexGroups.size() == 5) {
            //fill the model
            ConvertToRenderingModel(regexGroups);

            //if model has id then add
            if (_renderingModel != null)
                ReportModel.getReportingRenderingList().set(_renderingModelIndexOf, _renderingModel);
        }
    }

    /**
     * Convert the line and fill the model
     */
    private void ConvertToRenderingModel(ArrayList<String> groups) {

        //convert documentId
        ConvertDocumentId(groups);

        //convert documentId
        ConvertUniqueId(groups);

        //convert getRendering
        ConvertGetRendering(groups);
    }

    /**
     * Get the Document Id of a line
     */
    private void ConvertDocumentId(ArrayList<String> lineBlocks) {

        //if has the term
        if (lineBlocks.get(4).contains(SearchTermsEnum.START_RENDERING.getTerm())) {
            ArrayList<String> groups = regex.Convert(lineBlocks.get(4), "\\[([^\\]]*)\\]");

            if (groups.size() > 0) {

                _renderingModel = new ReportRenderingModel();
                _renderingModel.setTStampsGetRendering(new ArrayList<LocalDateTime>());
                _renderingModel.setTStampsStartRendering(new ArrayList<LocalDateTime>());

                //split the id and page
                String[] result = groups.get(0).split(",");

                //set id and page
                _renderingModel.setThread(lineBlocks.get(1));
                _renderingModel.setDocumentId(Integer.parseInt(result[0].trim()));
                _renderingModel.setPage(Integer.parseInt(result[1].trim()));

                //add item in a list
                ReportModel.getReportingRenderingList().add(_renderingModel);

                //get indexOf
                _renderingModelIndexOf = ReportModel.getReportingRenderingList().indexOf(_renderingModel);
            }
        }
    }

    /**
     * Get the UniqueId of a line
     */
    private void ConvertUniqueId(ArrayList<String> lineBlocks) {

        //if has the term
        if (_renderingModel != null) {
            if (lineBlocks.get(4).contains(SearchTermsEnum.UNIQUE_ID.getTerm())) {
                ArrayList<String> groups = regex.Convert(lineBlocks.get(4), "(\\d*-\\d*)");

                if (groups.size() > 0) {
                    _renderingModel.setUUID(groups.get(0));

                    //set timestamp startRendering
                    ConvertTimeStamp(lineBlocks.get(0), SearchTermsEnum.START_RENDERING);
                }
            }
        }
    }

    /**
     * Get the Timestamp of a line (StartRendering and GetRendering)
     */
    private void ConvertTimeStamp(String lineBlock, SearchTermsEnum searchTerm) {

        if (_renderingModel != null) {
            //format datetime by the default formatting
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMATTING);
            LocalDateTime dateTime = LocalDateTime.parse(lineBlock, formatter);

            if (searchTerm == SearchTermsEnum.START_RENDERING)
                _renderingModel.getTStampsStartRendering().add(dateTime);
            else if (searchTerm == SearchTermsEnum.GET_RENDERING)
                _renderingModel.getTStampsGetRendering().add(dateTime);
        }
    }

    /**
     * Get the GetRendering of a line
     */
    private void ConvertGetRendering(ArrayList<String> lineBlocks) {

        //if has the term
        if (ReportModel.getReportingRenderingList().size() > 0) {
            if (lineBlocks.get(4).contains(SearchTermsEnum.GET_RENDERING.getTerm())) {

                ArrayList<String> groups = regex.Convert(lineBlocks.get(4), "\\[([^\\]]*)\\]");

                if (groups.size() > 0) {

                    //reset the model
                    _renderingModel = null;

                    //gets the first occurrency by the UUID
                    Optional<ReportRenderingModel> optionalRendering = ReportModel.getReportingRenderingList().stream()
                            .filter((x) ->  x.getUUID() != null && x.getUUID().equals(groups.get(0))).findFirst();

                    if (!optionalRendering.isEmpty()) {
                        _renderingModel = optionalRendering.get();

                        //set timestamp GetRendering
                        if (_renderingModel != null) {
                            ConvertTimeStamp(lineBlocks.get(0), SearchTermsEnum.GET_RENDERING);

                            //get indexOf
                            _renderingModelIndexOf = ReportModel.getReportingRenderingList().indexOf(_renderingModel);
                        }
                    }
                }
            }
        }
    }
}

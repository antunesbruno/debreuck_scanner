package com.debreuck.analysis;

import com.debreuck.enums.SearchTermsEnum;
import com.debreuck.enums.XmlAliasEnum;
import com.debreuck.models.ReportModel;
import com.debreuck.models.ReportRenderingModel;
import com.debreuck.models.ReportSummaryModel;
import com.debreuck.utils.Constants;
import com.debreuck.utils.CreateXMLReport;
import com.debreuck.utils.LogPrint;
import com.debreuck.utils.RegularExpression;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class LogAnalysis {

    private RegularExpression regex;
    private ReportModel ReportModel;
    private Integer _renderingModelIndexOf = 0;
    private ReportRenderingModel _renderingModel;
    private Integer _countRenderings = 0;
    private Integer _countUniqueDuplicates = 0;
    private HashSet<String> _uniqueDuplicates = new HashSet<>();
    private Integer _countGetRenderings = 0;
    private Integer _countStartRenderings = 0;

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

            //create xml
            CreateXMLFileWithAlias();

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
                _renderingModel.setTStampsGetRendering(new ArrayList<>());
                _renderingModel.setTStampsStartRendering(new ArrayList<>());

                //split the id and page
                String[] result = groups.get(0).split(",");

                //set id and page
                _renderingModel.setDocument(Integer.parseInt(result[0].trim()));
                _renderingModel.setPage(Integer.parseInt(result[1].trim()));

                //add item in a list
                ReportModel.getReportingRenderingList().add(_renderingModel);

                //get indexOf
                _renderingModelIndexOf = ReportModel.getReportingRenderingList().indexOf(_renderingModel);

                //count renderings
                _countRenderings++;
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
                    _renderingModel.setUid(groups.get(0));

                    //set timestamp startRendering
                    ConvertTimeStamp(lineBlocks.get(0), SearchTermsEnum.START_RENDERING);

                    //count start renderings
                    _countStartRenderings++;

                    //duplicates
                    if(!_uniqueDuplicates.add(groups.get(0)))
                        _countUniqueDuplicates++;
                }
            }
        }
    }

    /**
     * Get the Timestamp of a line (StartRendering and GetRendering)
     */
    private void ConvertTimeStamp(String lineBlock, SearchTermsEnum searchTerm) {

        if (_renderingModel != null) {
            if (searchTerm == SearchTermsEnum.START_RENDERING)
                _renderingModel.getTStampsStartRendering().add(lineBlock);
            else if (searchTerm == SearchTermsEnum.GET_RENDERING)
                _renderingModel.getTStampsGetRendering().add(lineBlock);
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
                            .filter((x) ->  x.getUid() != null && x.getUid().equals(groups.get(0))).findFirst();

                    if (!optionalRendering.isEmpty()) {
                        _renderingModel = optionalRendering.get();

                        //set timestamp GetRendering
                        if (_renderingModel != null) {
                            ConvertTimeStamp(lineBlocks.get(0), SearchTermsEnum.GET_RENDERING);

                            //count get renderings
                            _countGetRenderings++;

                            //get indexOf
                            _renderingModelIndexOf = ReportModel.getReportingRenderingList().indexOf(_renderingModel);
                        }
                    }
                }
            }
        }
    }

    /**
     * Create XML file by the models filled using XStream XML Library
     */
    private void CreateXMLFileWithAlias() {

        if (ReportModel.getReportingRenderingList().size() > 0) {

            //set summary informations
            SetInformationsToSummary();

            //create the alias
            HashMap<XmlAliasEnum, Object> alias = new HashMap<>();

            //alias for classes
            HashMap<String, Class> aliasClasses = new HashMap<String, Class>();
            aliasClasses.put("report", ReportModel.class);
            aliasClasses.put("rendering", ReportRenderingModel.class);
            aliasClasses.put("summary", ReportSummaryModel.class);

            //alias for fields
            HashMap<String, HashMap<String, Class>> aliasFields = new HashMap<String, HashMap<String, Class>>();

            //timestamp field start
            HashMap<String, Class> timeStampStart = new HashMap<String, Class>();
            timeStampStart.put("TStampsStartRendering", ReportRenderingModel.class);
            aliasFields.put("start", timeStampStart);

            //timestamp field get
            HashMap<String, Class> timeStampGet = new HashMap<String, Class>();
            timeStampGet.put("TStampsGetRendering", ReportRenderingModel.class);
            aliasFields.put("get", timeStampGet);

            //alias for collections
            HashMap<String, Class> aliasCollections = new HashMap<String, Class>();
            aliasCollections.put("ReportingRenderingList", ReportModel.class);

            //set alias
            alias.put(XmlAliasEnum.CLASSES, aliasClasses);
            alias.put(XmlAliasEnum.FIELDS, aliasFields);
            alias.put(XmlAliasEnum.COLLECTION, aliasCollections);

            //generate XML
            try {

                //generate the file name
                String filename = CreateFilePathLocalTime();

                //create report XML
                CreateXMLReport.getInstance().toXMLFile(ReportModel, filename, alias);

                //print finish screen
                LogPrint.PrintFinish(filename);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set the summary informations in the report
     */
    private void SetInformationsToSummary()
    {
        ReportSummaryModel summary = new ReportSummaryModel();

        //number of renderings
        summary.setCount(_countRenderings);

        //duplicates renderings
        summary.setDuplicates(_countUniqueDuplicates);

        //start renderings without get renderings
        summary.setUnnecessary((_countStartRenderings - _countGetRenderings));

        //set summary
        ReportModel.setSummary(summary);
    }

    /**
     * Current file name with LocalDateTime to distinguish xml files generated
     * @return name of file
     */
    private String CreateFilePathLocalTime() {
        //get current path
        String filePath = System.getProperty("user.dir");

        //load file
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        //current file name
        return filePath + "\\" + Constants.FOLDER_FILE_GENERATED + "\\" + now.format(formatter) + "_serverLog.xml";
    }
}

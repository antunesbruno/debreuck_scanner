package com.debreuck.utils;

import com.debreuck.enums.XmlAliasEnum;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public final class CreateXMLReport {

    private XStream xstream = null;

    private CreateXMLReport() {
        xstream = new XStream();
        xstream.ignoreUnknownElements();
    }

    /**
     * Convert a any given Object to a XML String
     *
     * @param object
     * @return
     */
    public String toXMLString(Object object) {
        return xstream.toXML(object);
    }

    /**
     * Convert given XML to an Object
     *
     * @param xml
     * @return
     */
    public Object toObject(String xml) {
        return (Object) xstream.fromXML(xml);
    }

    /**
     * return this class instance
     *
     * @return
     */
    public static CreateXMLReport getInstance() {
        return new CreateXMLReport();
    }

    /**
     * convert to Object from given File
     *
     * @param xmlFile
     * @return
     * @throws IOException
     */
    public Object toObject(File xmlFile) throws IOException {
        return xstream.fromXML(new FileReader(xmlFile));
    }

    /**
     * create XML file from the given object with custom file name
     *
     * @param fileName
     * @throws IOException
     */
    public void toXMLFile(Object objTobeXMLTranslated, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        xstream.toXML(objTobeXMLTranslated, writer);
        writer.close();
    }

    /**
     * create XML file from the given object with custom file name and alias
     *
     */
    public void toXMLFile(Object objTobeXMLTranslated, String fileName, HashMap<XmlAliasEnum, Object> alias) throws IOException {

        //set alias
        for(Map.Entry<XmlAliasEnum, Object> itemAlias : alias.entrySet()) {

            switch (itemAlias.getKey())
            {
                case CLASSES:
                {
                    setAliasClassToXML((HashMap<String, Class>)itemAlias.getValue());
                    break;
                }
                case FIELDS:
                {
                    setAliasFieldsToXML((HashMap<String, HashMap<String, Class>>)itemAlias.getValue());
                    break;
                }
                case COLLECTION:
                {
                    setImplicitCollectionsToXML((HashMap<String, Class>)itemAlias.getValue());
                    break;
                }
            }
       }

        //date format
        String[] acceptableFormats = {"HHmmss"};
        xstream.registerConverter(new DateConverter(Constants.DATETIME_FORMATTING,acceptableFormats));

        //convert xml
        toXMLFile(objTobeXMLTranslated, fileName);
    }

    public void toXMLFile(Object objTobeXMLTranslated, String fileName, List omitFieldsRegXList) throws IOException {
        xstreamInitializeSettings(objTobeXMLTranslated, omitFieldsRegXList);
        toXMLFile(objTobeXMLTranslated, fileName);
    }

    /**
     * @param objTobeXMLTranslated
     * @
     */
    public void xstreamInitializeSettings(Object objTobeXMLTranslated, List omitFieldsRegXList) {
        if (omitFieldsRegXList != null && omitFieldsRegXList.size() > 0) {
            Iterator itr = omitFieldsRegXList.iterator();
            while (itr.hasNext()) {
                String omitEx = (String) itr.next();
                xstream.omitField(objTobeXMLTranslated.getClass(), omitEx);
            }
        }
    }

    /**
     * create XML file from the given object, file name is generated automatically (class name)
     *
     * @param objTobeXMLTranslated
     * @throws IOException
     */
    public void toXMLFile(Object objTobeXMLTranslated) throws IOException {
        toXMLFile(objTobeXMLTranslated, objTobeXMLTranslated.getClass().getName() + ".xml");
    }

    /**
     * create alias for a XML file
     * @param aliasList
     */
    private void setAliasClassToXML(HashMap<String, Class> aliasList) {
        for(Map.Entry<String, Class> alias : aliasList.entrySet()) {
            xstream.alias(alias.getKey(), alias.getValue());
        }
    }

    /**
     * create alias for fields a XML file
     * @param aliasList
     */
    private void setAliasFieldsToXML(HashMap<String, HashMap<String, Class>> aliasList) {
        for(Map.Entry<String, HashMap<String, Class>> alias : aliasList.entrySet()) {
            for(Map.Entry<String, Class> aliasClass : alias.getValue().entrySet()){
                xstream.addImplicitCollection(aliasClass.getValue(), aliasClass.getKey(), alias.getKey(), String.class);
            }
        }
    }

    /**
     * create implicit collections a XML file
     * @param aliasList
     */
    private void setImplicitCollectionsToXML(HashMap<String, Class> aliasList) {
        for(Map.Entry<String, Class> alias : aliasList.entrySet()) {
            xstream.addImplicitCollection(alias.getValue(), alias.getKey());
        }
    }

}


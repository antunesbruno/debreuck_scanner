package com.debreuck.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpression {

    /**
     * Convert a string using a regular expression predefined and return a string array
     * @param term string to convert
     * @param expression regular expression
    */
    public ArrayList<String> Convert(String term, String expression)
    {
        //result of conversion
        ArrayList<String> result = new ArrayList<String>();

        //convert regex
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(term);
        if (matcher.find())
        {
            if(matcher.groupCount() > 0) {
                for (int i = 0; i < matcher.groupCount(); i++) {
                    result.add(matcher.group(i + 1));
                }
            }
        }

        return result;
    }

}

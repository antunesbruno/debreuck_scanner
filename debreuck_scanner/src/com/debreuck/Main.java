package com.debreuck;

import com.debreuck.analysis.LogAnalysis;
import com.debreuck.utils.LoadFile;
import java.io.BufferedReader;

public class Main {

    public static void main(String[] args) {

        //instance class to open file
        LoadFile file = new LoadFile();

        //instance class to analysis the log
        LogAnalysis analysis = new LogAnalysis();

        //open log file
        BufferedReader br = file.GetBufferedFile();

        //start log analysis
        analysis.IterateLog(br);

        //close stream
        file.CloseBufferedFile();
    }
}

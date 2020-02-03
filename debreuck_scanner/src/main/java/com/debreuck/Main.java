package com.debreuck;

import com.debreuck.analysis.LogAnalysis;
import com.debreuck.utils.LoadFile;
import com.debreuck.utils.LogPrint;

import java.io.BufferedReader;

public class Main {

    public static void main(String[] args) {

        //print start screen
        LogPrint.PrintPresentation();

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

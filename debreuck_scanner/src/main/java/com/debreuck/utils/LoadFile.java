package com.debreuck.utils;

import java.io.*;

public class LoadFile {

    //log path
    String fileName = "logs\\server.log";

    //file stream
    protected FileInputStream fstream;

    public BufferedReader GetBufferedFile() {
        try {

            //get current path
            String filePath = System.getProperty("user.dir");

            //load file
            fstream = new FileInputStream(filePath + "\\" + fileName);

            //open buffer
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            //return buffer
            return br;

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    public void CloseBufferedFile(){
        if (fstream != null) {
            try {
                fstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

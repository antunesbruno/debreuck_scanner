package com.debreuck.utils;

public final class LogPrint {

    public static final void PrintLineLog(String message)
    {
        System.out.println(message);
    }

    public static final void PrintPresentation()
    {
        String presentation = new StringBuilder()
                .append("|***************************************************************|\n")
                .append("|***                WELCOME - DEBREUCK SCANNER               ***|\n")
                .append("|***                                                         ***|\n")
                .append("|***                     Starting...                         ***| \n")
                .append("|***                                                         ***|\n")
                .append("|************************************************************** |\n")
                .toString();

        PrintLineLog(presentation);
    }

    public static void PrintFinish(String filePathGenerated) {
        String finishing = new StringBuilder()
                .append("|***************************************************************|\n")
                .append("|***                 THANKS FOR USING                        ***|\n")
                .append("|***                 DEBREUCK SCANNER                        ***|\n")
                .append("|***                     Finished...                         ***| \n")
                .append("|***                                                         ***|\n")
                .append("|*** You files was Created successfully !!                   ***|\n")
                .append("|*** Verify the folder below !!                              ***|\n")
                .append("|************************************************************** |\n")
                .append("\n")
                .append(filePathGenerated)
                .toString();

        PrintLineLog(finishing);
    }

}

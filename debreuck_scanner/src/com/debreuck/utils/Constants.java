package com.debreuck.utils;

public final class Constants {

    /** default log expression <timestamp> [<thread>]<loglevel> [<message sender class>]: <log message> */
    public static final String LOG_EXPRESSION = "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) \\[([^\\]]*)\\] ([^\\ ]*)\\s*\\[([^\\]]*)\\]:\\s(.*)";

}

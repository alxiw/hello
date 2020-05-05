package io.github.alxiw.icq.geekbot.sys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLogger {

    private static final Logger logger = LoggerFactory.getLogger(AppLogger.class);
    private static final String TAG = "IM_APP";

    public static void i(String message) {
        logger.info(String.format("%s %s", TAG, message));
    }

    public static void e(String message) {
        logger.error(String.format("%s %s", TAG, message));
    }

    public static void e(Exception e, String message) {
        logger.error(String.format("%s %s", TAG, message), e);
    }
}

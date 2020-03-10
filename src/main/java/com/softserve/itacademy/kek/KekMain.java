package com.softserve.itacademy.kek;

import java.io.IOException;

import org.apache.log4j.Logger;

public class KekMain {
    private final static Logger logger = Logger.getLogger(KekMain.class);

    public static void main(String[] args) {
        try {
            logger.info("Starting application");
            new EmbeddedTomcatApp().start();
        } catch (IOException e) {
            logger.error("Could not start embedded tomcat");
            e.printStackTrace();
        }
    }
}

package com.softserve.itacademy.kek;

import java.io.IOException;

import org.apache.log4j.Logger;

public class KekMain {
    final static Logger logger = Logger.getLogger(KekMain.class);

    public static void main(String[] args) {
        try {
            logger.info("Starting embedded tomcat");
            new EmbeddedTomcatApp().start();
            logger.info("Embedded tomcat started");
        } catch (IOException e) {
            logger.error("Could not start embedded tomcat");
            e.printStackTrace();
        }
    }
}

package com.softserve.itacademy.kek;

import com.softserve.itacademy.kek.configuration.WebAppInitializer;
import com.softserve.itacademy.kek.security.SecurityWebAppInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.SpringServletContainerInitializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class EmbeddedTomcatApp {
    private final static Logger logger = LoggerFactory.getLogger(EmbeddedTomcatApp.class);
    private final Tomcat tomcat;

    /**
     * Creates the object to control the embedded Tomcat server.
     * Reads server parameters from the server.properties.
     * If the properties are not defined in the file, uses the following default values:
     * - port = 8080
     * - contextPath = \
     * - appBase = .
     *
     * @throws IOException in case when the properties file is not found
     */
    public EmbeddedTomcatApp() throws IOException {
        logger.info("Reading the server properties file");

        InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties");
        Properties properties = new Properties();
        properties.load(resourceStream);

        final int port = Integer.parseInt(properties.getProperty("server.port", "8080"));
        final String docBase = properties.getProperty("doc.base", new File("").getAbsolutePath());

        logger.info("Configuring embedded tomcat");

        tomcat = new Tomcat();
        tomcat.setPort(port);

        final Context context = tomcat.addContext("", docBase);

        Set<Class<?>> classes = new LinkedHashSet<>();
        classes.add(WebAppInitializer.class);
        classes.add(SecurityWebAppInitializer.class);

        context.addServletContainerInitializer(new SpringServletContainerInitializer(), classes);

        Tomcat.initWebappDefaults(context);
    }

    /**
     * Starts the Tomcat server with defined parameters
     */
    public void start() {
        try {
            logger.info("Starting embedded tomcat");

            tomcat.start();

            logger.info("Embedded tomcat started");

            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the Tomcat server
     */
    public void stop() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}

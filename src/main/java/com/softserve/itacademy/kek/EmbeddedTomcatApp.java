package com.softserve.itacademy.kek;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

import com.softserve.itacademy.kek.configuration.WebAppInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.SpringServletContainerInitializer;

// TODO: Add logger

public class EmbeddedTomcatApp {
    private final Tomcat tomcat;

    /**
     * Creates the object to control the embedded Tomcat server.
     * Reads server parameters from the server.properties.
     * If the properties are not defined in the file, uses the following default values:
     * - port = 8080
     * - contextPath = \
     * - appBase = .
     * @throws IOException in case when the properties file is not found
     */
    public EmbeddedTomcatApp() throws IOException {
        InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties");
        Properties properties = new Properties();
        properties.load(resourceStream);

        int port = Integer.parseInt(properties.getProperty("server.port", "8080"));
        String docBase = properties.getProperty("doc.base", new File("").getAbsolutePath());

        tomcat = new Tomcat();
        tomcat.setPort(port);

        final Context context = tomcat.addContext("", docBase);

        Tomcat.initWebappDefaults(context);

        context.addServletContainerInitializer(new SpringServletContainerInitializer(),
                Collections.singleton(WebAppInitializer.class));
    }

    /**
     * Starts the Tomcat server with defined parameters
     */
    public void start() {
        try {
            tomcat.start();
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



package com.softserve.itacademy.kek;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

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
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String appConfigPath = rootPath + "server.properties";
        Properties properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));

        int port = Integer.parseInt(properties.getProperty("server.port", "8080"));
        String contextPath = properties.getProperty("server.context", "\\");
        String appBase = properties.getProperty("server.appBase", ".");

        tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getHost().setAppBase(appBase);
        tomcat.addWebapp(contextPath, appBase);
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

package com.softserve.itacademy.kek;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

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

        tomcat = new Tomcat();
        tomcat.setPort(port);
        File base = new File("");
        Context rootCtx = tomcat.addContext("", base.getAbsolutePath());
        rootCtx.setDocBase("static");
        AnnotationConfigWebApplicationContext actx = new AnnotationConfigWebApplicationContext();
        actx.scan("com.softserve.itacademy.kek");
        DispatcherServlet dispatcher = new DispatcherServlet(actx);
        Tomcat.addServlet(rootCtx, "SpringMVC", dispatcher);
        rootCtx.addServletMapping("/*", "SpringMVC");
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

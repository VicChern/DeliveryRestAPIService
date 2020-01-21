package com.softserve.itacademy.kek;

import java.io.IOException;

public class KekMain {

    public static void main(String[] args) {
        try {
            new EmbeddedTomcatApp().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

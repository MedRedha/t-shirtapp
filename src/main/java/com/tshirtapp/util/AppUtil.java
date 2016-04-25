package com.tshirtapp.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by aminedev on 1/23/16.
 */
public class AppUtil {

    public String ROOT = "";
    public static String APP_NAME = "tshirtapp";
    public static String LOGOS = "logos";
    public static String PAYMENTS = "payments";

    public static String createAppDir() {
        String userHome = "user.home";
        String pathUserHome = System.getProperty(userHome);
        Path path = Paths.get(pathUserHome + File.separator + APP_NAME);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pathUserHome + File.separator + APP_NAME;
    }


    public static String createLogoDir() throws IOException {
        String userHome = "user.home";
        String pathUserHome = System.getProperty(userHome);
        Path path = Paths.get(pathUserHome + File.separator + APP_NAME + File.separator + LOGOS);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        return pathUserHome + File.separator + APP_NAME + File.separator + LOGOS;
    }

    public static String createPaymentDir() throws IOException {
        String userHome = "user.home";
        String pathUserHome = System.getProperty(userHome);
        Path path = Paths.get(pathUserHome + File.separator + APP_NAME + File.separator + PAYMENTS);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        return pathUserHome + File.separator + APP_NAME + File.separator + PAYMENTS;
    }
}

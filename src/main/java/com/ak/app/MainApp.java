package com.ak.app;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class MainApp {
    private MainApp() {
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(MainApp.class.getName());
        logger.info(() -> "Hello word 2024.02.03!");
        List<String> rheo = new ArrayList<>();

        try {
            for (String s : Files.readAllLines(Path.of("2023-11-15 13-17-59 375 aper.csv"), Charset.defaultCharset())) {
                rheo.add(s.split(",")[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        double[] x = rheo.stream().skip(1).mapToDouble(Double::parseDouble).toArray();
        logger.info(() -> Arrays.toString(x));

        double[] q = new double[x.length];
        double[] y = new double[x.length];
        double a = 0.95;
        y[0] = x[0];
        q[0] = x[0];
        for (int n = 1; n < x.length; n++) {
            q[n] = x[n] + a * q[n - 1];
            y[n] = q[n] - q[n - 1];
        }
        logger.info(() -> Arrays.toString(y));
    }
}
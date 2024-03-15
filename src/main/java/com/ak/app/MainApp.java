package com.ak.app;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class MainApp {
  private MainApp() {
  }

  public static void main(String[] args) throws IOException {
    Logger logger = Logger.getLogger(MainApp.class.getName());
    logger.info(() -> "Hello word 2024.02.03!");
    List<String> rheo = new ArrayList<>();

    for (String s : Files.readAllLines(Path.of("2023-11-15 13-17-59 375 aper.csv"), Charset.defaultCharset())) {
      rheo.add(s.split(";")[1]);
    }

    double[] x = rheo.stream().mapToDouble(Double::parseDouble).toArray();
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

    Files.writeString(Path.of("output.txt"),
        DoubleStream.of(y).mapToObj("%.6f"::formatted).collect(Collectors.joining("\n")));

    List<List<Double>> result = splitIntoSubarrays(y);
    logger.info(result::toString);

    Files.writeString(Path.of("output2.txt"), result.toString());

    List<Double> extremeValues = new ArrayList<>();

    for (List<Double> subarray : result) {
      boolean isPositive = subarray.getFirst() > 0;
      double extreme = findExtremeValue(subarray, isPositive);
      extremeValues.add(extreme);
    }
    logger.info(extremeValues::toString);
    Files.writeString(Path.of("output_extreme.txt"), extremeValues.toString());

    List<Integer> extremeIndexes = findExtremeIndexes(y, extremeValues);
    logger.info(extremeIndexes::toString);
    Files.writeString(Path.of("output_extremeindex.txt"), extremeIndexes.toString());

    List<Double> differences = calculateModulusDifferences(extremeValues);
    logger.info(differences::toString);
    Files.writeString(Path.of("output_module.txt"), differences.toString());

    List<Double> maxima = extractMaxima(extremeValues);
    List<Double> minima = extractMinima(extremeValues);
    logger.info(maxima::toString);
    Files.writeString(Path.of("extreme_max.txt"),
        maxima.toString());

    logger.info(minima::toString);
    Files.writeString(Path.of("extreme_min.txt"), minima.toString());

    List<Integer> maximaIndexes = findMaximaIndexes(y, maxima);
    logger.info(maximaIndexes::toString);
    Files.writeString(Path.of("Index_max.txt"), maximaIndexes.toString());

    List<Double> indexDifferences = calculateIndexDifferences(maximaIndexes);
    logger.info(indexDifferences::toString);
    Files.writeString(Path.of("Modulumax_Breathe rate.txt"), indexDifferences.toString());
  }

  public static List<List<Double>> splitIntoSubarrays(double[] doubles) {
    List<List<Double>> subarrays = new ArrayList<>(); //

    List<Double> currentSubarray = new ArrayList<>();

    for (double num : doubles) {
      if (currentSubarray.isEmpty() || (num > 0 && currentSubarray.getFirst() > 0) || (num < 0 && currentSubarray.getFirst() < 0)) {
        currentSubarray.add(num);
      }
      else {
        subarrays.add(currentSubarray);
        currentSubarray = new ArrayList<>();
        currentSubarray.add(num);
      }
    }

    if (!currentSubarray.isEmpty()) {
      subarrays.add(currentSubarray);
    }

    return subarrays;
  }

  public static double findExtremeValue(List<Double> subarray, boolean findMax) {
    double extreme = subarray.getFirst();
    for (double num : subarray) {
      if ((findMax && num > extreme) || (!findMax && num < extreme)) {
        extreme = num;
      }
    }
    return extreme;
  }

  public static List<Integer> findExtremeIndexes(double[] y, List<Double> extremeValues) {
    List<Integer> extremeIndexes = new ArrayList<>();
    for (double extreme : extremeValues) {
      for (int i = 0; i < y.length; i++) {
        if (y[i] == extreme) {
          extremeIndexes.add(i);
          break;  // Чтобы не добавлять один элемент несколько раз
        }
      }
    }
    return extremeIndexes;
  }

  public static List<Double> calculateModulusDifferences(List<Double> extremeValues) {
    List<Double> differences = new ArrayList<>();
    for (int i = 1; i < extremeValues.size(); i++) {
      double diff = Math.abs(extremeValues.get(i) - extremeValues.get(i - 1));
      differences.add(diff);
    }
    return differences;
  }

  public static List<Double> extractMaxima(List<Double> extremeValues) {
    List<Double> maxima = new ArrayList<>();
    for (double extreme : extremeValues) {
      if (extreme > 0) {
        maxima.add(extreme);
      }
    }
    return maxima;
  }

  public static List<Double> extractMinima(List<Double> extremeValues) {
    List<Double> minima = new ArrayList<>();
    for (double extreme : extremeValues) {
      if (extreme < 0) {
        minima.add(extreme);
      }
    }
    return minima;
  }

  public static List<Integer> findMaximaIndexes(double[] y, List<Double> maxima) {
    List<Integer> maximaIndexes = new ArrayList<>();
    for (double maximum : maxima) {
      for (int i = 0; i < y.length; i++) {
        if (y[i] == maximum) {
          maximaIndexes.add(i);
          break;
        }
      }
    }
    return maximaIndexes;
  }

  public static List<Double> calculateIndexDifferences(List<Integer> integers) {
    List<Double> maximaDifferences = new ArrayList<>();
    for (int i = 0; i < integers.size() - 1; i++) {
      double diff = Math.abs(integers.get(i + 1) - integers.get(i));
      maximaDifferences.add(diff);
    }
    return maximaDifferences;
  }
}




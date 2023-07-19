package org.example;

import org.example.utils.ConfigProject;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import javax.money.spi.MonetaryCurrenciesSingletonSpi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    //user's input
    Scanner scanner = new Scanner(System.in);
    String currencyOption = "";
    String conversionOption = "";
    String result = "";
    double finalResult;
    long howMuch = 0;
    System.out.println("What's your currency? (EUR, USD, BRL) ?");
    currencyOption = scanner.nextLine();
    System.out.println("How much money do you want to convert? ");
    howMuch = scanner.nextLong();
    scanner.nextLine();
    System.out.println("Which currency do you want to convert? (EUR, USD or BRL) ");
    conversionOption = scanner.nextLine();
    //retrieving responses from user
    String apiUrl = "https://v6.exchangerate-api.com/v6/MY_API_KEY/latest/" + currencyOption;
    String apiKey = ConfigProject.API_KEY;

    try {
      URL url = new URL(apiUrl.replace("MY_API_KEY", apiKey));

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
          response.append(line);
        }
        reader.close();

        result = response.substring(response.indexOf(conversionOption.toUpperCase()), response.indexOf(conversionOption.toUpperCase()) + response.indexOf(","));
        finalResult = Double.parseDouble(result.substring(result.indexOf(":") + 1, result.indexOf(",")));
        System.out.println("Your final response is: " + (finalResult * howMuch));
      } else {
        System.out.println("GET request failed. Response Code: " + responseCode);
      }

      connection.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
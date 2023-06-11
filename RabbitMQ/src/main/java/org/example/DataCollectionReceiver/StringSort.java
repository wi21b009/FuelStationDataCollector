package org.example.DataCollectionReceiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringSort {

    public static String sorted(String sort) {


    return null;
    }

    public static void StationSort(String message) {
        String[] input = message.split("\\|");

        int JobID = Integer.parseInt(input[1].trim());

        String info = input[0];

        // Regular expression pattern to match the desired values
        Pattern pattern = Pattern.compile("\\{id=(\\d+), kwh='([\\d.]+)', custmer_id=(\\d+), port=(\\d+)}");
        Matcher matcher = pattern.matcher(info);

        // Extract the values using regular expression matching
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            double kwh = Double.parseDouble(matcher.group(2));
            int customerId = Integer.parseInt(matcher.group(3));
            int port = Integer.parseInt(matcher.group(4));
        }

    }

    public static void HeaderSort(String message) {
        String[] input = message.split("\\|");

        int JobID = Integer.parseInt(input[0].trim());
        int UserID = Integer.parseInt(input[1].trim());
        int numDB = Integer.parseInt(input[2].trim());
    }
}

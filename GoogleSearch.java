package com.github.assemblingpanda;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GoogleSearch {
    // Google Search Result Web Scraper
    // Precondition: keywords will have to be "type of restaurant/cuisine (space) ZIP code"
    // Postcondition: (Current)
    //                  Create and write to file the results from the search after parsing from html to plaintext
    // Postcondition: (Later)
    //                  Return parsed information, results from the search after parsing from html to plaintext,
    //                  and parsing the plaintext to get relevant information, to the caller.
    public static String getResRec(String keywords){
        // Setting up return value and the search query
        String parsed = "";
        String [] split = keywords.split(" ");
        String url = "https://www.google.com/search" + "?q=" + "\"" + split[0] + "\"%20" + "restaurants%20near%20\"" + split[1] + "\"";
        //String url = "https://www.google.com/maps/search/" + split[0] + "+restaurants+near+" + split[1]; // Google Maps is not compatible...
        try {
            // Get the HTML source with the query (url), then parse to plaintext
            Document doc = Jsoup.connect(url).get();
            String plaintext = doc.text();

            // Write the plaintext to a new file to examine on how to parse/get futher information
            File file = new File("C:\\Users\\prakr\\Desktop\\School\\GMU\\Fall 2020\\Software Engineering\\myfirstbot\\src\\main\\java\\com\\github\\assemblingpanda\\htmlPrintOut1.txt");
            file.createNewFile();
            FileWriter wFile = new FileWriter(file);
            BufferedWriter outFile = new BufferedWriter(wFile);
            outFile.write(plaintext);
            outFile.close();
            wFile.close();
        } catch(IOException e) {
            System.out.println("Either file could not be created or could not connect.");
        };
        return parsed;
    }
}

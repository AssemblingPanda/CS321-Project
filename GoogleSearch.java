package com.github.assemblingpanda;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GoogleSearch {

    // Google Search Result Web Scraper for Restaurant Recommendations
    // Precondition: keywords will have to be "type of restaurant/cuisine (space) ZIP code"
    // Postcondition: Return parsed information, results from the search after parsing from html to plaintext,
    //                and parsing the plaintext to get relevant information, to the caller
    public static String getResRec(String keywords){
        // Setting up return value, processing value, and the getting the search results for the search query
        String completeResult = "";
        String totallyParsed = "";
        String plaintext = doSearch(keywords, 1, "HTMLtoPlainTextRes");

        // If only 1 restaurant show up:
        // If "See photos" exists then that means that after that substring,
        // there will be information regarding a restaurant, return that to the caller
        if(plaintext.contains("See photos")){
            return plaintext.split("See photos")[1];
        }

        // Don't think we need this algorithm, everything works very smoothly so far
        // If 3 restaurants show up:
        // If algorithm below results in no "See photos" being contained in the plaintext then use this algorithm:
        // Save info after "Hours or services may differ" that will be the name of the restaurant, but up to a "(" then remove the last token which would be the rating
        // or Save info after "Hours"
        // Continue searching after that point after "Delivery" and same as above
        // Do 1 more time
        // Append the zip to each of those search queries
        // ...
        // end

        // If no results from previous algorithm (not needed?), then another possible algorithm: (complete)
        // Parsing work: get addresses of restaurants (max 3 of them)
        // After thorough analysis of countless parsed HTML source code for Google searches,
        // using split here with the interpunct allows us to grab the address of the resulting restaurants that Google shows
        // Example: "... · information · information · information · ..."
        String [] splitPlaintext = plaintext.split("\u00b7"); // "·" interpunct

        // Remove the ZIP code from the user input so that we can utilize the restaurant type/cuisine to find the addresses
        String [] splitKeywords = keywords.split(" ");
        String keywordsPart = "";
        for(int i = 0; i < splitKeywords.length-1; i++){
            keywordsPart = keywordsPart + " " + splitKeywords[i];
        }

        // Parse the plaintext to get the addresses for up to 3 restaurants that Google gave
        // Sometimes there will be extra information past the address which cannot be removed, so cut the overall string
        // If the length of the address itself is greater than 30 characters, then we cut it up to 30
        // Why 30? After many tests and analyzing the countless addresses,
        // 30 seems to be the sweet spot to cut such that the search for the new string will give useful search results
        // Additionally, UPS, FedEx, and other shipping companies limit their address range between 30 and 35 characters
        // Example: " Type/Cuisine Address ..."
        String parsedPlaintext = "";
        for(String s : splitPlaintext){
            if(s.toLowerCase().startsWith(keywordsPart + " ") || s.toLowerCase().startsWith(" " + "restaurant" + "")) {
                if (s.replace(" Restaurant ", "").toLowerCase().replace(keywordsPart + " ", "").length() > 30) {
                    if(s.toLowerCase().startsWith(" restaurant ")) {
                        // Example: " Restaurant Address ..."
                        s = "Restaurant" + s.substring(11, 30 + 11);
                    }
                    else{
                        // Example: " Type/Cuisine Address ..."
                        s = keywordsPart + " " + s.substring(keywordsPart.length() + 1, 30 + keywordsPart.length()+1);
                    }
                }
                parsedPlaintext += s + "\n";
            }
        }

        // Used for debugging
        // Writes the addresses of the restaurants that Google showed:
        if(!writeFile(parsedPlaintext, "PlaintextParsed")) return "error file";

        // Addresses totally parsed
        totallyParsed = parsedPlaintext;

        // Split the addresses for each of them to be Google searched individually
        String [] splitQuery = totallyParsed.split("\n");

        // Do a search for up to 3 restaurants and see results for debugging purposes
        for(int i = 0; i < splitQuery.length; i++) {
            plaintext = doSearch(splitQuery[i], 3, "restaurant"+i);
            splitPlaintext = plaintext.split("See photos");

            // If "See photos" does not exist then from the countless analysis of Google's HTML,
            // either this restaurant is inaccessible due to the way the plaintext turns out from the HTML parse
            // or there is not enough information for this restaurant meaning that it should not be recommended anyways
            if(splitPlaintext.length < 2) continue;

            // The required information is always after "See photos" so setting/appending the 1th index-ed string for return
            completeResult = completeResult + " XA Potential RestaurantX " + splitPlaintext[1] + "\n\n";
        }
        // Used for debugging
        // Writes the information for the restaurants plus random extra junk:
        if(!writeFile(completeResult, "TheReturningInfo")) return "error file";
        return completeResult;
    }

    // Google Search Result Web Scraper for Recently Opened Restaurants
    // Precondition: keywords will have to be "ZIP code"
    // Postcondition: Return parsed information, results from the search after parsing from html to plaintext,
    //                and parsing the plaintext to get relevant information, to the caller
    public static String getRecentlyOpenedRes(String keyword){
        // Setting up return value, processing value, and the getting the search results for the search query
        String completeResult = "";
        String totallyParsed = "";
        String plaintext = doSearch(keyword, 2, "HTMLtoPlainTextRes");

        // Parsing work: get name and addresses of restaurants (max 2 of them)
        // After thorough analysis of countless parsed HTML source code for Google searches,
        // using split here with the interpunct allows us to grab the address of the resulting restaurants that Google shows
        // Example: "... · information · information · information · ..."
        String [] splitPlaintext = plaintext.split("\u00b7"); // "·" interpunct

        // Parse the plaintext to get the names and addresses for up to 2 restaurants that Google gave
        // Sometimes there will be extra information past the address which cannot be removed, so cut the overall string
        // If the length of the address itself is greater than 30 characters, then we cut it up to 30
        // Why 30? After many tests and analyzing the countless addresses,
        // 30 seems to be the sweet spot to cut such that the search for the new string will give useful search results
        // Additionally, UPS, FedEx, and other shipping companies limit their address range between 30 and 35 characters
        // When " Recently opened " pops up in the plaintext then after it is the name of the restaurant
        // After that information set/interpunct, the partial address will be available that contains a ","
        // Example: " Recently opened Name ... · ... Address, ..."
        String parsedPlaintext = "";
        int count = 0;
        for(int i = 0; i < splitPlaintext.length; i++){
            // Max 2 restaurants found
            if(count == 2) break;
            if(splitPlaintext[i].contains(" Recently opened ")) {
                count++;
                String temp = splitPlaintext[i].split(" Recently opened ")[1];

                // temp should have the name of the restaurant,
                // but if "Web results" is contained in that name then that means end of the results from Google
                if(temp.contains("Web results")) continue;
                parsedPlaintext += temp;

                // Google search is inconsistent, if this fails then there are no more restaurants
                try {
                    temp = splitPlaintext[i + 1];
                } catch(ArrayIndexOutOfBoundsException e){
                    continue;
                }
                String [] splitTemp = temp.split(",");
                temp = splitTemp[0];
                if(temp.length() > 35) {
                        temp = temp.substring(0, 35);
                }
                parsedPlaintext += temp + " " + keyword + "\n";
            }
        }

        // Used for debugging
        // Writes the addresses of up the restaurants that Google showed:
        if(!writeFile(parsedPlaintext, "PlaintextParsed")) return "error file";

        // Addresses totally parsed
        totallyParsed = parsedPlaintext;

        // Split the addresses for each of them to be Google searched individually
        String [] splitQuery = totallyParsed.split("\n");

        // Do a search for up to 2 restaurants and see results for debugging purposes
        for(int i = 0; i < splitQuery.length; i++) {
            plaintext = doSearch(splitQuery[i], 3, "restaurant"+i);
            splitPlaintext = plaintext.split("See photos");

            // If "See photos" does not exist then from the countless analysis of Google's HTML,
            // either this restaurant is inaccessible due to the way the plaintext turns out from the HTML parse
            // or there is not enough information for this restaurant meaning that it should not be recommended anyways
            if(splitPlaintext.length < 2) continue;

            // The required information is always after "See photos" so setting/appending the 1th index-ed string for return
            completeResult = completeResult + " XA Potential RestaurantX " + splitPlaintext[1] + "\n\n";
        }
        // Used for debugging
        // Writes the information for the restaurants plus random extra junk:
        if(!writeFile(completeResult, "TheReturningInfo")) return "error file";
        return completeResult;
    }

    // This is where the Google search happens for every type of search required for this bot to function
    // Takes in a query, a key to indicate what kind of search, and fileName used for debugging
    // Gets the HTML source and parses to plaintext
    // Returns the plaintext
    private static String doSearch(String query, int key, String fileName){
        String url = "";

        // Setup for Restaurant Recommendation Search:
        if(key == 1) {
            String [] splitQuery = query.split(" ");
            String queryPart = "";
            for(int i = 0; i < splitQuery.length-1; i++){
                queryPart = queryPart + "+" + splitQuery[i];
            }
            url = "https://www.google.com/search" + "?q=" + queryPart + "%20" + "restaurants%20near%20" + splitQuery[splitQuery.length-1];
        }

        // Setup for Recently Opened Restaurant Search:
        else if(key == 2){
            String [] splitQuery = query.split(" ");
            url = "https://www.google.com/search" + "?q=" + "recently%20opened%20restaurants%20near%20" + splitQuery[0];
        }

        // Setup for Individual Restaurant Search Based on Address and Cuisine/Type:
        else if(key == 3) {
            url = "https://www.google.com/search" + "?q=" + query.replaceAll(" ", "+") + "%20restaurant";
        }

        // System.out.println(url); Here for debugging
        String plaintext = "";
        try {
            // Get the HTML source with the query (url), then parse to plaintext
            Document doc = Jsoup.connect(url).get();
            plaintext = doc.text();

            // For debugging purposes
            // Write the plaintext to a new file to examine on how to parse/get further information
            if (!writeFile(plaintext, fileName)) return "error file";
        } catch(IOException e){
            return "error internet";
        }
        return plaintext;
    }

    // Used for debugging for thinking about ways to parse
    // Takes the string to write and the file to write on
    private static Boolean writeFile(String s, String fileName){
        try{
            File file = new File("C:\\Users\\prakr\\Desktop\\School\\GMU\\Fall 2020\\Software Engineering\\myfirstbot\\src\\main\\java\\com\\github\\assemblingpanda\\" + fileName + ".txt");
            file.createNewFile();
            FileWriter wFile = new FileWriter(file);
            BufferedWriter outFile = new BufferedWriter(wFile);
            outFile.write(s);
            outFile.close();
            wFile.close();
        } catch(IOException e){
            return false;
        }
        return true;
    }
}

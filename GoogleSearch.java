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
        String url = "https://www.google.com/search" + "?q=" + split[0] + "%20" + "restaurants%20near%20" + split[1];
        //String url = "https://www.google.com/search" + "?q=" + "\"" + split[0] + "\"%20" + "restaurants%20near%20\"" + split[1] + "\"";
        //String url = "https://www.google.com/maps/search/" + split[0] + "+restaurants+near+" + split[1]; // Google Maps is not compatible...
        try {
            // Get the HTML source with the query (url), then parse to plaintext
            Document doc = Jsoup.connect(url).get();
            String plaintext = doc.text();

            // Write the plaintext to a new file to examine on how to parse/get further information
            if(!writeFile(plaintext, "HTMLtoPlaintext")) return "error";

            // Parsing work: get addresses of restaurants (3 of them)
            String [] splitPlaintext = plaintext.split("\u00b7"); // "·" interpunct

            int i = 0;
            int entered = 0;
            String parsedPlaintext = "";
            for(String s : splitPlaintext){
                System.out.println(s);
                i++;
                if(s.toLowerCase().startsWith(" " + split[0] + "")) {
                    entered++;
                    if (s.length() > 40) {
                        s = s.substring(0, 40);
                    }
                    //if (entered != 3) {
                        //parsedPlaintext += s + splitPlaintext[i] + "\n";
                        parsedPlaintext += s + "\n";
                    //}
                }
            }
            if(!writeFile(parsedPlaintext, "PlaintextParsed")) return "error";
            parsed = parsedPlaintext; // set printing to those 3 res addresses

            // search one of the 3 restaurants and see if parsing is possible to get info
            splitPlaintext = parsed.split("\n");
            url = "https://www.google.com/search" + "?q=" + splitPlaintext[0].replaceAll(" ", "+");
            try{
                doc = Jsoup.connect(url).get();
                //doc = Jsoup.connect(url).get();
                plaintext = doc.body().text();
            } catch(IOException e){
                return "error";
            };
            if(!writeFile(plaintext, "res1")) return "error";

            // after checking file, it seems that it might be possible
            splitPlaintext = plaintext.split("See photos");
            // Then work with more delims. Can this be done? hmmm
            /*
                See photos

                Ciro's New York Pizza Website Directions Saved (0) Saved Save

                4.5351 Google reviews $$Italian restaurant Order pickup New! View menu and order food Hungry? Try ordering online Order
                delivery New York-style pizza & classic pastas are served in a wood-paneled space with burgundy accents. Dine-in· Takeout· No-contact delivery

                Located in: Centreville Crest Shopping Center

                Address: 6067 Centreville Crest Ln, Centreville, VA 20121

                Hours: Closed ? Opens 11AM Tuesday 11AM–11PM Wednesday 11AM–11PM Thursday 11AM–11PM Friday 11AM–11PM Saturday 11AM–11PM Sunday 11AM–10PM Monday 11AM–11PM

                Suggest an edit Unable to add this file. Please check that it is a valid photo.

                Menu: ciro-ristorante-online-ordering-centreville.brygid.online

                Phone: (703) 830-0003 Hours or services may differ Category: : Place name: : : : Website: : : Suggest an edit Unable to add this file...
            */
            // Algorithm:
            //              1. Remove all strings up to and including "See photos" // done above
            //              2. Name = Get string up to "Website"
            //              3. Rating = Get string after "Saved save" and before "Google reviews"
            //              4. Dollar signs = check $ count after
            //              5. Everything else is already set up.
            String allInfo = splitPlaintext[1];
            String name = "Name: " + (allInfo.split("Directions"))[0].replaceAll("Website", "");
            String ratings = "Ratings: " + (allInfo.split("Saved Save"))[1].split("Google reviews")[0].trim().substring(0,3) + "/5.0";
            String dollars = (allInfo.split("Google reviews ")[1]);
            dollars = "Cost: " + dollars.substring(0,5).replaceAll("[^$]", "");
            String address = "Address: " + (allInfo.split("Address: "))[1].split("Hours:")[0];
            //String hours = "Hours: Currently " + (allInfo.split("Hours: "))[1].split("Suggest an edit")[0].replaceAll(" ?", "");
            String [] splitPhoneNum =  (allInfo.split("Phone: "))[1].split(" ");
            String phone = "Phone: " + splitPhoneNum[0] + " " + splitPhoneNum[1];
            parsed = name + "\n" + address + "\n" + ratings + "\n" + dollars + "\n" + phone;
            System.out.println(parsed);
        } catch(IOException e) {
            return "error";
        };
        return parsed;
    }

    // Used for debugging and thinking about ways to parse
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

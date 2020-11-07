package com.github.huitk;
//RestaurantUtil class
public class RestaurantUtil {

    //GoogleSearch's getResRec() calls has a large string with delimiter between each restaurant.
    //Delimiter is "XA Potential RestaurantX".
    //When using split, array index 0 is always empty so restaurant array is -1 the split array.
    //For each set of restaurant information batch in token array, parse and get Restaurant object back.
    //If any Restaurants are null in Restaurant array, then make new array that holds no nulls.
    //Remove all duplicate Restaurants.
    //Return the Restaurant array or null if no restaurants.
    public static Restaurant[] getRecommendations(String criteria){
        String searchResults = GoogleSearch.getResRec(criteria);
        String[] tokens = searchResults.split("XA Potential RestaurantX");
        Restaurant[] recommendations = new Restaurant[tokens.length-1];
        boolean hasNull = false;
        int numberOfNulls = 0;
        for(int i = 0; i < tokens.length-1; i++){
            recommendations[i] = getSetInformation(tokens[i+1]);
            if(recommendations[i] == null){
                hasNull = true;
                numberOfNulls++;
            }
        }
        //remove nulls
        if(hasNull == true && (numberOfNulls > 0)){
            Restaurant[] tmp = new Restaurant[recommendations.length-numberOfNulls];
            if(tmp.length == 0){
                return null;
            }
            for(int i = 0, j = 0; i < recommendations.length && j < tmp.length; i++){
                if(recommendations[i] != null){
                    tmp[j] = recommendations[i];
                    j++;
                }
            }
            recommendations = tmp;
        }
        //remove duplicates
        numberOfNulls = 0;
        for(int i = 0; i < recommendations.length; i++){
            for(int j = i+1; j < recommendations.length; j++){
                if(recommendations[i] == null){
                    continue;
                }
                if(recommendations[i].equals(recommendations[j])){
                    recommendations[j] = null;
                    numberOfNulls++;
                    for(int k = j; k < recommendations.length-1; k++){
                        recommendations[k] = recommendations[k+1];
                        recommendations[k+1] = null;
                    }
                }
            }
        }
        if(numberOfNulls > 0){
            Restaurant[] tmp = new Restaurant[recommendations.length-numberOfNulls];
            for(int i = 0; i < tmp.length; i++){
                tmp[i] = recommendations[i];
            }
            recommendations = tmp;
        }
        return recommendations;
    }

    //Get a recently opened restaurant
    //GoogleSearch's getRecentlyOpenedRes() calls has a large string with delimiter between each restaurant.
    //Delimiter is "XA Potential RestaurantX".
    //When using split, array index 0 is always empty so restaurant array is -1 the split array.
    //For each set of restaurant information batch in token array, parse and get Restaurant object back.
    //If all is null, then return null
    //If there is Restaurant object, then use random to pick one.
    //Return a Restaurant or null if no restaurants.
    public static Restaurant findRecentlyOpened(String criteria){
        String searchResults = GoogleSearch.getRecentlyOpenedRes(criteria);
        String[] tokens = searchResults.split("XA Potential RestaurantX");
        Restaurant[] recentlyOpenedRes = new Restaurant[tokens.length-1];
        int numberOfRestaurants = 0;
        for(int i = 0; i < recentlyOpenedRes.length; i++) {
            Restaurant tmp = getSetInformation(tokens[i+1]);
            if(tmp == null){
                continue;
            }
            recentlyOpenedRes[numberOfRestaurants] = tmp;
            numberOfRestaurants++;
        }
        if(numberOfRestaurants == 0){
            return null;
        }
        else if(numberOfRestaurants == 1){
            return recentlyOpenedRes[0];
        }
        int randomIndex = (int)(Math.random()*2);
        return recentlyOpenedRes[randomIndex];
    }

    //Parse the string allInfo
    //allInfo should contain all the information for a single restaurant.
    //Make and set values in the Restaurant object to return
    //If nothing can be found from allInfo, then return null
    private static Restaurant getSetInformation(String allInfo){
        //if the allInfo has this, then it is not restaurant anymore
        if(allInfo.contains("Temporarily closed") || allInfo.contains("Permanently closed")){
            return null;
        }
        //this is sometimes before name
        allInfo = allInfo.replace("See outside", "");
        Restaurant restaurant = new Restaurant();
        String[] tokens = null;
        //split after where name comes up, try all possible appearances
        if(allInfo.contains("Website Directions Saved")){
            tokens = allInfo.split("Website Directions Saved");
        }
        else if(allInfo.contains("Directions Saved")){
            tokens = allInfo.split("Directions Saved");
        }
        else if(allInfo.contains("Website Directions")){
            tokens = allInfo.split("Website Directions");
        }
        else if(allInfo.contains("Website Saved")){
            tokens = allInfo.split("Website Saved");
        }
        else if(allInfo.contains("Directions")){
            tokens = allInfo.split("Directions");
        }
        else if(allInfo.contains("Website")){
            tokens = allInfo.split("Website");
        }
        else if(allInfo.contains("Saved")){
            tokens = allInfo.split("Saved");
        }
        if((tokens == null) || (tokens.length < 2)){
            return null;
        }
        //split string
        restaurant.setName(tokens[0].trim());
        //get number of reviews, ratings, and cost, split here
        //tokens[0] has ratings and reviews, and tokens[1] has the rest
        if(tokens[1].contains("Google reviews")) {
            tokens = tokens[1].split("Google reviews");
            String[] tmp = tokens[0].split("Save ");
            char[] ratingsAndReviews = tmp[1].replaceAll(" ", "").toCharArray();
            if (ratingsAndReviews.length > 1) {
                //means there is rating
                if (tmp[1].contains(".")) {
                    restaurant.setNumStars("" + ratingsAndReviews[0] + ratingsAndReviews[1] + ratingsAndReviews[2]);
                    if (ratingsAndReviews.length > 3) {
                        String numberOfReviews = "";
                        for (int i = 3; i < ratingsAndReviews.length; i++) {
                            numberOfReviews = numberOfReviews + ratingsAndReviews[i];
                        }
                        restaurant.setNumReviews(numberOfReviews);
                    }
                }
                //no rating, only reviews
                else {
                    String numberOfReviews = "";
                    for (int i = 0; i < ratingsAndReviews.length; i++) {
                        numberOfReviews = numberOfReviews + ratingsAndReviews[i];
                    }
                    restaurant.setNumReviews(numberOfReviews);
                }
            }
            //get cost
            restaurant.setDollars(tokens[1].substring(0, 7).replaceAll("[^$]", ""));
        }
        //get address
        if(!tokens[1].contains("Address: ")){
            return null;
        }
        tokens = tokens[1].split("Address: ");
        //tokens[1] has address and rest of info, including hours
        tokens = tokens[1].split("Hours: ");
        //tokens[0] has address, tokens[1] has hours and rest of info
        restaurant.setAddress(tokens[0]);
        String hoursInfo = tokens[1].split("Suggest an edit")[0];
        //clean hoursInfo here
        restaurant.setOperationalHours(hoursInfo);
        if(tokens[1].contains("Menu: ")) {
            tokens = tokens[1].split("Menu: ");
            int index = tokens[1].indexOf(".com");
            restaurant.setWebsite(tokens[1].substring(0,index+4));
        }
        if(tokens[1].contains("Phone: ")) {
            tokens = tokens[1].split("Phone: ");
            tokens = tokens[1].split(" ");
            if(tokens[0].contains("(")) {
                restaurant.setPhoneNumber("" + tokens[0] + " " + tokens[1]);
            }
        }
        return restaurant;
    }
}
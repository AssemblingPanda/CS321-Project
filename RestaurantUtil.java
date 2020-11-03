package com.github.huitk;
//RestaurantUtil class
public class RestaurantUtil {

    //Get restaurant recommendations
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
    public static Restaurant findRecentlyOpened(){
        Restaurant recentlyOpened = new Restaurant();
        return recentlyOpened;
    }
	
    //Parse allInfo and return Restaurant object with info
    private static Restaurant getSetInformation(String allInfo){
        return new Restaurant();
    }
}

package com.github.jmbidinger;


import java.util.ArrayList;
import java.util.List;

public class RollDice {
    // The maximum number of options that can be chosen from
    private static final int MAX_CHOICES = 30;

    public static final String NORMAL = "Normal";
    // Error codes
    public static final String NO_CHOICES = "No Choices";
    public static final String NO_START_BRACKETS = "No Starting Brackets";
    public static final String NO_END_BRACKETS = "No Ending Brackets";
    public static final String OTHER_BRACKET_ERROR = "Other Bracket Error";
    public static final String NULL_INPUT = "Null Input";
    public static final String INCORRECT_COMMAND = "Incorrect Command";
    public static final String TOO_MANY_CHOICES = "Too Many Choices";

    /**
     * This function will return a random integer between 1 and numSides
     *
     * @param numSides
     * @return dice
     */
    public static int rollDice(int numSides){
        if(numSides <= 0){
            return 0;
        }
        int dice = (int)(Math.random()*numSides) + 1;
        return dice;
    }

    /**
     * This function will take a formatted string as input, where the
     * format is !choose: [a1, a2, ..., an].
     * The output will be a list of strings where list.get(i) = ai.
     *
     * @param input
     * @return options
     */
    public static List<String> getOptions(String input){
        List<String> options = new ArrayList<String>();
        // Check input validity
        String returnCode = inputCheckGetOptions(input);

        // If the input code is not normal, return a list containing a flag (false) and
        // the return code to be used by TheFoodBot to give useful feedback to the user.
        if(returnCode != NORMAL){
            options.add("false");
            options.add(returnCode);
            return options;
        }
        // The input will have the format "!choose: [a1, a2,...,an]", so we need to remove the "!choose:"
        String trimmedInput = input.substring(input.indexOf(":")+1);

        StringBuilder name = new StringBuilder("");
        // Now we have to the the contents inside each set of brackets into a separate string and remove the brackets
        for(int i = 0; i < trimmedInput.length(); i++){
            if(trimmedInput.charAt(i) == '['){ // If the current character is '[', then skip this iteration
                continue;
            }
            if(trimmedInput.charAt(i) == ','){
                //The comma has been reached, so the current substring is complete
                // Add the current substring to the list, then result the substring for the next iteration
                if(i > 1){
                    options.add(name.toString());
                    name = new StringBuilder("");
                }
            }
            else if(trimmedInput.charAt(i) == ']'){
                // The final choice has been read in, so stop the loop and return the list of options
                options.add(name.toString());

                break;
            }
            else{ //The closing bracket has not been reached, so append the current character to the current substring
                name.append(trimmedInput.charAt(i));
            }

        }
        // Check to see if the number of options exceeds the maximum allowable number
        if(options.size() > MAX_CHOICES){
            options = new ArrayList<String>();
            options.add("false");
            options.add(TOO_MANY_CHOICES);
        }
        return options;
    }

    /**
     * This function takes in a list of strings (choices), and uses a random dice roll to select
     * a string in that list. That selected string will then be returned.
     * @param options
     * @return choice
     */
    public static String rollForChoice(List<String> options){
        String choice = null;
        int dice;
        if(options == null || options.size() > MAX_CHOICES){
            return choice;
        }
        if(options.size() > 1){
            if(options.get(0) == "false"){
                return choice;
            }
        }
        dice = rollDice(options.size());
        if(dice < 1){
            return choice;
        }
        if(options.size() < 1){ // The list has only 1 value in it, so that will automatically be returned
            choice = options.get(0);
        }
        else { // Return the element in the list located at the random index
            choice = options.get(dice - 1);
        }
        return choice;
    }

    /**
     * This function takes in a string as input. This string will contain an integer (ex. 1234).
     * This function will convert that string of numbers into an integer, then return that number.
     * @param str
     * @return number
     */
    public static int getNumber(String str){
        int number = -1;
        boolean containsNumber = false;
        if(str == null || str.isEmpty()){
            return number;
        }
        StringBuilder numString = new StringBuilder();
        // Traverse the string, and take the characters corresponding to numbers and append
        // them into a string
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                numString.append(c);
                containsNumber = true;
            }
        }
        // If the string is non-empty (it contains a number), then get the integer value of the string
        if(containsNumber){
            number = Integer.valueOf(numString.toString());
        }
        return number;
    }

    /**
     * This function controls the amount of time the bot has to wait to respond once receiving
     * a command. This is designed in order to help the bot feel more human, as near instantaneous results
     * can make the conversational nature of the bot seem too mechanical.
     *
     * @param length
     */
    protected static void wait(int length){
        if(length <= 0){
            return;
        }
        if(length > 3000){
            length = 1500; // The max allowable length will be 1500 milliseconds
        }
        try
        {
            Thread.sleep(length);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * This method checks to see whether or not the input string matches the format required
     * by TheFoodBot. It returns an exit code based on the results. If no errors are found, it
     * returns 'Normal' as the exit code. Otherwise, checks are performed to determine what the
     * specific errors are, so that information may be displayed to the user in order to improve their
     * experience by making the bot's feedback more helpful.
     *
     * @param input
     * @return exitCode
     */
    protected static String inputCheckGetOptions(String input){
        String exitCode = NORMAL;

        if(input == null){
            return NULL_INPUT;
        }
        else if(input.length() == 0){
            return NO_CHOICES;
        }
        else if(!input.contains("!choose:")){
            return INCORRECT_COMMAND;
        }
        if(input.length() <= input.indexOf(":")+1){
            return NO_CHOICES;
        }

        boolean hasData = false;
        boolean hasStartBracket = false;
        boolean hasEndBracket = false;

        // Check to see if there are starting brackets, ending brackets,
        // and whether or not there is anything in between. If the string does not
        // contain any characters other than ';', ' ', '[', and ']' after the !choose:
        // then the string will give the exit code for no options.
        for(int i = input.indexOf(":")+1; i < input.length(); i++){
            if(input.charAt(i) != ':' && input.charAt(i) != ' ' && input.charAt(i) != '['
            && input.charAt(i) != ']'){
                hasData = true;
            }
            if(input.charAt(i) == '['){
                hasStartBracket = true;
            }
            if(input.charAt(i) == ']'){
                hasEndBracket = true;
            }
        }
        if(!hasData){
            return NO_CHOICES;
        }
        if(!hasStartBracket && !hasEndBracket){
            return OTHER_BRACKET_ERROR;
        }
        if(!hasStartBracket) {
            return NO_START_BRACKETS;
        }
        if(!hasEndBracket){
            return NO_END_BRACKETS;
        }
        return exitCode;
    }

    /**
     * This is an accessor which returns the maximum allowable number of choices for the rollForChoice method.
     * @return MAX_CHOICES
     */
    public static int getMaxChoices(){
        return MAX_CHOICES;
    }
}

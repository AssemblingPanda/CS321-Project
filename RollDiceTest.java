package com.github.jmbidinger;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RollDiceTest {

    // Constants from the RollDice class to use
    public static final String NORMAL = RollDice.NORMAL;
    // Error codes
    public static final String NO_CHOICES = RollDice.NO_CHOICES;
    public static final String NO_START_BRACKETS = RollDice.NO_START_BRACKETS;
    public static final String NO_END_BRACKETS = RollDice.NO_END_BRACKETS;
    public static final String OTHER_BRACKET_ERROR = RollDice.OTHER_BRACKET_ERROR;
    public static final String NULL_INPUT = RollDice.NULL_INPUT;
    public static final String INCORRECT_COMMAND = RollDice.INCORRECT_COMMAND;
    public static final String TOO_MANY_CHOICES = RollDice.TOO_MANY_CHOICES;

    @Test
    void rollDice() {
        // Checks to see that a dice with 0 sides returns a 0
        assertEquals(0, RollDice.rollDice(0));

        // Checks to see that the value of the dice roll is always greater than 0
        // and less than or equal to the number of sides
        for(int i = 0; i < 500; i++) {
            int dice = RollDice.rollDice(50);
            boolean valid = (dice > 0) && (dice <= 50);
            assertEquals(true, valid);
        }
    }

    @Test
    void getOptions() {
        List<String> theOptions = RollDice.getOptions("!choose: [option1, option2, option3]");
        List<String> expected = new ArrayList<String>();
        expected.add(" option1");
        expected.add(" option2");
        expected.add(" option3");
        // Check that the options match the expected valid input
        assertEquals(expected, theOptions);
        expected = new ArrayList<String>();
        int maxChoices = 30;

        // Populate a list with 31 entries (1 more than is allowed) to see that the output is not flagged as invalid
        // until the size of the list grows larger than what is allowed
        for(int i = 0; i < maxChoices+1; i++){
            expected.add(""+i);
            theOptions = RollDice.getOptions("!choose: ["+expected.toString()+"]");
            if(i < maxChoices){
                // The size of the list is still valid, so the options should not contain any error flags
                assertEquals(true, (theOptions.size() != 2 || !theOptions.get(0).equals("false")));
            }
        }
        // The list has been populated with 31 elements, so theOptions should have 2 elements
        // The first element is "false"
        // The second element is the constant RollDice.TOO_MANY_CHOICES
        assertEquals(true, (theOptions.size() == 2 && expected.size() != 2));
        if(theOptions.size() >= 2) {
            assertEquals(true, theOptions.get(0).equals("false"));
            assertEquals(true, theOptions.get(1).equals(TOO_MANY_CHOICES));
        }
    }

    @Test
    void rollForChoice() {
        List<String> options = null;
        assertEquals(null, RollDice.rollForChoice(options));

        options = new ArrayList<String>();
        options.add(" option1");
        options.add(" option2");
        options.add(" option3");
        String choice = RollDice.rollForChoice(options);
        boolean found = options.contains(choice);
        // The list of options contains the choice that was made
        assertEquals(true, found);

        options = new ArrayList<String>();
        for(int i = 0; i < 31; i++){
            options.add(" "+i);
            if(i < 30) {
                // The choice should be contained in the list of available options
                assertEquals(true, options.contains(RollDice.rollForChoice(options)));
            }
        }
        // There are too many elements to choose from, so the choice should be null
        assertEquals(null, RollDice.rollForChoice(options));
    }

    @Test
    void getNumber() {
        int number = 57;
        String str = "!roll d"+number;
        // The getNumber method was correctly able to identify the number inside of the string
        assertEquals(number, RollDice.getNumber(str));
    }


    @Test
    void inputCheckGetOptions() {
        String input = null; // Should give the NULL_INPUT flag
        assertEquals(NULL_INPUT, RollDice.inputCheckGetOptions(input));

        input = ""; // Should give the NO_CHOICES flag
        assertEquals(NO_CHOICES, RollDice.inputCheckGetOptions(input));

        input = "this is not a valid input"; // Should give the INCORRECT_COMMAND flag
        assertEquals(INCORRECT_COMMAND, RollDice.inputCheckGetOptions(input));

        input = "!choose: "; // Should give the NO_CHOICES flag
        assertEquals(NO_CHOICES, RollDice.inputCheckGetOptions(input));

        input = "!choose: [           ]"; // Should give the NO_CHOICES flag
        assertEquals(NO_CHOICES, RollDice.inputCheckGetOptions(input));

        input = "!choose: 1, 2, 3"; // Should give the OTHER_BRACKET_ERROR flag
        assertEquals(OTHER_BRACKET_ERROR, RollDice.inputCheckGetOptions(input));

        input = "!choose: [1, 2, 3"; // Should give the NO_END_BRACKETS flag
        assertEquals(NO_END_BRACKETS, RollDice.inputCheckGetOptions(input));

        input = "!choose: 1, 2, 3]"; // Should give the NO_START_BRACKETS flag
        assertEquals(NO_START_BRACKETS, RollDice.inputCheckGetOptions(input));

        int max = 30;
        input = "!choose: [";
        while(max > 0){
            input += max+"";
            max--;
            if(max > 0){
                input += ", ";
            }
        }
        input += "]";
        // Should give the NORMAL flag
        assertEquals(NORMAL, RollDice.inputCheckGetOptions(input));

        input = "!choose: [1, 2, 3]"; // Should give the NORMAL flag
        assertEquals(NORMAL, RollDice.inputCheckGetOptions(input));
    }

    @Test
    void getMaxChoices() {
        // Checks to see that the maximum allowed number of choices is 30
        assertEquals(30, RollDice.getMaxChoices());
    }
}
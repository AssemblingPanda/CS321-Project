package com.github.jmbidinger;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import java.util.concurrent.atomic.AtomicReference;

public class TheFoodBot {
    public static void main(String[] args) {

        String dice = "!roll dice";
        String rollSomeDice = "!roll some dice";
        String rollNSided = "!roll D";
        String helpPrompt = "It seems like you are calling for us, but we cannot do whatever it is that you are asking of us.\n" +
                "Refer to our help menu by typing \"!Help Menu\" to see what commands are available.";

        // TheFoodBot's token
        String token = "NzY3OTU0NjgzNjQxNzI0OTU4.X45biA.eZt6cUF59N0JN3cItm73elGVCDY";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        // The output message will be changed according to the inputs from the user
        AtomicReference<String> outputMessage = new AtomicReference<>("");

        api.addMessageCreateListener(event -> {
            String messageOriginal = event.getMessageContent();
            String messageLC = messageOriginal.toLowerCase();

            if(event.getMessageAuthor().isBotUser() || (messageLC.length() < 1) || (messageLC.charAt(0) != '!')){
                outputMessage.set("");
            }

            else if (messageLC.contains("!rr: ")) { // RR placeholder for Restaurant Recommendation
                messageLC = messageLC.replaceAll("!rr: \\[", "").replaceAll("\\[", "").replaceAll("]", "");
                event.getChannel().sendMessage(messageLC);
                String recs = GoogleSearch.getResRec(messageLC);
                //RestaurantRecommendations.getRes(recs);
                event.getChannel().sendMessage("Here are some restaurant recommendations:");
                event.getChannel().sendMessage(recs);
            }

            else if (messageLC.contains("!rn: ")) { // RN placeholder for Restaurant Notification
                event.getChannel().sendMessage("Empty Right Now");
            }

            else if(messageLC.contains("!choose:")){
                // wait(100);
                event.getChannel().sendMessage("https://thumbs.gfycat.com/SecondTartCygnet-size_restricted.gif");
                String choice = RollDice.rollForChoice(RollDice.getOptions(messageOriginal));
                if(choice == null){
                    event.getChannel().sendMessage(helpPrompt);
                }
                else {
                    // wait(100);
                    outputMessage.set(choice + ", I choose you!");
                }
            }

            else if(messageLC.contains(dice.toLowerCase()) || messageLC.contains(rollSomeDice.toLowerCase())
                    || messageLC.contains(rollNSided.toLowerCase())
                    || messageLC.contains("roll a d".toLowerCase()) || messageLC.contains("roll me some dice")){
                int diceRoll;
                int nSides;
                if((messageLC.contains(rollNSided.toLowerCase()) && !messageLC.contains(dice.toLowerCase()))
                        || messageLC.contains("roll a d".toLowerCase())){
                    nSides = RollDice.getNumber(messageLC);
                    if(nSides > 0){
                        diceRoll = RollDice.rollDice(nSides);
                        outputMessage.set(diceRoll+"");
                    }
                    else{
                        outputMessage.set("Sorry, I can't roll dice with only "+nSides+" sides");
                    }
                }

            }

            else if (event.getMessageContent().equalsIgnoreCase("!Help Menu")) {
                event.getChannel().sendMessage("!rr: [cuisine/type of restaurant] [Zip Code]\n" +
                        "!rn: [ZIP Code]\n" +
                        "!choose: [name1], [name2], [name3],...,[name30]");
            }

            else {
                event.getChannel().sendMessage(helpPrompt);
            }

            //  wait(50);
            event.getChannel().sendMessage(String.valueOf(outputMessage));
        });
    }
}
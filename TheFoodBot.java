package com.github.jmbidinger;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.Message;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class TheFoodBot {
    public static void main(String[] args) {

        String dice = "!roll dice";
        String rollSomeDice = "!roll some dice";
        String rollNSided = "!roll D";
        String helpPrompt = "It seems like you are calling for us, but we cannot do whatever it is that you are asking of us.\n" +
                "Refer to our help menu by typing \"!Help Menu\" to see what commands are available.";
        String helpMenu = "!rr: [cuisine/type of restaurant] [Zip Code]\n" +
                "!rn: [ZIP Code]\n" +
                "!choose: [name1] [name2] [name3]...[name30]";

        // TheFoodBot's token
        String token = "NzU4MDM4OTU5MTAzMjc5MTk2.X2pIyw.yG10-910eOE6QF31EkNyxvJ32L4";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        // The output message will be changed according to the inputs from the user
        AtomicReference<String> outputMessage = new AtomicReference<>("");

        // Sends a notification to the general chat channel upon startup
        if(api.hasAllUsersInCache()){
            api.getServerById("747981812407468042").get().getTextChannelsByName("general").get(0).sendMessage("Would you like to get some restaurant notifications? Enter !rn: [ZIP code]");
        }

        api.addMessageCreateListener(event -> {
            String messageOriginal = event.getMessageContent();
            String messageLC = messageOriginal.toLowerCase();

            if(event.getMessageAuthor().isBotUser() || (messageLC.length() < 1) || (messageLC.charAt(0) != '!')){
                outputMessage.set("");
            }

            else if (messageLC.contains("!rr: ")) { // RR placeholder for Restaurant Recommendation
                messageLC = messageLC.replaceAll("!rr: \\[", "").replaceAll("\\[", "").replaceAll("]", "");
                Restaurant [] parsedRecs = RestaurantUtil.getRecommendations(messageLC);
                if(parsedRecs == null){
                    event.getChannel().sendMessage("No recommendable restaurants found in your area :(\n");
                }
                String ret = "";
                for(int i = 0; i < parsedRecs.length; i++){
                    ret += parsedRecs[i].toString() + "\n";
                }
                if(ret.equals("")){
                    ret = "No recommendable restaurants found in your area :(\n";
                }
                else{
                    ret = "Here are some restaurant recommendations:\n" + ret;
                }
                event.getChannel().sendMessage(ret);
            }

            else if (messageLC.contains("!rn: ")) { // RN placeholder for Restaurant Notification
                messageLC = messageLC.replaceAll("!rn: \\[", "").replaceAll("\\[","").replaceAll("]", "");
                Restaurant newRestaurant = RestaurantUtil.findRecentlyOpened(messageLC);
                String ret = "";
                if(newRestaurant == null){
                    ret = "No recently opened restaurants found in your area :<\n";
                }
                else{
                    ret = "Here is one recently opened restaurant:\n" + newRestaurant.toString();
                }
                event.getChannel().sendMessage(ret);
            }

            else if(messageLC.contains("!choose:")){
                // wait(100);
                Message toDelete = null;
                List<String> options = RollDice.getOptions(messageOriginal);
                String choice = RollDice.rollForChoice(options);
                if(choice == null){
                    if(options != null && options.size() > 0){
                        // The user entered more than the allowed number of choices
                        event.getChannel().sendMessage("The maximum number of restaurants you can enter is "+RollDice.getMaxChoices()
                                +"\nPlease try again using "+RollDice.getMaxChoices()+" restaurants or less");
                    }
                    else {
                        if(options != null && options.size() == 0){
                            try{
                                toDelete = event.getChannel().sendMessage("https://lh3.googleusercontent.com/" +
                                        "proxy/CPvahwr0k9vNpbwwxt8FE7BrpkAxsfYE5U-Z5GL5N8HTXk1vSwWhKu6sFdbJjzhL12HF0Y" +
                                        "ZcrKByNywAU_zWMtwzxxaxSREsL2JQEV39RuDailtOY_rP14BMhgm3NrvoGjGFpWfR5cbE").get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            event.getChannel().sendMessage("It looks like there are not any restaurants for me to choose from. " +
                                    "Please try again or type \"!Help Menu\" for more information");
                            RollDice.wait(3000);
                            event.getChannel().deleteMessages(toDelete);
                        }
                        else {
                            event.getChannel().sendMessage(helpPrompt);
                        }
                    }
                }
                else {
                    try {
                        toDelete = event.getChannel().sendMessage("https://thumbs.gfycat.com/SecondTartCygnet-size_restricted.gif").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    RollDice.wait(3000);
                    event.getChannel().deleteMessages(toDelete);
                    RollDice.wait(800);
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
                event.getChannel().sendMessage(helpMenu);
            }

            else {
                event.getChannel().sendMessage(helpPrompt);
            }

            //  wait(50);
            event.getChannel().sendMessage(String.valueOf(outputMessage));
        });
    }

}

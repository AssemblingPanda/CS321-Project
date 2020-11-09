package com.github.jmbidinger;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class TheFoodBot {
    public static void main(String[] args) {

        String dice = "!roll dice";
        String rollSomeDice = "!roll some dice";
        String rollNSided = "!roll D";
        String restaurantRestrictions = "buffet, asian, bar, ayce, sports bar, pub, korean, steak, barbeque, " +
                "panda express, chinese, bagels, ice cream, pizza, italian, irish, afghani, mediterranean";
        String helpPrompt = "It seems like you are calling for us, but we cannot do whatever it is that you are asking of us.\n" +
                "Refer to our help menu by typing \"!Help Menu\" to see what commands are available.";
        String helpMenu = "Here is the help menu:\n\n" +
                "These are the commands that we have available for you:\n" +
                "!rec Cuisine/Types of Restaurant, ZIP Code\n" +
                "!new ZIP Code\n" +
                "!choose: [name1, name2, name3, ..., name30]\n\n" +
                "Choose from these Types of Restaurant:\n" +restaurantRestrictions;

        String notificationsPrompt = "Hi! Would you like to see a recently opened restaurant? Enter !new ZIP code\n"+
                "If not, then check out our help menu to get you started if you are new! Enter !help menu";
        String messageNoRecent = "No recently opened restaurants found in your area :disappointed:\n";
        String messageNoRecommend = "No recommendable restaurants found in your area :disappointed:\n";
        String messageNoOptions = "It looks like there are not any restaurants for me to choose from. " +
                "Please try again or type \"!Help Menu\" for more information";
        // Link to a humorous image to be displayed when the user has not entered enough information to select a choice from
        String imageLinkNoOptions = "https://lh3.googleusercontent.com/proxy/mKojzvEiUQN-hGlZJUCEY_2PLlbg7J0GOL28DvL3S7sCZCefJgRpvkzl2a9xJor5JdDryTYmzJ-PxJRaGThHM1H-yQp_y21CUfzEiaX2-YCpRLfLb5UJuba4PKXctmL4baEOEHJT9O3f";
        // Link to a gif of rolling dice to be displayed while the bot is making a selection
        String imageLinkDice = "https://thumbs.gfycat.com/SecondTartCygnet-size_restricted.gif";


        // TheFoodBot's token:
        //String token = "NzY3OTU0NjgzNjQxNzI0OTU4.X45biA.eZt6cUF59N0JN3cItm73elGVCDY";
        // My bot's token:
        String token = "NzU4MDM4OTU5MTAzMjc5MTk2.X2pIyw.yG10-910eOE6QF31EkNyxvJ32L4";
        //String token = "NzU4MDA2ODYxMTA1NTk0NDA5.X2oq5g.Wr6jRDZ_Zer4wbCOzPRbUxupqx4";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        // The output message will be changed according to the inputs from the user
        AtomicReference<String> outputMessage = new AtomicReference<>("");

        // For every channel that this bot is in:
        // Create a new "restaurant-notifications" channel, if channel already exists, do not create a channel
        // Send in a notification to the "restaurant-notifications" channel
        if(api.hasAllUsersInCache()){
            Server [] servers = api.getServers().toArray(new Server[api.getServers().size()]);
            for(int i = 0; i < servers.length; i++){
                String serverId = servers[i].getIdAsString();
                Server ithServer = api.getServerById(serverId).get();
                if(ithServer.canYouCreateChannels()) {
                    List<ServerTextChannel> textChannels = ithServer.getTextChannelsByNameIgnoreCase("restaurant-notifications");
                    if(textChannels.size() > 0) {
                        textChannels.get(0).sendMessage(notificationsPrompt);
                    } else {
                        ithServer.createTextChannelBuilder().setName("restaurant notifications").create().join().sendMessage(notificationsPrompt);
                    }
                }
            }
        }

        api.addMessageCreateListener(event -> {
            String messageOriginal = event.getMessageContent();
            String messageLC = messageOriginal.toLowerCase();

            if(event.getMessageAuthor().isBotUser() || (messageLC.length() < 1) || (messageLC.charAt(0) != '!')){
                outputMessage.set("");
            }

            else if (messageLC.matches("[!][r][e][c][ ][a-zA-Z0-9 ]+[,][ ][0-9]+")) {
                // Get the string into the proper format to be parsed
                messageLC = messageLC.replace("!rec ", "").replace(",", "");
                // Separate the restaurant type from the zipcode to check whether or not the restaurant type
                // is in the list allowed by the user restrictions.
                String restaurantType = messageLC.substring(0, messageLC.lastIndexOf(' '));
                // The type of cuisine/restaurant entered by the user is not one of the allowed options,
                // so display a useful and detailed explanation to the user
                if (!restaurantRestrictions.contains(restaurantType)) {
                    // Find the first latter of the restaurant type, which is in lowercase, and convert it to upper case
                    // so that it looks better to the user when it is displayed to the screen
                    char firstLetter = restaurantType.charAt(0);
                    restaurantType = restaurantType.replace(firstLetter, (char)(firstLetter-32));
                    outputMessage.set("Unfortunately, "+restaurantType+" is not one of the recognized types of cuisine. " +
                            "You can search for restaurants from the following list:\n" + restaurantRestrictions);
                }
                else {
                    Restaurant[] parsedRecs = RestaurantUtil.getRecommendations(messageLC);
                    if (parsedRecs == null) {
                        event.getChannel().sendMessage(messageNoRecommend);
                    }
                    String ret = "";
                    for (int i = 0; i < parsedRecs.length; i++) {
                        ret += parsedRecs[i].toString() + "\n";
                    }
                    if (ret.equals("")) {
                        ret = messageNoRecommend;
                    } else {
                        ret = "Here are some restaurant recommendations:\n" + ret;
                    }
                    event.getChannel().sendMessage(ret);
                }
            }

            else if (messageLC.matches("[!][n][e][w][ ][0-9]+")) {
                messageLC = messageLC.replace("!new ", "");
                Restaurant newRestaurant = RestaurantUtil.findRecentlyOpened(messageLC);
                String ret = "";
                if(newRestaurant == null){
                    ret = messageNoRecent;
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
                                // Briefly display a funny image to the screen indicating that
                                // the user did not enter enough information for the bot to make a selection
                                toDelete = event.getChannel().sendMessage(imageLinkNoOptions).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            event.getChannel().sendMessage(messageNoOptions);
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
                        toDelete = event.getChannel().sendMessage(imageLinkDice).get();
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
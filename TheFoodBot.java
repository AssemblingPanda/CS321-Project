package com.github.jmbidinger;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class TheFoodBot {
    public static void main(String[] args) {

        String dice = "!roll dice";
        String rollSomeDice = "!roll some dice";
        String rollNSided = "!roll D";

        String restaurantRestrictions = "\ncafe, buffet, asian, bar, ayce, sports bar, pub, korean, steak, barbeque, " +
                "chinese, bagels, ice cream, pizza, italian, irish, afghani, mediterranean, turkish, seafood, american, " +
                "sushi, donut, german, vietnamese, spanish, diner, sandwich, fine dining, mexican, ethiopian, thai, " +
                "indian, nepalese, uzbeki, jewish, polish, lebanese, jamaican, georgian, french, greek, indonesian, " +
                "japanese, kurdish, pennsylvania dutch, middle eastern, pakistani, malay, russian, soul, filipino, " +
                "british, egyptian, danish, caribbean, bubble tea, cajun, brazilian, bangladeshi, azerbaijani, " +
                "armenian, argentinian, texan, persian, peruvian, portuguese, romanian, " +
                "serbian, slovak, somali, taiwanese, tex mex, udupi, ukranian.\n";

        String helpPrompt = "It seems like you are calling for us, but we cannot do whatever it is that you are asking of us.\n" +
                "Refer to our help menu by typing \"!help\" to see what commands are available.";

        String helpMenu = "Here is the help menu:\n\n" +
                "These are the commands that we have available for you:\n" +
                "Want some restaurant recommendations? Enter: !rec Type of Cuisine or Restaurant, ZIP Code\n" +
                "Choose from the \"Types of Cuisine or Restaurant\" listed here:\n" + restaurantRestrictions +
                "\nWant to see if there is a recently opened restaurant near you? Enter: !new ZIP Code\n" +
                "Want us to pick a place for you? Enter: !choose: [option1, option2, ..., option30]\n";

        String notificationsPrompt = "Hi! Would you like to see a recently opened restaurant? Enter !new ZIP code\n"+
                "If not, then check out our help menu to get you started if you are new! Enter !help menu";

        String messageNoRecent = "No recently opened restaurants found in your area :disappointed:\n";
        String messageNoRecommend = "No recommendable restaurants found in your area :disappointed:\n";
        String messageNoOptions = "It looks like there are not any restaurants for me to choose from. " +
                "Please try again or type \"!help\" for more information";

        // Link to a gif of rolling dice to be displayed while the bot is making a selection
        String imageLinkDice = "https://thumbs.gfycat.com/SecondTartCygnet-size_restricted.gif";

        String messageStartBracketError = "It looks like you did not use a bracket to start your list of choices." +
                "The !choose command must have the following format\n\t!choose: [option1, ..., option30]";
        String messageEndBracketError = "It looks like you did not use a bracket to end your list of choices." +
                "The !choose command must have the following format\n\t!choose: [option1, ..., option30]";
        String messageOtherBracketError = "It looks like you did not use a bracket to start or end your list of choices." +
                "The !choose command must have the following format\n\t!choose: [option1, ..., option30]";

        //TheFoodBot's token:
        String token = "NzY3OTU0NjgzNjQxNzI0OTU4.X45biA.eZt6cUF59N0JN3cItm73elGVCDY";

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        // The output message will be changed according to the inputs from the user
        AtomicReference<String> outputMessage = new AtomicReference<>("");

        // Allows easy creation of messages to send to user, while making it look appealing to the user
        MessageBuilder msg = new MessageBuilder();

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
                        msg.setEmbed(new EmbedBuilder()
                                .setTitle("New notification")
                                .setDescription(notificationsPrompt)
                                .setColor(Color.WHITE))
                                .send(textChannels.get(0));
                        //textChannels.get(0).sendMessage(notificationsPrompt);
                    } else {
                        msg.setEmbed(new EmbedBuilder()
                                .setTitle("New notification")
                                .setDescription(notificationsPrompt)
                                .setColor(Color.WHITE))
                                .send(ithServer.createTextChannelBuilder().setName("restaurant notifications").create().join());
                        //ithServer.createTextChannelBuilder().setName("restaurant notifications").create().join().sendMessage(notificationsPrompt);
                    }
                }
            }
        }

        api.addMessageCreateListener(event -> {
            String messageOriginal = event.getMessageContent();
            String messageLC = messageOriginal.toLowerCase();

            if(event.getMessageAuthor().isBotUser() || (messageLC.length() < 1) || (messageLC.charAt(0) != '!')){
                // Do nothing
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
                    msg.setEmbed(new EmbedBuilder()
                            .setTitle("Unrecognized type of restaurant or cuisine")
                            .setDescription("Unfortunately, "+restaurantType+" is not one of the recognized types of restaurants/cuisines. " +
                                    "You can search for restaurants using the following list:\n" + restaurantRestrictions)
                            .setColor(Color.RED))
                            .send(event.getChannel());
                }
                else {
                    Restaurant[] parsedRecs = RestaurantUtil.getRecommendations(messageLC);
                    if (parsedRecs == null) {
                        msg.setEmbed(new EmbedBuilder()
                                .setDescription(messageNoRecommend)
                                .setColor(Color.YELLOW))
                                .send(event.getChannel());
                    }
                    else {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle("A restaurant recommendation:")
                                .setColor(Color.CYAN);
                        for (int i = 0; i < parsedRecs.length; i++) {
                            msg.setEmbed(embed
                                    .setDescription(parsedRecs[i].toString()))
                                    .send(event.getChannel());
                        }
                    }
                }
            }

            else if (messageLC.matches("[!][n][e][w][ ][0-9]+")) {
                messageLC = messageLC.replace("!new ", "");
                Restaurant newRestaurant = RestaurantUtil.findRecentlyOpened(messageLC);
                if(newRestaurant == null){
                    msg.setEmbed(new EmbedBuilder()
                            .setDescription(messageNoRecent)
                            .setColor(Color.YELLOW))
                            .send(event.getChannel());
                }
                else{
                    msg.setEmbed(new EmbedBuilder()
                            .setTitle("A recently opened restaurant:")
                            .setDescription(newRestaurant.toString())
                            .setColor(Color.CYAN))
                            .send(event.getChannel());
                }
            }

            else if(messageLC.contains("!choose:")) {
                Message toDelete = null;
                List<String> options = RollDice.getOptions(messageOriginal);
                String choice = RollDice.rollForChoice(options);
                boolean skipToEnd = false;
                // Check the results for errors
                if (options == null) {
                    msg.setEmbed(new EmbedBuilder()
                            .setTitle("Incorrect command?")
                            .setColor(Color.RED)
                            .setDescription(helpPrompt))
                            .send(event.getChannel());
                } else if (options.size() == 2) {
                    if (options.get(0).equalsIgnoreCase("false")) {
                        // This signals that an error was detected, so it will go through the
                        // if statements to determine what message to send to the user
                        skipToEnd = true;
                        if (options.get(1) == RollDice.NO_CHOICES) {
                            msg.setEmbed(new EmbedBuilder()
                                    .setTitle("No choices?")
                                    .setColor(Color.RED)
                                    .setDescription(messageNoOptions))
                                    .send(event.getChannel());
                        } else if (options.get(1) == RollDice.NULL_INPUT) {
                            msg.setEmbed(new EmbedBuilder()
                                    .setTitle("Incorrect command?")
                                    .setColor(Color.RED)
                                    .setDescription(helpPrompt))
                                    .send(event.getChannel());
                        } else if (options.get(1) == RollDice.NO_START_BRACKETS) {
                            msg.setEmbed(new EmbedBuilder()
                                    .setTitle("Incorrect command?")
                                    .setColor(Color.RED)
                                    .setDescription(messageStartBracketError))
                                    .send(event.getChannel());
                        } else if (options.get(1) == RollDice.NO_END_BRACKETS) {
                            msg.setEmbed(new EmbedBuilder()
                                    .setTitle("Incorrect command?")
                                    .setColor(Color.RED)
                                    .setDescription(messageEndBracketError))
                                    .send(event.getChannel());
                        } else if (options.get(1) == RollDice.OTHER_BRACKET_ERROR) {
                            msg.setEmbed(new EmbedBuilder()
                                    .setTitle("Incorrect command?")
                                    .setColor(Color.RED)
                                    .setDescription(messageOtherBracketError))
                                    .send(event.getChannel());
                        }
                        else if(options.get(1) == RollDice.TOO_MANY_CHOICES){
                            msg.setEmbed(new EmbedBuilder()
                                    .setDescription("The maximum number of restaurants you can enter is "+RollDice.getMaxChoices()
                                            +"\nPlease try again using "+RollDice.getMaxChoices()+" restaurants or less")
                                    .setColor(Color.RED))
                                    .send(event.getChannel());
                        } else {
                            // Incorrect Command
                            msg.setEmbed(new EmbedBuilder()
                                    .setTitle("Incorrect command?")
                                    .setColor(Color.RED)
                                    .setDescription(helpPrompt))
                                    .send(event.getChannel());
                        }
                    }
                }
                if (!skipToEnd) {
                    // An error was not detected previously with "options", so now check for errors with "choice"
                    if (choice == null) {
                        if (options != null && options.size() > 0) {
                            // The user entered more than the allowed number of choices
                            msg.setEmbed(new EmbedBuilder()
                                    .setDescription("The maximum number of restaurants you can enter is "+RollDice.getMaxChoices()
                                            +"\nPlease try again using "+RollDice.getMaxChoices()+" restaurants or less")
                                    .setColor(Color.RED))
                                    .send(event.getChannel());
                        } else {
                            if (options != null && options.size() == 0) {
                                msg.setEmbed(new EmbedBuilder()
                                        .setTitle("No choices?")
                                        .setColor(Color.RED)
                                        .setDescription("Data data data! I cannot make bricks without clay" + messageNoOptions))
                                        .send(event.getChannel());
                            } else {
                                msg.setEmbed(new EmbedBuilder()
                                        .setTitle("Incorrect command?")
                                        .setColor(Color.RED)
                                        .setDescription(helpPrompt))
                                        .send(event.getChannel());
                            }
                        }
                    } else {
                        try {
                            // Display a gif of some rolling dice while the user waits for the results
                            toDelete = event.getChannel().sendMessage(imageLinkDice).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        RollDice.wait(3000);
                        // Remove the dice gif from the screen
                        event.getChannel().deleteMessages(toDelete);
                        RollDice.wait(1000);
                        msg.setEmbed(new EmbedBuilder()
                                .setColor(Color.MAGENTA)
                                .setDescription(choice + ", I choose you!"))
                                .send(event.getChannel());
                    }
                }
            }

            else if(messageLC.contains(dice.toLowerCase()) || messageLC.contains(rollSomeDice.toLowerCase())
                    || messageLC.contains(rollNSided.toLowerCase())
                    || messageLC.contains("roll a d".toLowerCase()) || messageLC.contains("roll me some dice")){
                // The user has asked to receive a numerical number from 1 to the number of sides entered
                // This is given as an alternative to "!choose:" if the user wants to use this to make their
                // decisions instead of typing in the names of each restaurant
                int diceRoll;
                int nSides;
                if((messageLC.contains(rollNSided.toLowerCase()) && !messageLC.contains(dice.toLowerCase()))
                        || messageLC.contains("roll a d".toLowerCase())){
                    nSides = RollDice.getNumber(messageLC);
                    if(nSides > 0){
                        diceRoll = RollDice.rollDice(nSides);
                        msg.setEmbed(new EmbedBuilder()
                                .setColor(Color.MAGENTA)
                                .setDescription(diceRoll+""))
                                .send(event.getChannel());
                    }
                    else{
                        msg.setEmbed(new EmbedBuilder()
                                .setColor(Color.RED)
                                .setDescription("Sorry, I can't roll dice with only "+nSides+" sides"))
                                .send(event.getChannel());
                    }
                }
            }

            else if (event.getMessageContent().equalsIgnoreCase("!help")) {
                // The user has requested to see the help menu
                msg.setEmbed(new EmbedBuilder()
                        .setTitle("Here is our help menu:")
                        .setColor(Color.YELLOW)
                        .setDescription(helpMenu))
                        .send(event.getChannel());
            }

            else {
                msg.setEmbed(new EmbedBuilder()
                        .setTitle("Incorrect command?")
                        .setColor(Color.RED)
                        .setDescription(helpPrompt))
                        .send(event.getChannel());
            }
        });
    }
}

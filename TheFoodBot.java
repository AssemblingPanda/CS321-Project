package com.github.assemblingpanda;

import com.github.jmbidinger.RollDice;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import java.util.concurrent.atomic.AtomicReference;

public class TheFoodBot {
    public static void main(String[] args) {

        String dice = "!roll dice";
        String rollSomeDice = "!roll some dice";
        String rollNSided = "!roll D";

        // Insert the bot's token here
        String token = "NzY3OTU0NjgzNjQxNzI0OTU4.X45biA.eZt6cUF59N0JN3cItm73elGVCDY";
		
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
		
        api.addMessageCreateListener(event -> {
            String messageOriginal = event.getMessageContent();
            String message = messageOriginal.toLowerCase();
			
            if(event.getMessageAuthor().isBotUser() || (message.length() < 1) || (message.charAt(0) != '!')){
                message = "";
                outputMessage.set("");
            }
            else if(message.contains("!choose:")){
                // wait(100);
                event.getChannel().sendMessage("https://thumbs.gfycat.com/SecondTartCygnet-size_restricted.gif");
                String choice = RollDice.rollForChoice(RollDice.getOptions(messageOriginal));
                // wait(100);
                outputMessage.set(choice + ", I choose you!");
                //event.getChannel().sendMessage(choice+", I choose you!");
                message = "";
            }
			
            else if(message.contains(dice.toLowerCase()) || message.contains(rollSomeDice.toLowerCase())
                    || message.contains(rollNSided.toLowerCase())
                    || message.contains("roll a d".toLowerCase()) || message.contains("roll me some dice")){
                int diceRoll;
                int nSides = 6;
                if((message.contains(rollNSided.toLowerCase()) && !message.contains(dice.toLowerCase()))
                        || message.contains("roll a d".toLowerCase())){
                    int tempNumber = RollDice.getNumber(message);
                    System.out.println(""+tempNumber);
                    if(tempNumber > 0){
                        nSides = tempNumber;
                    }
                    else{
                        outputMessage.set("No can do, Imma roll a D6 instead");
                    }
                }
                diceRoll = RollDice.rollDice(nSides);
                outputMessage.set(diceRoll+"");
            }

            else if (event.getMessageContent().equalsIgnoreCase("!Help Menu")) {
                event.getChannel().sendMessage("!rr: [cuisine/type of restaurant] [Zip Code]\n" +
                        "!rn: [ZIP Code]\n" +
                        "!choose: [name1], [name2], [name3],...,[name30]");
            }

            else {
                event.getChannel().sendMessage("It seems like you are calling for us, but we cannot do whatever it is that you are asking of us.\n" +
                        "Refer to our help menu by typing \"!Help Menu\" to see what commands are available.");
            }
			
            message = "";
            //  wait(50);
            event.getChannel().sendMessage(String.valueOf(outputMessage));
        });
    }
}
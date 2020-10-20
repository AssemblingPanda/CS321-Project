package com.github.assemblingpanda;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class TheFoodBot {
    public static void main(String[] args) {
        // Insert the bot's token here
        String token = "NzY3OTU0NjgzNjQxNzI0OTU4.X45biA.eZt6cUF59N0JN3cItm73elGVCDY";
		
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
		
        api.addMessageCreateListener(event -> {
            
        });
    }
}
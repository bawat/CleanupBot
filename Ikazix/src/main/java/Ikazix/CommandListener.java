package Ikazix;

import java.util.Optional;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;

public class CommandListener {
	
	public static void main(String[] args) {
		DiscordClient client = new DiscordClientBuilder(System.getenv("DISCORD_CLEANUP_BOT_TOKEN")).build();
		client.getEventDispatcher().on(MessageCreateEvent.class) // This listens for all events that are of MessageCreateEvent
        .subscribe(event -> {
        	
        	MessageChannel channel = event.getMessage().getChannel().block();
        	channel.getMessagesBefore(channel.getLastMessage().block().getId())
        			.filter(e -> isIkazix(e.getAuthor()))
        			.filter(e -> e.getTimestamp().toEpochMilli() > System.currentTimeMillis() - 1000 * 60 * 60 * 24)
        			.filter(e -> e.getTimestamp().toEpochMilli() < System.currentTimeMillis() - 60 * 1000)
        			.collectList().block().stream().forEach(e -> e.delete().block());
        	
        }); // "subscribe" is the method you need to call to actually make sure that it's doing something.
		client.login().block(); // You could call subscribe here, but if you did, the JVM would have no non-daemon threads running and would shut down. Use block unless you know what you're doing!
	}
	
	private static boolean isIkazix(Optional<User> author) {
		if(!author.isPresent()) return false;
		return "0821".equals(author.get().getDiscriminator());
	}
}

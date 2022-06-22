package in.mcxiv.gfg_bot.mods;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ResponsivenessHelper extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        event.getMessage().addReaction("\uD83D\uDC40").queue();
    }
}

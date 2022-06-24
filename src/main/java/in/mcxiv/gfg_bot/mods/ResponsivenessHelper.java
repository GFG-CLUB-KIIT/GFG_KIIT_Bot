package in.mcxiv.gfg_bot.mods;

import in.mcxiv.gfg_bot.SpecialisedListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class ResponsivenessHelper extends SpecialisedListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        event.getMessage().addReaction("\uD83D\uDC40").queue();
    }
}

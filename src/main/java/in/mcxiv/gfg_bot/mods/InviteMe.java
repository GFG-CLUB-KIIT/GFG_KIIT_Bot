package in.mcxiv.gfg_bot.mods;

import in.mcxiv.gfg_bot.GFG_KIIT_Bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class InviteMe extends ListenerAdapter {

    private final GFG_KIIT_Bot bot;

    public InviteMe(GFG_KIIT_Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getMessage().getContentDisplay().contains("invite")) return;

        event.getChannel()
                .sendMessage("You can add me to your study group using this link:\n%s".formatted(bot.resources.inviteLink))
                .queue();
    }
}
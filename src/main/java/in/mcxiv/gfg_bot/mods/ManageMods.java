package in.mcxiv.gfg_bot.mods;

import in.mcxiv.gfg_bot.GFG_KIIT_Bot;
import in.mcxiv.gfg_bot.SpecialisedListenerAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ManageMods extends SpecialisedListenerAdapter {

    private final GFG_KIIT_Bot bot;

    public ManageMods(GFG_KIIT_Bot bot) {
        this.bot = bot;
    }

    @Override
    public String getName() {
        return "Mods Manager";
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String content = event.getMessage().getContentDisplay();
        if (!content.contains("mod")) return;
        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) return;

        String name = event.getMessage().getContentRaw();
        name = name.substring(name.indexOf("able") + 4).strip();
        if (name.isEmpty()) return;
        SpecialisedListenerAdapter la = bot.listenerAdapter.getListenerByName(name);
        if (isNull(la, name, event)) return;

        if (content.contains("mod enable")) enableMod(event, la);
        else if (content.contains("mod disable")) disableMod(event, la);
    }

    private void enableMod(MessageReceivedEvent event, SpecialisedListenerAdapter la) {
        if (la.isActivated()) event.getMessage().reply("The mod is already online :eyes:...").queue();
        else {
            la.setActivated(true);
            event.getMessage().reply("%s has successfully been activated!".formatted(la.getName())).queue();
        }
    }

    private void disableMod(MessageReceivedEvent event, SpecialisedListenerAdapter la) {
        if (Objects.equals(la.getName(), getName()))
            event.getMessage().reply("What are you trying to do :eyes:...?").queue();
        else if (!la.isActivated())
            event.getMessage().reply("The mod is already offline :eyes:...").queue();
        else {
            la.setActivated(false);
            event.getMessage().reply("%s has successfully been deactivated!".formatted(la.getName())).queue();
        }
    }

    private boolean isNull(SpecialisedListenerAdapter la, String content, MessageReceivedEvent event) {
        if (la != null) return false;
        event.getMessage().reply("'%s' doesn't seems to be one of the mods which I have installed \uD83E\uDD14...".formatted(content)).queue();
        return true;
    }

    @Override
    public EmbedBuilder getHelpEmbed() {
        return new EmbedBuilder()
                .addField("mod enable", "Enable a mod.", false)
                .addField("mod disable", "Disable a mod.", false);
    }

}

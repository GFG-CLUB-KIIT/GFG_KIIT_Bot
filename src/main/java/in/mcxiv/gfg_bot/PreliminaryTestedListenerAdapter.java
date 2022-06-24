package in.mcxiv.gfg_bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public final class PreliminaryTestedListenerAdapter extends ListenerAdapter {

    private final ArrayList<SpecialisedListenerAdapter> listeners = new ArrayList<>();
    private final GFG_KIIT_Bot bot;

    public PreliminaryTestedListenerAdapter(GFG_KIIT_Bot bot) {
        this.bot = bot;
    }

    public void addEventListener(SpecialisedListenerAdapter listener) {
        listeners.add(Objects.requireNonNull(listener));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        listeners.stream().filter(SpecialisedListenerAdapter::isSuperior).forEach(listenerAdapter -> listenerAdapter.onMessageReceived(event));
        if (!event.getMessage().getContentDisplay().startsWith(bot.resources.botCommand)) return;
        listeners.stream().filter(SpecialisedListenerAdapter::isInferior).forEach(listenerAdapter -> listenerAdapter.onMessageReceived(event));
    }

}

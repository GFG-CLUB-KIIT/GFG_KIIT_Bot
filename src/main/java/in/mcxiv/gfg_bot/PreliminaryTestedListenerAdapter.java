package in.mcxiv.gfg_bot;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class PreliminaryTestedListenerAdapter extends ListenerAdapter {

    private final ArrayList<SpecialisedListenerAdapter> listeners = new ArrayList<>();
    private final GFG_KIIT_Bot bot;

    public PreliminaryTestedListenerAdapter(GFG_KIIT_Bot bot) {
        this.bot = bot;
    }

    public void addEventListener(SpecialisedListenerAdapter listener) {
        listeners.add(Objects.requireNonNull(listener));
    }

    public List<SpecialisedListenerAdapter> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    private Stream<SpecialisedListenerAdapter> getActiveListeners() {
        return listeners.stream().filter(SpecialisedListenerAdapter::isActivated);
    }

    public SpecialisedListenerAdapter getListenerByName(String name) {
        for (SpecialisedListenerAdapter listener : listeners)
            if (listener.getName().equalsIgnoreCase(name))
                return listener;
        return null;
    }

    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {
        if (event instanceof MessageReceivedEvent) return;
        getActiveListeners().forEach(el -> el.onEvent(event));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getChannel().getType() != ChannelType.TEXT) return;
        getActiveListeners().filter(SpecialisedListenerAdapter::isSuperior)
                .forEach(listenerAdapter -> listenerAdapter.onMessageReceived(event));
        if (!event.getMessage().getContentDisplay().startsWith(bot.resources.botCommand)) return;
        getActiveListeners().filter(SpecialisedListenerAdapter::isInferior)
                .forEach(listenerAdapter -> listenerAdapter.onMessageReceived(event));
    }

}

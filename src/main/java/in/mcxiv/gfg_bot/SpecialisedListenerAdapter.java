package in.mcxiv.gfg_bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class SpecialisedListenerAdapter extends ListenerAdapter {

    private boolean isActivated = true;

    public boolean isSuperior() {
        return false;
    }

    public final boolean isInferior() {
        return !isSuperior();
    }

    public abstract String getName();

    public abstract EmbedBuilder getHelpEmbed();

    public final boolean isActivated() {
        return isActivated;
    }

    public final void setActivated(boolean activated) {
        isActivated = activated;
    }
}

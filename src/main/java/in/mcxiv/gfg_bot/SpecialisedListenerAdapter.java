package in.mcxiv.gfg_bot;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class SpecialisedListenerAdapter extends ListenerAdapter {

    public boolean isSuperior() {
        return false;
    }

    public final boolean isInferior() {
        return !isSuperior();
    }

}

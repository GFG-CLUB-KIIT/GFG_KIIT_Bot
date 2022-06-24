package in.mcxiv.gfg_bot;

import com.mcxiv.logger.decorations.Format;
import com.mcxiv.logger.tables.Table;
import in.mcxiv.gfg_bot.mods.GFGSummerCampProjectUtilities;
import in.mcxiv.gfg_bot.mods.InviteMe;
import in.mcxiv.gfg_bot.mods.ResponsivenessHelper;
import in.mcxiv.tryCatchSuite.Try;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Scanner;

public class GFG_KIIT_Bot extends ListenerAdapter {

    static final GFG_KIIT_Bot instance = new GFG_KIIT_Bot();

    public volatile Resources resources;
    public volatile JDA jda;
    public PreliminaryTestedListenerAdapter listenerAdapter;

    public GFG_KIIT_Bot() {
        refreshResources();
        initializeBot();
    }

    public static GFG_KIIT_Bot getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Try.setExceptionHandler((i, e) -> {
            if (i) Table.tabulate(getInstance().resources.log, e);
            else getInstance().error(e.getClass().getSimpleName(), e.getMessage());
        });
        Thread inputThread = new Thread(() -> {
            new Scanner(System.in).next();
            getInstance().jda.shutdown();
            getInstance().notice("App Closing", "The application is going to shut down.");
            System.exit(0);
        });
        inputThread.setPriority(Thread.MIN_PRIORITY);
        inputThread.start();
    }

    private void refreshResources() {
        resources = new Resources();
    }

    private void initializeBot() {
        if (jda != null) jda.shutdownNow();

        JDABuilder builder = JDABuilder
                .createDefault(resources.botToken)
                .setActivity(Activity.watching("you 👀"))
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE)
                .disableIntents(Arrays.asList(GatewayIntent.values()))
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(this, listenerAdapter = new PreliminaryTestedListenerAdapter(this));

        listenerAdapter.addEventListener(new ResponsivenessHelper());
        listenerAdapter.addEventListener(new InviteMe(this));
        listenerAdapter.addEventListener(new GFGSummerCampProjectUtilities(this));

        jda = Try.get(builder::build);
        Try.run(jda::awaitReady);
        notice("Initializer", "JDA Up and Running!");
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

    }

    @Format({"  :: :#0f0 @050 <D hh;mm;ss> %*30s b: ::", "::\t:#090 @dfd n %-130s:"})
    public void notice(String title, String msg) {
        resources.log.prt(title, msg);
    }

    @Format({"  :: :#f00 @500 <D hh;mm;ss> %*30s b: ::", "::\t:#900 @fdd n %-130s:"})
    public void error(String title, String msg) {
        resources.log.prt(title, msg);
    }

    public String stripCommand(String contentRaw) {
        contentRaw = contentRaw.stripLeading();
        if (!contentRaw.startsWith(resources.botCommand))
            return contentRaw;
        return contentRaw.substring(resources.botCommand.length()).stripLeading();
    }
}

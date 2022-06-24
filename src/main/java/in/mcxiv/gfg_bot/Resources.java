package in.mcxiv.gfg_bot;

import com.mcxiv.logger.decorations.Decorations;
import com.mcxiv.logger.formatted.FLog;
import com.mcxiv.logger.formatted.fixed.FileLog;
import com.mcxiv.logger.tools.LogLevel;
import com.mcxiv.logger.ultimate.ULog;
import org.slf4j.impl.StaticLoggerBinder;

import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class Resources {

    public static final boolean IS_UNDER_TEST = true;
    public static final String NAME = "GFG KIIT Bot";
    public static final String NAME_U = "GFG_KIIT_Bot";

    public final FLog log;
    public final int cores;
    public final String inviteLink;
    public final String botCommand = "kb";
    final String botToken;

    public Resources() {
        cores = Runtime.getRuntime().availableProcessors();
        System.out.printf("We are running on a total of %d cores!%n", cores);
        if (cores <= 1) {
            System.out.println("Welp... Setting up Parallelism Flag.");
            System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "1");
        }

        //  250 Jaspinik, 322 gfgkiitbot

        FLog file_logger = FileLog.getNew(NAME_U + ".log");
        FLog strm_logger = FLog.getNew();
        file_logger.setDecorationType(Decorations.TAG);
        strm_logger.setDecorationType(Decorations.CONSOLE);
        log = ULog.forNew().add(file_logger).add(strm_logger).create();
        log.prtf(":: :#900$YBGbn: ::").consume(LocalDateTime.now().toString());
        if (IS_UNDER_TEST) LogLevel.DEBUG.activate();
        else LogLevel.NOTICE.activate();
        StaticLoggerBinder.setInstance(new StaticLoggerBinder(log));

        ResourceBundle bundle = ResourceBundle.getBundle("privateResources");
        botToken = IS_UNDER_TEST ? bundle.getString("development_discord_bot_token")
                : bundle.getString("discord_bot_token");
        inviteLink = bundle.getString("discord_bot_invite_link");
    }
}

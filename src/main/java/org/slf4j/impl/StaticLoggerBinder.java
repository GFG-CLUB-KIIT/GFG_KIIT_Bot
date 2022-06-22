package org.slf4j.impl;

import com.mcxiv.logger.formatted.FLog;
import in.mcxiv.gfg_bot.GFG_KIIT_Bot;
import in.mcxiv.gfg_bot.SJF4JBinder;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {

    private static StaticLoggerBinder instance;
    private static SJF4JBinder binder = null;

    public StaticLoggerBinder(FLog log) {
        binder = new SJF4JBinder(log);
    }

    public static StaticLoggerBinder getSingleton() {
        return instance;
    }

    public static void setInstance(StaticLoggerBinder instance) {
        StaticLoggerBinder.instance = instance;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return binder;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return SJF4JBinder.class.getName();
    }
}

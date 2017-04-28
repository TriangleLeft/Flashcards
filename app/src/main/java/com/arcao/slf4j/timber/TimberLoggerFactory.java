package com.arcao.slf4j.timber;

import com.triangleleft.flashcards.ui.common.BaseActivity;
import com.triangleleft.flashcards.ui.common.BaseFragment;
import com.triangleleft.flashcards.ui.common.ComponentManager;
import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TimberLoggerFactory is an implementation of {@link ILoggerFactory} returning
 * the appropriately named {@link org.slf4j.LoggerFactory} instance.
 *
 * @author Martin Sloup <arcao@arcao.com>
 */
public class TimberLoggerFactory implements ILoggerFactory {
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("\\$\\d+$");

    private final ILoggerFactory NOPFactory = new NOPLoggerFactory();
    private final ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();
    private final static Set<String> IGNORED_TAGS = new HashSet<>();

    static {
        IGNORED_TAGS.add(BaseFragment.class.getSimpleName());
        IGNORED_TAGS.add(BaseActivity.class.getSimpleName());
        IGNORED_TAGS.add(ComponentManager.class.getSimpleName());
        IGNORED_TAGS.add(AbstractPresenter.class.getSimpleName());
    }

    /**
     * Return an appropriate {@link TimberLoggerAdapter} instance by name.
     */
    @Override
    public Logger getLogger(String name) {
        String tag = createTag(name);
        for (String ignoredTag : IGNORED_TAGS) {
            if (tag.contains(ignoredTag)) {
                return NOPFactory.getLogger(name);
            }
        }
        Logger logger = loggerMap.get(tag);
        if (logger == null) {
            Logger newInstance = new TimberLoggerAdapter(tag);
            Logger oldInstance = loggerMap.putIfAbsent(tag, newInstance);
            logger = oldInstance == null ? newInstance : oldInstance;
        }
        return logger;
    }

    private static String createTag(String name) {
        String tag = name;

        Matcher m = ANONYMOUS_CLASS.matcher(tag);
        if (m.find()) {
            tag = m.replaceAll("");
        }
        return tag.substring(tag.lastIndexOf('.') + 1);
    }
}
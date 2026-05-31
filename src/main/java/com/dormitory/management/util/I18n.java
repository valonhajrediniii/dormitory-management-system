package com.dormitory.management.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public final class I18n {
    public static final Locale ALBANIAN = Locale.forLanguageTag("sq");
    public static final Locale ENGLISH = Locale.ENGLISH;

    private static final String BUNDLE_BASE_NAME = "i18n.messages";

    private static Locale currentLocale = ALBANIAN;

    private I18n() {
    }

    public static Locale getLocale() {
        return currentLocale;
    }

    public static void setLocale(Locale locale) {
        currentLocale = Objects.requireNonNull(locale);
    }

    public static ResourceBundle bundle() {
        return ResourceBundle.getBundle(BUNDLE_BASE_NAME, currentLocale);
    }

    public static String tr(String key, Object... args) {
        String pattern = bundle().getString(key);
        if (args == null || args.length == 0) {
            return pattern;
        }
        return MessageFormat.format(pattern, args);
    }

    public static Locale localeFromCode(String code) {
        if (code == null) {
            return ALBANIAN;
        }

        String normalized = code.trim().toLowerCase(Locale.ROOT);
        if (normalized.equals("en")) {
            return ENGLISH;
        }
        return ALBANIAN;
    }

    public static List<String> localizedLanguageOptions() {
        return List.of(tr("language.albanian"), tr("language.english"));
    }

    public static String localizedLanguageForCurrentLocale() {
        return localizedLanguageLabel(getLocale());
    }

    public static String localizedLanguageLabel(Locale locale) {
        if (locale != null && "en".equalsIgnoreCase(locale.getLanguage())) {
            return tr("language.english");
        }
        return tr("language.albanian");
    }

    public static Locale localeFromSelection(String selection) {
        if (selection == null) {
            return ALBANIAN;
        }

        String normalized = selection.trim();
        if (normalized.equalsIgnoreCase("EN")
                || normalized.equalsIgnoreCase("English")
                || normalized.equalsIgnoreCase("Anglisht")
                || normalized.equals(tr("language.english"))) {
            return ENGLISH;
        }
        if (normalized.equalsIgnoreCase("SQ")
                || normalized.equalsIgnoreCase("Shqip")
                || normalized.equals(tr("language.albanian"))) {
            return ALBANIAN;
        }

        return localeFromCode(normalized);
    }

    public static String codeFromLocale(Locale locale) {
        if (locale != null && "en".equalsIgnoreCase(locale.getLanguage())) {
            return "EN";
        }
        return "SQ";
    }
}

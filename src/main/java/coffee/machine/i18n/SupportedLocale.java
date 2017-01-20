package coffee.machine.i18n;

import java.util.Arrays;
import java.util.Locale;

/**
 * This enum defines all supported locales and request parameter values, corresponding them.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public enum SupportedLocale {
    RU(new Locale("ru", "RU"), "ru"),
    UA(new Locale("uk", "UA"), "ua"),
    EN(new Locale("en", "EN"), "en");

    private static final SupportedLocale defaultLocale = EN;
    private final Locale locale;
    private final String param;

    SupportedLocale(Locale locale, String param) {
        this.locale = locale;
        this.param = param;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getParam() {
        return param;
    }

    public static Locale getDefault() {
        return defaultLocale.getLocale();
    }

    public static SupportedLocale[] getLocales() {
        return values();
    }

    public static Locale getSupportedOrDefault(Locale locale) {
        return Arrays.stream(values())
                .map(SupportedLocale::getLocale)
                .filter(loc -> loc.equals(locale))
                .findAny()
                .orElse(getDefault());
    }

    public static Locale getSupportedOrDefault(String param) {
        return Arrays.stream(values())
                .filter(holder -> holder.param.equals(param))
                .map(SupportedLocale::getLocale)
                .findAny()
                .orElse(getDefault());
    }
}

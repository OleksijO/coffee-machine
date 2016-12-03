package coffee.machine.i18n;

import java.util.Locale;

/**
 * @author oleksij.onysymchuk@gmail.com 20.11.2016.
 */
public enum SupportedLocale {
	RU(new Locale("ru", "RU"), "ru"), UA(new Locale("uk", "UA"), "ua"), EN(new Locale("en", "EN"), "en");

	final private Locale locale;
	final private String param;
	final static private SupportedLocale defaultLocale = RU;

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
}

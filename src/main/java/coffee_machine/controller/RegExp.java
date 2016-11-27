package coffee_machine.controller;

public interface RegExp {
	String REGEXP_EMAIL = "^([a-z0-9_-]+\\.)*[a-z0-9_\\-]+@[a-z0-9_-]+(\\.[a-z0-9_\\-]+)*\\.[a-z]{2,6}$";
	String REGEXP_PASSWORD = "^[A-Za-z0-9_\\-]{4,12}$";

	String REGEXP_DRINK_PARAM = "^drink_\\d+$";
	String REGEXP_ADDON_IN_DRINK_PARAM = "^drink_\\d+addon_\\d+$";
	String REGEXP_NUMBER = "\\d+";

}

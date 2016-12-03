package coffee.machine.controller;

/**
 * This class represents interface for command in command pattern.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface CommandHolder {

	/**
	 * @param path request uri
	 * @return	correspondent command instance for GET request method
	 */
	Command get(String path);

	/**
	 * @param path request uri
	 * @return	correspondent command instance for POST request method
	 */
	Command post(String path);

}

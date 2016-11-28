package coffee_machine.controller;

public interface CommandHolder {

	Command get(String path);

	Command post(String path);

}

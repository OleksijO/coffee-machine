package coffee_machine.controller;

public interface CommandHolder {

	Command get(String path);

	void init();
}

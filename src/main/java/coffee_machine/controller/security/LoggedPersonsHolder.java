package coffee_machine.controller.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import coffee_machine.model.entity.user.Admin;
import coffee_machine.model.entity.user.User;

public class LoggedPersonsHolder {
	final private static LoggedPersonsHolder instance = new LoggedPersonsHolder();
	final private Map<String, User> tableSessionIdUser = new ConcurrentHashMap<>();
	final private Map<String, Admin> tableSessionIdAdmin = new ConcurrentHashMap<>();

	public static LoggedPersonsHolder getInstance() {
		return instance;
	}

	public boolean isSessionUserAuthentificated(HttpSession session) {
		return tableSessionIdUser.containsKey(session.getId());
	}

	public boolean isSessionAdminAuthentificated(HttpSession session) {
		return tableSessionIdAdmin.containsKey(session.getId());
	}

	public User getLoggedUser(HttpSession session) {
		return tableSessionIdUser.get(session.getId());
	}

	public Admin getLoggedAdmin(HttpSession session) {
		return tableSessionIdAdmin.get(session.getId());
	}

	public void authentificateUser(HttpSession session, User user) {
		tableSessionIdUser.put(session.getId(), user);
	}

	public void authentificateAdmin(HttpSession session, Admin admin) {
		tableSessionIdAdmin.put(session.getId(), admin);
	}

	public void logOut(HttpSession session) {
		tableSessionIdUser.remove(session.getId());
		tableSessionIdAdmin.remove(session.getId());
	}

	public void logOut(User user) {
		tableSessionIdUser.keySet().forEach(sessionId -> {
			if (tableSessionIdUser.get(sessionId).equals(user)) {
				tableSessionIdUser.remove(sessionId);
				return;
			}
		});

	}

	public void logOut(Admin admin) {
		tableSessionIdUser.keySet().forEach(sessionId -> {
			if (tableSessionIdUser.get(sessionId).equals(admin)) {
				tableSessionIdUser.remove(sessionId);
				return;
			}
		});

	}
}

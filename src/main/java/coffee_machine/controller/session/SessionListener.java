package coffee_machine.controller.session;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import coffee_machine.controller.security.LoggedPersonsHolder;

/**
 * Application Lifecycle Listener implementation class SessionListener
 *
 */
@WebListener
public class SessionListener implements HttpSessionListener {
	private LoggedPersonsHolder loggedUsersHolder = LoggedPersonsHolder.getInstance();

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		loggedUsersHolder.logOut(event.getSession());
	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {

	}

}

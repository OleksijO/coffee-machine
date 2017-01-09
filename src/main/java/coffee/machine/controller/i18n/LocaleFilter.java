package coffee.machine.controller.i18n;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.i18n.SupportedLocale;
import coffee.machine.view.Parameters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

import static coffee.machine.view.Attributes.BUNDLE_FILE;
import static coffee.machine.view.Attributes.USER_LOCALE;

/**
 * This class represents locale filter.
 * It changes and sets up user's current session locale depends on request parameters. Initially tries to set up
 * locale to locale of request if it is supported by application (or sets up default), later changes on demand by
 * request query parameter.
 * Also, sets up message resource bundle for the view pages.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = ((HttpServletRequest) request);

        setUpResourceBundleSourceForView(req);
        changeUserLocaleByRequestParameter(req);

        setUpUserLocaleIfAbsent(req);

        chain.doFilter(request, response);
    }

    private void setUpResourceBundleSourceForView(HttpServletRequest request) {
        if (request.getSession().getAttribute(BUNDLE_FILE) == null) {
            request.getSession().setAttribute(BUNDLE_FILE, CoffeeMachineConfig.MESSAGES);
        }
    }

    private void changeUserLocaleByRequestParameter(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String localeParameter = request.getParameter(Parameters.USER_LOCALE);
        if (localeParameter != null) {
            Locale locale = SupportedLocale.getSupportedOrDefault(localeParameter);
            session.setAttribute(USER_LOCALE, locale);
        }
    }

    private void setUpUserLocaleIfAbsent(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute(USER_LOCALE) != null) {
            return;
        }
        Locale requestLocale = request.getLocale();
        Locale locale = SupportedLocale.getSupportedOrDefault(requestLocale);
        session.setAttribute(USER_LOCALE, locale);
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}

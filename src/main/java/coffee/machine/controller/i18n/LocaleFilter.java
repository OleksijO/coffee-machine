package coffee.machine.controller.i18n;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.i18n.SupportedLocale;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

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
    private static final Logger logger = Logger.getLogger(LocaleFilter.class);


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
            Locale locale = findSupportedLocaleByParameter(localeParameter);
            session.setAttribute(USER_LOCALE, locale);
        }
    }

    private Locale findSupportedLocaleByParameter(String localeParameter) {
        for (SupportedLocale loc : SupportedLocale.values()) {
            if (loc.getParam().equals(localeParameter)) {
                return loc.getLocale();
            }
        }
        return SupportedLocale.getDefault();
    }

    private void setUpUserLocaleIfAbsent(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute(USER_LOCALE) != null) {
            return;
        }
        Locale requestLocale = request.getLocale();
        Locale locale = findSupportedLocale(requestLocale);
        session.setAttribute(USER_LOCALE, locale);
    }

    private Locale findSupportedLocale(Locale requestLocale) {
        if (requestLocale == null) {
            return SupportedLocale.getDefault();
        }
        for (SupportedLocale loc : SupportedLocale.values()) {
            if (loc.getLocale().equals(requestLocale)) {
                return loc.getLocale();
            }
        }
        return SupportedLocale.getDefault();
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}

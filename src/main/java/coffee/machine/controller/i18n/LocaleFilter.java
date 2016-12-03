package coffee.machine.controller.i18n;

import coffee.machine.CoffeeMachineConfig;
import coffee.machine.i18n.SupportedLocale;
import coffee.machine.view.Attributes;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * This class represents locale filter.
 * It changes and sets up user's current session locale depends on request parameters. Initially tries to set up
 * locale to locale of request if it is supported by application (or sets up default), later changes on demand by
 * request query parameter.
 *
 * Also, sets up message resource budle for the view pages.
 *
 *  @author oleksij.onysymchuk@gmail.com
 */
public class LocaleFilter implements Filter {
    private static final Logger logger = Logger.getLogger(LocaleFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = ((HttpServletRequest) request);
        HttpSession session = req.getSession();

        /* setting up resource bundle for jsp fmt */
        if (req.getSession().getAttribute(Attributes.BUNDLE_FILE) == null) {
            req.getSession().setAttribute(Attributes.BUNDLE_FILE, CoffeeMachineConfig.MESSAGES);
        }

        /* changing user locale by request query parameter */
        if (req.getParameter(Parameters.USER_LOCALE) != null) {
            Locale locale = SupportedLocale.getDefault();
            for (SupportedLocale loc : SupportedLocale.values()) {
                if (loc.getParam().equals(req.getParameter(Parameters.USER_LOCALE))) {
                    locale = loc.getLocale();
                    break;
                }
            }
            session.setAttribute(Attributes.USER_LOCALE, locale);
        }

        /* initially set up user locale be locale in request if supported or default if not*/
        if (session.getAttribute(Attributes.USER_LOCALE) == null) {
            Locale locale = null;
            Locale requeslLocale = request.getLocale();
            if (requeslLocale != null) {
                for (SupportedLocale loc : SupportedLocale.values()) {
                    if (loc.getLocale().toString().equals(requeslLocale.toString())) {
                        locale = loc.getLocale();
                        break;
                    }
                }
            }
            if (locale == null) {
                locale = SupportedLocale.getDefault();
            }
            session.setAttribute(Attributes.USER_LOCALE, locale);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}

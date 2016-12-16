package coffee.machine.controller.i18n;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.i18n.SupportedLocale;
import coffee.machine.view.Attributes;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * This class represents locale filter.
 * It changes and sets up user's current session locale depends on request parameters. Initially tries to set up
 * locale to locale of request if it is supported by application (or sets up default), later changes on demand by
 * request query parameter.
 *
 * Also, sets up message resource bundle for the view pages.
 *
 *  @author oleksij.onysymchuk@gmail.com
 */
public class LocaleFilter implements Filter {
    private static final Logger logger = Logger.getLogger(LocaleFilter.class);
    public static final String UTF_8 = "utf-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = ((HttpServletRequest) request);
        HttpSession session = req.getSession();

        setRequestResponseCharacterEncoding(request, response, UTF_8);
        setUpResourceBundleSourceForView(req);
        changeUserLocaleByRequestParameter(req);

        if (session.getAttribute(Attributes.USER_LOCALE) == null) {
            initialSetUpOfUserLocale(req);
        }

        chain.doFilter(request, response);
    }

    private void setRequestResponseCharacterEncoding(ServletRequest request, ServletResponse response, String encoding) {
        try {
            request.setCharacterEncoding(encoding);
            response.setCharacterEncoding(encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }
    }

    private void setUpResourceBundleSourceForView(HttpServletRequest request) {
        if (request.getSession().getAttribute(Attributes.BUNDLE_FILE) == null) {
            request.getSession().setAttribute(Attributes.BUNDLE_FILE, CoffeeMachineConfig.MESSAGES);
        }
    }

    private void changeUserLocaleByRequestParameter(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (request.getParameter(Parameters.USER_LOCALE) != null) {
            Locale locale = SupportedLocale.getDefault();
            for (SupportedLocale loc : SupportedLocale.values()) {
                if (loc.getParam().equals(request.getParameter(Parameters.USER_LOCALE))) {
                    locale = loc.getLocale();
                    break;
                }
            }
            session.setAttribute(Attributes.USER_LOCALE, locale);
        }
    }



    private void initialSetUpOfUserLocale(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Locale locale = null;
        Locale requestLocale = request.getLocale();
        if (requestLocale != null) {
            for (SupportedLocale loc : SupportedLocale.values()) {
                if (loc.getLocale().toString().equals(requestLocale.toString())) {
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

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}

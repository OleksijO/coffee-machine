package coffee_machine.controller.i18n;

import coffee_machine.view.Attributes;
import coffee_machine.view.Parameters;
import coffee_machine.i18n.SupportedLocale;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;


//@WebFilter("/*")
public class LocaleFilter implements Filter {
    private static final Logger logger = Logger.getLogger(LocaleFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        HttpSession session = req.getSession();
        logger.debug("Locale Filter processing");
        if (req.getParameter(Parameters.USER_LOCALE) != null) {
            logger.debug("Requested locale = " + req.getParameter(Parameters.USER_LOCALE));
            Locale locale = SupportedLocale.getDefault();
            for (SupportedLocale loc : SupportedLocale.values()) {
                if (loc.getParam().equals(req.getParameter(Parameters.USER_LOCALE))) {
                    locale = loc.getLocale();
                    logger.debug("Found supported locale = " + locale );
                    break;
                }
            }

            logger.debug("Set locale = " + locale );
            session.setAttribute(Attributes.USER_LOCALE, locale);

        }

        if (session.getAttribute(Attributes.USER_LOCALE) == null) {
            logger.debug("Locale does not set. Request Locale = " + req.getLocale());
            Locale locale = null;
            for (SupportedLocale loc : SupportedLocale.values()) {
                if (loc.getLocale().toString().equals(request.getLocale().toString())) {
                    locale = loc.getLocale();
                    logger.debug("Found supported locale = " + locale );
                    break;
                }
            }
            if (locale == null) {
                locale = SupportedLocale.getDefault();
                logger.debug("Did not found request locale. Set default = " + locale );
            }
            session.setAttribute(Attributes.USER_LOCALE, locale);
        }
        logger.debug("User locale is = " + session.getAttribute(Attributes.USER_LOCALE));
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}

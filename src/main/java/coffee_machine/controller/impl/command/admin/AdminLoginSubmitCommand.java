package coffee_machine.controller.impl.command.admin;

import coffee_machine.controller.Command;
import coffee_machine.controller.impl.command.abstracts.AbstractLoginCommand;
import coffee_machine.controller.security.PasswordEncryptor;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.model.entity.user.Admin;
import coffee_machine.service.AdminService;
import coffee_machine.service.impl.AdminServiceImpl;
import coffee_machine.view.Attributes;
import coffee_machine.view.PagesPaths;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.view.Attributes.*;
import static coffee_machine.view.PagesPaths.*;
import static coffee_machine.view.Parameters.LOGIN;
import static coffee_machine.view.Parameters.PASSWORD;
import static coffee_machine.i18n.message.key.error.CommandErrorKey.*;


public class AdminLoginSubmitCommand extends AbstractLoginCommand implements Command {
    private static final Logger logger = Logger.getLogger(AdminLoginSubmitCommand.class);
    AdminService adminService = AdminServiceImpl.getInstance();

    public AdminLoginSubmitCommand() {
        super(logger);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_ADMIN_LOGIN);
            request.setAttribute(Attributes.LOGIN_FORM_TITLE, GeneralKey.LOGIN_ADMIN_FORM_TITLE);
            request.setAttribute(Attributes.LOGIN_FORM_ACTION, PagesPaths.ADMIN_LOGIN_PATH);

            String email = request.getParameter(LOGIN);
            String password = request.getParameter(PASSWORD);

            request.setAttribute(PREVIOUS_ENTERED_EMAIL, email);

            if (!checkLogin(email)) {
                request.setAttribute(ERROR_MESSAGE, ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN);
                logger.info(TRY_FAILED_WRONG_EMAIL + email);
                return LOGIN_PAGE;
            }
            if (!checkPassword(password)) {
                request.setAttribute(ERROR_MESSAGE, ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN);
                logger.info(TRY_FAILED_WRONG_PASSWORD);
                return LOGIN_PAGE;
            }

            String encryptedPassword = PasswordEncryptor.encryptPassword(password);
            Admin admin = adminService.getAdminByLogin(email);

            if ((admin == null) || (!encryptedPassword.equals(admin.getPassword()))) {
                logger.info(TRY_FAILED_WRONG_EMAIL_OR_PASSWORD);
                request.setAttribute(ERROR_MESSAGE, ERROR_LOGIN_NO_SUCH_COMBINATION);
            } else {
                if (admin.isEnabled()) {
                    logger.info(String.format(ADMIN_LOGGED_IN, admin.getId()));
                    request.getSession().setAttribute(ADMIN_ID, admin.getId());
                    response.sendRedirect(ADMIN_HOME_PATH);
                    return REDIRECTED;

                } else {
                    request.setAttribute(ERROR_MESSAGE, ERROR_LOGIN_ADMIN_DISABLED);
                }
            }
        } catch (ApplicationException e) {
            logApplicationError(e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(e);
            request.setAttribute(ERROR_MESSAGE, GeneralKey.ERROR_UNKNOWN);
        }

        return LOGIN_PAGE;
    }


}

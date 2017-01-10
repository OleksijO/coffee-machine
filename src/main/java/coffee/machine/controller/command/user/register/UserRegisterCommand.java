package coffee.machine.controller.command.user.register;

import coffee.machine.controller.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.REGISTER_USER_FORM_TITLE;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.TITLE_USER_REGISTER;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.USER_REGISTER_PAGE;
import static coffee.machine.view.PagesPaths.USER_REGISTER_PATH;

/**
 * Created by oleksij.onysymchuk@gmail
 */
public class UserRegisterCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute(PAGE_TITLE, TITLE_USER_REGISTER);
        request.setAttribute(REGISTER_FORM_TITLE, REGISTER_USER_FORM_TITLE);
        request.setAttribute(REGISTER_FORM_ACTION, USER_REGISTER_PATH);
        return USER_REGISTER_PAGE;
    }
}

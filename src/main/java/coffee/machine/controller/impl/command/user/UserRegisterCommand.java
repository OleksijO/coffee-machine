package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.Command;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by oleksij.onysymchuk@gmail on 08.12.2016.
 */
public class UserRegisterCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_REGISTER);
        request.setAttribute(Attributes.REGISTER_FORM_TITLE, GeneralKey.REGISTER_USER_FORM_TITLE);
        request.setAttribute(Attributes.REGISTER_FORM_ACTION, PagesPaths.USER_REGISTER_PATH);
        return PagesPaths.USER_REGISTER_PAGE;
    }
}

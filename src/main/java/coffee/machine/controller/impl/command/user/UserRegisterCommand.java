package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.Command;
import coffee.machine.controller.impl.command.helper.UserRegisterCommandHelper;
import coffee.machine.view.PagesPaths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by oleksij.onysymchuk@gmail
 */
public class UserRegisterCommand implements Command {
    private UserRegisterCommandHelper helper = new UserRegisterCommandHelper();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        helper.setGeneralRegisterPageAttributes(request);
        return PagesPaths.USER_REGISTER_PAGE;
    }
}

package coffee_machine.controller.impl.command;

import coffee_machine.controller.Command;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.Messages.TEST_ERROR_MESSAGE;
import static coffee_machine.Messages.TEST_USUAL_MESSAGE;
import static coffee_machine.controller.Attributes.ERROR_MESSAGE;
import static coffee_machine.controller.Attributes.USUAL_MESSAGE;
import static coffee_machine.controller.PagesPaths.HOME_PAGE;

public class HomeCommand implements Command {
    private static final Logger logger = Logger.getLogger( HomeCommand.class);
    private static int counter = 0;    //TODO DELETE AFTER TESTING

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        // TODO DELETE HARDCODE AFTER TESTS

        if (counter % 2 == 0) {
            request.setAttribute(USUAL_MESSAGE, TEST_USUAL_MESSAGE);
        }
        if (counter % 3 == 0) {
            request.setAttribute(ERROR_MESSAGE,TEST_ERROR_MESSAGE);
        }
        counter++;
        // TODO end of hardcode

        return HOME_PAGE;
    }

}

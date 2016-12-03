package coffee_machine.controller.impl.command.user;

import coffee_machine.controller.impl.command.CommandExecuteWrapper;
import coffee_machine.service.HistoryRecordService;
import coffee_machine.service.impl.HistoryRecordServiceImpl;
import coffee_machine.view.Attributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee_machine.view.Attributes.USER_ID;
import static coffee_machine.view.PagesPaths.USER_HISTORY_PAGE;

/**
 * This class represents user purchase history page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserHistoryCommand extends CommandExecuteWrapper {
    private HistoryRecordService historyRecordService = HistoryRecordServiceImpl.getInstance();

    public UserHistoryCommand() {
        super(USER_HISTORY_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getSession().getAttribute(USER_ID);
        request.setAttribute(Attributes.USER_RECORD_HISTORY_LIST, historyRecordService.getAllByUserId(userId));
        return USER_HISTORY_PAGE;
    }

}

package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.view.PagesPaths;
import coffee.machine.service.HistoryRecordService;
import coffee.machine.service.impl.HistoryRecordServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.Attributes.PAGE_TITLE;
import static coffee.machine.view.Attributes.USER_ID;
import static coffee.machine.view.Attributes.USER_RECORD_HISTORY_LIST;

/**
 * This class represents user purchase history page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserHistoryCommand extends CommandExecuteWrapper {
    private HistoryRecordService historyRecordService = HistoryRecordServiceImpl.getInstance();

    public UserHistoryCommand() {
        super(PagesPaths.USER_HISTORY_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int userId = (int) request.getSession().getAttribute(USER_ID);
        request.setAttribute(PAGE_TITLE, GeneralKey.TITLE_USER_HISTORY);
        request.setAttribute(USER_RECORD_HISTORY_LIST, historyRecordService.getAllByUserId(userId));
        return PagesPaths.USER_HISTORY_PAGE;
    }

}

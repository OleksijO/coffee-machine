package coffee_machine.controller.impl.command.user;

import coffee_machine.controller.Command;
import coffee_machine.controller.logging.ControllerErrorLogging;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.service.HistoryRecordService;
import coffee_machine.service.impl.HistoryRecordServiceImpl;
import coffee_machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static coffee_machine.view.Attributes.*;
import static coffee_machine.view.PagesPaths.USER_HISTORY_PAGE;

public class UserHistoryCommand implements Command, ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(UserHistoryCommand.class);

    private HistoryRecordService historyRecordService = HistoryRecordServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_HISTORY);

        try {

            int userId = (int) request.getSession().getAttribute(USER_ID);
            request.setAttribute(Attributes.USER_RECORD_HISTORY_LIST, historyRecordService.getAllByUserId(userId));

        } catch (ApplicationException e) {
            logApplicationError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, GeneralKey.ERROR_UNKNOWN);
        }


        return USER_HISTORY_PAGE;
    }

}

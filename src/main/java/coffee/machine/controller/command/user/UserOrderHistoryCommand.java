package coffee.machine.controller.command.user;

import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.service.OrderService;
import coffee.machine.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.controller.i18n.message.key.ControllerMessageKey.TITLE_USER_ORDER_HISTORY;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.USER_ORDER_HISTORY_PAGE;

/**
 * This class represents user purchase history page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserOrderHistoryCommand extends CommandWrapperTemplate {
    private OrderService orderHistoryService = OrderServiceImpl.getInstance();

    public UserOrderHistoryCommand() {
        super(USER_ORDER_HISTORY_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        return USER_ORDER_HISTORY_PAGE;
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_USER_ORDER_HISTORY);
        int userId = (int) request.getSession().getAttribute(USER_ID);
        request.setAttribute(USER_ORDERS, orderHistoryService.getAllByUserId(userId));
    }

}

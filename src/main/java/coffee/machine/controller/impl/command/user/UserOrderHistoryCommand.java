package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.impl.command.CommandWrapperTemplate;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.service.OrderService;
import coffee.machine.view.PagesPaths;
import coffee.machine.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.view.Attributes.PAGE_TITLE;
import static coffee.machine.view.Attributes.USER_ID;
import static coffee.machine.view.Attributes.USER_ORDER_LIST;

/**
 * This class represents user purchase history page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserOrderHistoryCommand extends CommandWrapperTemplate {
    private OrderService orderHistoryService = OrderServiceImpl.getInstance();

    public UserOrderHistoryCommand() {
        super(PagesPaths.USER_ORDER_HISTORY_PAGE);
    }



    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (int) request.getSession().getAttribute(USER_ID);
        request.setAttribute(USER_ORDER_LIST, orderHistoryService.getAllByUserId(userId));
        return PagesPaths.USER_ORDER_HISTORY_PAGE;
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, GeneralKey.TITLE_USER_ORDER_HISTORY);
    }

}

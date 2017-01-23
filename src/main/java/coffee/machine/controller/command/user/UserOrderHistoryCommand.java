package coffee.machine.controller.command.user;

import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.controller.command.helper.RequestDataExtractor;
import coffee.machine.service.OrderService;
import coffee.machine.service.impl.OrderServiceImpl;
import coffee.machine.view.config.Parameters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.controller.i18n.message.key.ControllerMessageKey.TITLE_USER_ORDER_HISTORY;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_UNKNOWN;
import static coffee.machine.view.config.Attributes.*;
import static coffee.machine.view.config.Pages.USER_ORDER_HISTORY_PAGE;

/**
 * This class represents user purchase history page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserOrderHistoryCommand extends CommandWrapperTemplate {
    private static final int itemsPerPage = 10;
    private OrderService orderHistoryService = OrderServiceImpl.getInstance();
    private RequestDataExtractor helper = new RequestDataExtractor();

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
        //int pageNumber = getPageNumberFromRequest(request);


        int userId = (int) request.getSession().getAttribute(USER_ID);
       // int ordersStartFrom = (pageNumber - 1) * itemsPerPage;
        //Orders orders = orderHistoryService.getOrdersByUserWithLimits(userId, ordersStartFrom, itemsPerPage);
        //List<Order> orders =  orderHistoryService.getAllByUserId(userId, ordersStartFrom, itemsPerPage + 1);
        request.setAttribute(USER_ORDERS, orderHistoryService.getAllByUserId(userId));
    }

    private int getPageNumberFromRequest(HttpServletRequest request) {
        int requestedPageNumber = helper.getIntFromRequestByParameter(request, Parameters.PAGE, ERROR_UNKNOWN);
        if (requestedPageNumber < 1) {
            requestedPageNumber = 1;
        }
        return requestedPageNumber;
    }

}

package coffee.machine.controller.command.user;

import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.controller.command.helper.RequestDataExtractor;
import coffee.machine.model.value.object.Orders;
import coffee.machine.service.OrderService;
import coffee.machine.service.impl.OrderServiceImpl;
import coffee.machine.view.config.Attributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static coffee.machine.controller.i18n.message.key.ControllerMessageKey.TITLE_USER_ORDER_HISTORY;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_UNKNOWN;
import static coffee.machine.view.config.Attributes.*;
import static coffee.machine.view.config.Pages.USER_ORDER_HISTORY_PAGE;
import static coffee.machine.view.config.Parameters.PAGE;

/**
 * This class represents user purchase history page handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserOrderHistoryCommand extends CommandWrapperTemplate {
    private static final int itemsPerPage = 10;
    private static final int FIRST = 1;

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

        int currentPageNumber = getPageNumberFromRequest(request);
        int userId = (int) request.getSession().getAttribute(Attributes.USER_ID);
        int ordersStartFrom = calculateItemOffset(currentPageNumber);

        Orders orders = orderHistoryService.getOrdersByUserWithLimits(userId, ordersStartFrom, itemsPerPage);
        int lastPageNumber = calculateLastPageNumber(orders.getTotalCount());
        while (currentPageNumber > lastPageNumber) {
            currentPageNumber = lastPageNumber;
            ordersStartFrom = calculateItemOffset(currentPageNumber);
            orders = orderHistoryService.getOrdersByUserWithLimits(userId, ordersStartFrom, itemsPerPage);
            lastPageNumber = calculateLastPageNumber(orders.getTotalCount());
        }

        request.setAttribute(PAGE_TITLE, TITLE_USER_ORDER_HISTORY);
        request.setAttribute(USER_ORDERS, orders.getOrderList());
        request.setAttribute(CURRENT_PAGE, currentPageNumber);
        request.setAttribute(LAST_PAGE, lastPageNumber);
    }

    private int calculateItemOffset(int pageNumber) {
        return (pageNumber - FIRST) * itemsPerPage;
    }

    private int calculateLastPageNumber(int totalCount) {
        return (int) Math.ceil(1.0 * totalCount / itemsPerPage);
    }

    private int getPageNumberFromRequest(HttpServletRequest request) {
        if (request.getParameter(PAGE)==null){
            return FIRST;
        }
        int requestedPageNumber = helper.getIntFromRequestByParameter(request, PAGE, ERROR_UNKNOWN);
        if (requestedPageNumber < FIRST) {
            requestedPageNumber = FIRST;
        }
        return requestedPageNumber;
    }

}

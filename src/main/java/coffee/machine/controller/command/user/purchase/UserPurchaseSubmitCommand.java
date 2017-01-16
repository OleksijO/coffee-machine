package coffee.machine.controller.command.user.purchase;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.RegExp;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.controller.command.helper.RequestDataExtractor;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.service.AccountService;
import coffee.machine.service.OrderPreparationService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.OrderPreparationServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import static coffee.machine.controller.i18n.message.key.ControllerMessageKey.PURCHASE_THANKS_MESSAGE;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.QUANTITY_SHOULD_BE_NON_NEGATIVE;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.TITLE_USER_PURCHASE;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.USER_PURCHASE_PAGE;

public class UserPurchaseSubmitCommand extends CommandWrapperTemplate {
    private static final Logger logger = Logger.getLogger(UserPurchaseSubmitCommand.class);
    private static final String USER_WITH_ID_MADE_PURCHASE_FORMAT = "User with id = %d made purchase: %s";

    private final Pattern patternProduct = Pattern.compile(RegExp.REGEXP_ANY_PRODUCT);
    private final Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
    private final Pattern patternAddon = Pattern.compile(RegExp.REGEXP_ADDON_IN_DRINK_PARAM);

    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();
    private OrderPreparationService coffeeMachine = OrderPreparationServiceImpl.getInstance();

    private RequestDataExtractor dataExtractorHelper = new RequestDataExtractor();


    public UserPurchaseSubmitCommand() {
        super(USER_PURCHASE_PAGE);
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_USER_PURCHASE);
        request.setAttribute(ADMIN_CONTACTS, CoffeeMachineConfig.ADMIN_CONTACT_INFO);
        request.setAttribute(BALANCE_LOW_WARN_LIMIT, CoffeeMachineConfig.BALANCE_WARN_LIMIT);
        request.setAttribute(DRINKS, drinkService.getAll());
        request.setAttribute(USER_BALANCE,
                accountService.getByUserId(getUserIdFromSession(request.getSession()))
                        .map(Account::getRealAmount)
                        .orElseThrow(IllegalStateException::new));
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        saveFormData(request);
        Order preOrder = getOrderFromRequest(request);
        Order order = coffeeMachine.prepareOrder(preOrder);
        logDetailsAndPlaceMessageToRequest(request, order);
        clearFormData(request);
        return USER_PURCHASE_PAGE;
    }

    private void saveFormData(HttpServletRequest request) {
        request.setAttribute(PREVIOUS_VALUES_TABLE,
                dataExtractorHelper.getParametersFromRequestByPattern(request, patternProduct));
    }

    private int getUserIdFromSession(HttpSession session) {
        return (int) session.getAttribute(USER_ID);
    }

    private Order getOrderFromRequest(HttpServletRequest request) {

        List<Drink> drinks = getBaseDrinksFromRequest(request);
        drinks = getAddonsToDrinksFromRequest(drinks, request);
        return new Order.Builder()
                .setDrinks(drinks)
                .setUserId(getUserIdFromSession(request.getSession()))
                .build();
    }

    private List<Drink> getBaseDrinksFromRequest(HttpServletRequest request) {
        List<Drink> drinks = new ArrayList<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (patternDrink.matcher(param).matches()) {
                addDrinkToList(drinks, param, request);
            }
        }
        return drinks;
    }

    private void addDrinkToList(List<Drink> drinks, String param, HttpServletRequest request) {
        int quantity = dataExtractorHelper
                .getIntFromRequestByParameter(request, param, QUANTITY_SHOULD_BE_NON_NEGATIVE);
        drinks.add(new Drink.Builder()
                .setQuantity(quantity)
                .setId(dataExtractorHelper.getFirstNumberFromParameterName(param))
                .build());

    }

    private List<Drink> getAddonsToDrinksFromRequest(List<Drink> drinks, HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (patternAddon.matcher(param).matches()) {
                addAddonToDrink(drinks, param, request);
            }
        }
        return drinks;

    }

    private void addAddonToDrink(List<Drink> drinks, String param, HttpServletRequest request) {
        int quantity = dataExtractorHelper
                .getIntFromRequestByParameter(request, param, QUANTITY_SHOULD_BE_NON_NEGATIVE);

        int drinkId = dataExtractorHelper.getFirstNumberFromParameterName(param);
        int addonId = dataExtractorHelper.getSecondNumberFromParameterName(param);
        drinks.stream()
                .filter(drink -> drink.getId() == drinkId)
                .findFirst()
                .orElseThrow(IllegalStateException::new)
                .addAddon(new Product.Builder()
                        .setId(addonId)
                        .setQuantity(quantity)
                        .build());

    }

    private void logDetailsAndPlaceMessageToRequest(HttpServletRequest request, Order order) {
        logger.info(String.format(USER_WITH_ID_MADE_PURCHASE_FORMAT, order.getUserId(), order));
        request.setAttribute(ORDER, order);
        request.setAttribute(USUAL_MESSAGE, PURCHASE_THANKS_MESSAGE);
    }

    private void clearFormData(HttpServletRequest request) {
        request.removeAttribute(PREVIOUS_VALUES_TABLE);
    }

    public void setCoffeeMachineOrderService(OrderPreparationService coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }
}

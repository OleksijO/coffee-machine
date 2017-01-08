package coffee.machine.controller.command.user.purchase;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.RegExp;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.controller.command.helper.RequestDataExtractor;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Drinks;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.AccountService;
import coffee.machine.service.CoffeeMachineOrderService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.CoffeeMachineOrderServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import static coffee.machine.i18n.message.key.CommandKey.PURCHASE_THANKS_MESSAGE;
import static coffee.machine.i18n.message.key.GeneralKey.TITLE_USER_PURCHASE;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.USER_PURCHASE_PAGE;

public class UserPurchaseSubmitCommand extends CommandWrapperTemplate {
    private static final Logger logger = Logger.getLogger(UserPurchaseSubmitCommand.class);
    private static final String USER_WITH_ID_MADE_PURCHASE_FORMAT = "User with id = %d made purchase: %s";

    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();
    private CoffeeMachineOrderService coffeeMachine = CoffeeMachineOrderServiceImpl.getInstance();

    private final Pattern patternItem = Pattern.compile(RegExp.REGEXP_ANY_ITEM);
    private final Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
    private final Pattern patternAddon = Pattern.compile(RegExp.REGEXP_ADDON_IN_DRINK_PARAM);
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
                dataExtractorHelper.getParametersFromRequestByPattern(request, patternItem));
    }

    private int getUserIdFromSession(HttpSession session) {
        return (int) session.getAttribute(USER_ID);
    }

    private Order getOrderFromRequest(HttpServletRequest request) {

        Drinks drinks = getBaseDrinksFromRequest(request);
        drinks = getAddonsToDrinksFromRequest(drinks, request);
        return new Order.Builder()
                .setDrinks(drinks.getDrinks())
                .setUserId(getUserIdFromSession(request.getSession()))
                .build();
    }

    private Drinks getBaseDrinksFromRequest(HttpServletRequest request) {
        Drinks drinks = new Drinks();
        ;
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (patternDrink.matcher(param).matches()) {
                addDrinkWithNonZeroQuantity(drinks, param, request);
            }
        }
        return drinks;
    }

    private void addDrinkWithNonZeroQuantity(Drinks drinks, String param, HttpServletRequest request) {
        int quantity = dataExtractorHelper.getIntFromRequestByParameter(request, param);
        if (quantity != 0) {
            drinks.add(new Drink.Builder()
                    .setQuantity(quantity)
                    .setId(dataExtractorHelper.getFirstNumberFromParameterName(param))
                    .build());
        }
    }

    private Drinks getAddonsToDrinksFromRequest(Drinks drinks, HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (patternAddon.matcher(param).matches()) {
                addAddonWithNonZeroQuantityToDrink(drinks, param, request);
            }
        }
        return drinks;

    }

    private void addAddonWithNonZeroQuantityToDrink(Drinks drinks, String param, HttpServletRequest request) {
        int quantity = dataExtractorHelper.getIntFromRequestByParameter(request, param);
        if (quantity != 0) {
            int drinkId = dataExtractorHelper.getFirstNumberFromParameterName(param);
            int addonId = dataExtractorHelper.getSecondNumberFromParameterName(param);
            drinks.getDrinks().stream()
                    .filter(drink -> drink.getId() == drinkId)
                    .findFirst()
                    .ifPresent(drink -> drink.addAddon(new Item.Builder()
                            .setId(addonId)
                            .setQuantity(quantity)
                            .build()));

        }
    }

    private void logDetailsAndPlaceMessageToRequest(HttpServletRequest request, Order order) {
        logger.info(String.format(USER_WITH_ID_MADE_PURCHASE_FORMAT, order.getUserId(), order));
        request.setAttribute(ORDER, order);
        request.setAttribute(USUAL_MESSAGE, PURCHASE_THANKS_MESSAGE);
    }

    private void clearFormData(HttpServletRequest request) {
        request.removeAttribute(PREVIOUS_VALUES_TABLE);
    }


}

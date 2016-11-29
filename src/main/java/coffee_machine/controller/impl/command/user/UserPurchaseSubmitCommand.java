package coffee_machine.controller.impl.command.user;

import coffee_machine.controller.Command;
import coffee_machine.controller.RegExp;
import coffee_machine.controller.exception.ControllerException;
import coffee_machine.controller.logging.ControllerErrorLogging;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.CommandKey;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.i18n.message.key.error.CommandErrorKey;
import coffee_machine.model.entity.HistoryRecord;
import coffee_machine.model.entity.goods.Drink;
import coffee_machine.service.AccountService;
import coffee_machine.service.CoffeeMachineService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.CoffeeMachineServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;
import coffee_machine.view.Attributes;
import coffee_machine.view.PagesPaths;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static coffee_machine.view.Attributes.*;

public class UserPurchaseSubmitCommand implements Command, ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(UserPurchaseSubmitCommand.class);

    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();
    private CoffeeMachineService coffeeMachine = CoffeeMachineServiceImpl.getInstance();
    private Pattern patternNumber = Pattern.compile(RegExp.REGEXP_NUMBER);
    private Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
    private Pattern patternAddonInDrink = Pattern.compile(RegExp.REGEXP_ADDON_IN_DRINK_PARAM);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_PURCHASE);
        try {
            List<Drink> drinksToBuy = getDrinksFromRequest(request);
            int userId = (int) request.getSession().getAttribute(USER_ID);
            HistoryRecord record = coffeeMachine.prepareDrinksForUser(drinksToBuy, userId);
            request.setAttribute(USER_BALANCE, accountService.getByUserId(userId).getRealAmount());
            request.setAttribute(USUAL_MESSAGE, CommandKey.PURCHASE_THANKS_MESSAGE);
            request.setAttribute(Attributes.HISTORY_RECORD, record);
        } catch (ApplicationException e) {
            logApplicationError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(logger, request, e);
            request.setAttribute(ERROR_MESSAGE, GeneralKey.ERROR_UNKNOWN);
        }

        return PagesPaths.USER_PURCHASE_PAGE;
    }

    private List<Drink> getDrinksFromRequest(HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        Map<Integer, Integer> drinkQuantityByIds = new HashMap<>();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            Matcher matcher = patternDrink.matcher(param);
            if (matcher.matches()) {
                int drinkQuantity = getIntFromRequestByParameter(param, request);
                if (drinkQuantity > 0) {
                    int drinkId = getDrinkIdFromParam(param);
                    drinkQuantityByIds.put(drinkId, drinkQuantity);
                }
            }
        }
        List<Drink> drinks = drinkService.getAllByIdSet(drinkQuantityByIds.keySet());
        drinks = getBaseDrinkListAndSetDrinkQuantities(drinks, drinkQuantityByIds);
        params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            Matcher matcher = patternAddonInDrink.matcher(param);
            if (matcher.matches()) {
                int addonQuantity = getIntFromRequestByParameter(param, request);
                if (addonQuantity > 0) {
                    int drinkId = getDrinkIdFromParam(param);
                    addAddonToDrinkInList(drinkId, getAddonIdFromParam(param), addonQuantity, drinks);
                }
            }
        }
        return drinks;
    }

    private int getIntFromRequestByParameter(String param, HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter(param));
        } catch (Exception e) {
            throw new ControllerException(CommandErrorKey.QUANTITY_SHOULD_BE_INT);
        }

    }

    private int getDrinkIdFromParam(String param) {
        Matcher matcher = patternNumber.matcher(param);
        matcher.find(0);
        return Integer.parseInt(param.substring(matcher.start(), matcher.end()));

    }

    private List<Drink> getBaseDrinkListAndSetDrinkQuantities(List<Drink> drinks,
                                                              Map<Integer, Integer> drinkQuantityByIds) {
        List<Drink> baseDrinks = new ArrayList<>();
        drinks.forEach(drink -> {
            Drink baseDrink = drink.getBaseDrink();
            baseDrink.setQuantity(drinkQuantityByIds.get(drink.getId()));
            baseDrinks.add(baseDrink);
        });
        return baseDrinks;

    }

    private int getAddonIdFromParam(String param) {
        Matcher matcher = patternNumber.matcher(param);
        matcher.find(0);                                    // passing drink id
        matcher.find(matcher.end());
        return Integer.parseInt(param.substring(matcher.start(), matcher.end()));
    }

    private void addAddonToDrinkInList(int drinkId, int addonId, int addonQuantity, List<Drink> drinks) {

        drinks.forEach(drink -> {
            if (drink.getId() == drinkId) {
                drink.getAddons().forEach(addon -> {
                    if (addon.getId() == addonId) {
                        addon.setQuantity(addon.getQuantity() + addonQuantity);
                    }
                });
            }
        });

    }

}

package coffee_machine.controller.impl.command.user;

import static coffee_machine.view.Attributes.DRINKS;
import static coffee_machine.view.Attributes.ERROR_ADDITIONAL_MESSAGE;
import static coffee_machine.view.Attributes.ERROR_MESSAGE;
import static coffee_machine.view.Attributes.USER_BALANCE;
import static coffee_machine.view.Attributes.USER_ID;
import static coffee_machine.view.Attributes.USUAL_MESSAGE;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import coffee_machine.controller.Command;
import coffee_machine.controller.impl.command.request.data.extractor.PurchaseFormDataExtractor;
import coffee_machine.controller.impl.command.request.data.extractor.impl.PurchaseFormExtractorImpl;
import coffee_machine.controller.logging.ControllerErrorLogging;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.CommandKey;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.model.entity.HistoryRecord;
import coffee_machine.model.entity.item.Drink;
import coffee_machine.service.AccountService;
import coffee_machine.service.CoffeeMachineService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.CoffeeMachineServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;
import coffee_machine.view.Attributes;
import coffee_machine.view.PagesPaths;

public class UserPurchaseSubmitCommand implements Command, ControllerErrorLogging {
    private static final Logger logger = Logger.getLogger(UserPurchaseSubmitCommand.class);

    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();
    private CoffeeMachineService coffeeMachine = CoffeeMachineServiceImpl.getInstance();

    private PurchaseFormDataExtractor formExtractor = new PurchaseFormExtractorImpl();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_PURCHASE);
        try {
			/* extracting drinks to buy from request */
            List<Drink> drinksToBuy = getDrinksFromRequest(request);
            int userId = (int) request.getSession().getAttribute(USER_ID);

			/* performing purchase */
            HistoryRecord record = coffeeMachine.prepareDrinksForUser(drinksToBuy, userId);

			/* putting data to show on view in request */
            request.setAttribute(USER_BALANCE, accountService.getByUserId(userId).getRealAmount());
            request.setAttribute(USUAL_MESSAGE, CommandKey.PURCHASE_THANKS_MESSAGE);
            request.setAttribute(Attributes.HISTORY_RECORD, record);
            request.setAttribute(DRINKS, drinkService.getAll());

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

    List<Drink> getDrinksFromRequest(HttpServletRequest request) {
		/* extracting drinks quantity */
        Map<Integer, Integer> drinkQuantityByIds = formExtractor.getDrinksQuantityByIdFromRequest(request);

		/* loading entities from bd */
        List<Drink> drinks = drinkService.getAllBaseByIdSet(drinkQuantityByIds.keySet());

		/* setting actual drink quantities gotten from request */
        setDrinkQuantities(drinks, drinkQuantityByIds);

		/* extracting quantity of addons in drinks */
        Map<Integer, Map<Integer, Integer>> addonsQuantityInDrinksById =
                formExtractor.getAddonsQuantityInDrinksByIdFromRequest(request);

		/* setting actual drink quantities gotten from request */
        setAddonsQuantityInDrinks(addonsQuantityInDrinksById, drinks);

        return drinks;
    }


    void setDrinkQuantities(List<Drink> drinks, Map<Integer, Integer> drinkQuantityByIds) {
        drinks.forEach(drink -> {

			drink.setQuantity(drinkQuantityByIds.get(drink.getId()));

        });
    }

    void setAddonsQuantityInDrinks(Map<Integer, Map<Integer, Integer>> addonsQuantityInDrinksById, List<Drink> drinks) {
        drinks.forEach(drink -> {
            Map<Integer, Integer> addonsQuantityById = addonsQuantityInDrinksById.get(drink.getId());

            if (addonsQuantityById != null) {
                drink.getAddons().forEach(addon -> {

					Integer quantity = addonsQuantityById.get(addon.getId());
                    if (quantity != null) {

						addon.setQuantity(quantity);
                    }
                });
            }
        });
    }


}

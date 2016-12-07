package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.controller.impl.command.request.data.extractor.impl.PurchaseFormExtractorImpl;
import coffee.machine.i18n.message.key.CommandKey;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.model.entity.Order;
import coffee.machine.service.AccountService;
import coffee.machine.service.CoffeeMachineService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.exception.ServiceException;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.CoffeeMachineServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import coffee.machine.controller.impl.command.request.data.extractor.PurchaseFormDataExtractor;
import coffee.machine.model.entity.item.Drink;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserPurchaseSubmitCommand extends CommandExecuteWrapper {
    private static final Logger logger = Logger.getLogger(UserPurchaseSubmitCommand.class);

    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();
    private CoffeeMachineService coffeeMachine = CoffeeMachineServiceImpl.getInstance();

    private PurchaseFormDataExtractor formExtractor = new PurchaseFormExtractorImpl();

    public UserPurchaseSubmitCommand() {
        super(PagesPaths.USER_PURCHASE_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_USER_PURCHASE);
        int userId = (int) request.getSession().getAttribute(Attributes.USER_ID);

        // extracting drinks to buy from request
        List<Drink> drinksToBuy = getDrinksFromRequest(request);

        // performing purchase
        try {
            Order record = coffeeMachine.prepareDrinksForUser(drinksToBuy, userId);
            request.setAttribute(Attributes.ORDER, record);
        } catch (ServiceException e){
            request.setAttribute(Attributes.USER_ACCOUNT, accountService.getByUserId(userId));
            throw e;
        }

		// putting data to show on view in request
        request.setAttribute(Attributes.USUAL_MESSAGE, CommandKey.PURCHASE_THANKS_MESSAGE);
        request.setAttribute(Attributes.DRINKS, drinkService.getAll());
        request.setAttribute(Attributes.USER_ACCOUNT, accountService.getByUserId(userId));
        return PagesPaths.USER_PURCHASE_PAGE;
    }

    List<Drink> getDrinksFromRequest(HttpServletRequest request) {
        // extracting drinks quantity
        Map<Integer, Integer> drinkQuantityByIds = formExtractor.getDrinksQuantityByIdFromRequest(request);

		// loading entities from bd
        List<Drink> drinks = drinkService.getAllBaseByIdSet(drinkQuantityByIds.keySet());

		// setting actual drink quantities gotten from request
        setDrinkQuantities(drinks, drinkQuantityByIds);

		// extracting quantity of addons in drinks
        Map<Integer, Map<Integer, Integer>> addonsQuantityInDrinksById =
                formExtractor.getAddonsQuantityInDrinksByIdFromRequest(request);

		// setting actual drink quantities gotten from request
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

package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.controller.impl.command.helper.UserPurchaseCommandHelper;
import coffee.machine.controller.impl.command.request.data.extractor.ItemsStringFormDataExtractor;
import coffee.machine.controller.impl.command.request.data.extractor.PurchaseFormDataExtractor;
import coffee.machine.controller.impl.command.request.data.extractor.impl.ItemsStringFormDataExtractorImpl;
import coffee.machine.controller.impl.command.request.data.extractor.impl.PurchaseFormExtractorImpl;
import coffee.machine.i18n.message.key.CommandKey;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.service.AccountService;
import coffee.machine.service.CoffeeMachineService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.exception.ServiceException;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.CoffeeMachineServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserPurchaseSubmitCommand extends CommandExecuteWrapper {
    private static final Logger logger = Logger.getLogger(UserPurchaseSubmitCommand.class);
    private static final String USER_WITH_ID_MADE_PURCHASE_FORMAT = "User with id = %d made purchase: %s";

    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AccountService accountService = AccountServiceImpl.getInstance();
    private CoffeeMachineService coffeeMachine = CoffeeMachineServiceImpl.getInstance();

    private PurchaseFormDataExtractor formExtractor = new PurchaseFormExtractorImpl();
    private ItemsStringFormDataExtractor formStringDataExtractor = new ItemsStringFormDataExtractorImpl();
    private UserPurchaseCommandHelper helper = new UserPurchaseCommandHelper();


    public UserPurchaseSubmitCommand() {
        super(PagesPaths.USER_PURCHASE_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        helper.setGeneralRegisterPageAttributes(request);
        request.setAttribute(Attributes.PREVIOUS_VALUES_TABLE,
                formStringDataExtractor.getAllItemParameterValuesFromRequest(request));

        int userId = (int) request.getSession().getAttribute(Attributes.USER_ID);

        try {

            List<Drink> drinksToBuy = getDrinksFromRequest(request);
            Order order = coffeeMachine.prepareDrinksForUser(drinksToBuy, userId);
            request.setAttribute(Attributes.ORDER, order);

            logger.info(String.format(USER_WITH_ID_MADE_PURCHASE_FORMAT, userId, order));
        } catch (ServiceException e) {
            placeNecessaryDataToRequest(request, userId);
            throw e;
        }

        request.setAttribute(Attributes.USUAL_MESSAGE, CommandKey.PURCHASE_THANKS_MESSAGE);
        placeNecessaryDataToRequest(request, userId);

        request.removeAttribute(Attributes.PREVIOUS_VALUES_TABLE);  //clearing form in case of success purchase

        return PagesPaths.USER_PURCHASE_PAGE;
    }

    List<Drink> getDrinksFromRequest(HttpServletRequest request) {

        Map<Integer, Integer> drinkQuantityByIds = formExtractor.getDrinksQuantityByIdFromRequest(request);
        List<Drink> drinks = drinkService.getAllBaseByIdSet(drinkQuantityByIds.keySet());
        setDrinkQuantities(drinks, drinkQuantityByIds);
        Map<Integer, Map<Integer, Integer>> addonsQuantityInDrinksById =
                formExtractor.getAddonsQuantityInDrinksByIdFromRequest(request);
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

    private void placeNecessaryDataToRequest(HttpServletRequest request, int userId) {
        request.setAttribute(Attributes.DRINKS, drinkService.getAll());
        request.setAttribute(Attributes.USER_ACCOUNT, accountService.getByUserId(userId));
    }

}

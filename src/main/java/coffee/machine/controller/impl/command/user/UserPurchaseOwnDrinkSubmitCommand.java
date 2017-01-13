package coffee.machine.controller.impl.command.user;

import coffee.machine.controller.impl.command.CommandExecuteWrapper;
import coffee.machine.controller.impl.command.helper.UserPurchaseCommandHelper;
import coffee.machine.controller.impl.command.request.data.extractor.ItemsStringFormDataExtractor;
import coffee.machine.controller.impl.command.request.data.extractor.PurchaseFormDataExtractor;
import coffee.machine.controller.impl.command.request.data.extractor.PurchaseOwnDrinkFormExtractor;
import coffee.machine.controller.impl.command.request.data.extractor.impl.ItemsStringFormDataExtractorImpl;
import coffee.machine.controller.impl.command.request.data.extractor.impl.PurchaseFormExtractorImpl;
import coffee.machine.controller.impl.command.request.data.extractor.impl.PurchaseOwnDrinkFormExtractorImpl;
import coffee.machine.i18n.message.key.CommandKey;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.AccountService;
import coffee.machine.service.AddonService;
import coffee.machine.service.CoffeeMachineService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.exception.ServiceException;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.AddonServiceImpl;
import coffee.machine.service.impl.CoffeeMachineServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import coffee.machine.view.Parameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class UserPurchaseOwnDrinkSubmitCommand extends CommandExecuteWrapper {
    private static final Logger logger = Logger.getLogger(UserPurchaseOwnDrinkSubmitCommand.class);
    private static final String USER_WITH_ID_MADE_PURCHASE_FORMAT = "User with id = %d made purchase: %s";

    private DrinkService drinkService = DrinkServiceImpl.getInstance();
    private AddonService addonService = AddonServiceImpl.getInstance();
    private CoffeeMachineService coffeeMachine = CoffeeMachineServiceImpl.getInstance();

    private PurchaseOwnDrinkFormExtractor formExtractor = new PurchaseOwnDrinkFormExtractorImpl();
    private ItemsStringFormDataExtractor formStringDataExtractor = new ItemsStringFormDataExtractorImpl();
    private UserPurchaseCommandHelper helper = new UserPurchaseCommandHelper();


    public UserPurchaseOwnDrinkSubmitCommand() {
        super(PagesPaths.USER_PURCHASE_OWN_PAGE);
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
            placeNecessaryDataToRequest(request);
            throw e;
        }

        request.setAttribute(Attributes.USUAL_MESSAGE, CommandKey.PURCHASE_THANKS_MESSAGE);
        placeNecessaryDataToRequest(request);

        request.removeAttribute(Attributes.PREVIOUS_VALUES_TABLE);  //clearing form in case of success purchase

        return PagesPaths.USER_PURCHASE_OWN_PAGE;
    }

    List<Drink> getDrinksFromRequest(HttpServletRequest request) {
        List<Drink> drinks = new ArrayList(1);
        int drinkId = formExtractor.getBaseDrinkIdFromRequest(request);
        Drink drinkData = drinkService.getById(drinkId);
        Set<Item> addons = getAddonsFromRequest(request);
        Drink drink = new Drink.Builder()
                .id(drinkId)
                .name(drinkData.getName())
                .quantity(1)
                .price(drinkData.getPrice())
                .setAddons(addons)
                .getDrink();
        drinks.add(drink);

        return drinks;
    }

    private Set<Item> getAddonsFromRequest(HttpServletRequest request) {
        Map<Integer, Integer> addonsQuantities = formExtractor.getAddonsQuantityByIdFromRequest(request);
        List<Item> addons = addonService.getAllByIdSet(addonsQuantities.keySet());
        setAddonQuantities(addons, addonsQuantities);
        return new TreeSet(addons);
    }


    void setAddonQuantities(List<Item> addons, Map<Integer, Integer> addonQuantityByIds) {

        addons.forEach(addon -> addon.setQuantity(addonQuantityByIds.get(addon.getId())));
    }

    private void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(Attributes.DRINKS, drinkService.getAll());
        request.setAttribute(Attributes.ADDONS, addonService.getAll());
    }

}

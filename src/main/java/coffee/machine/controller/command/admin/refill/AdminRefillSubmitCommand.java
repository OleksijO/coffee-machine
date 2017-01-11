package coffee.machine.controller.command.admin.refill;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.RegExp;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.controller.command.helper.RequestDataExtractor;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.value.object.ItemReceipt;
import coffee.machine.service.AccountService;
import coffee.machine.service.AddonService;
import coffee.machine.service.RefillService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.AddonServiceImpl;
import coffee.machine.service.impl.RefillServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static coffee.machine.controller.i18n.message.key.ControllerMessageKey.ADMIN_REFILL_SUCCESSFUL;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.QUANTITY_SHOULD_BE_NON_NEGATIVE;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.TITLE_ADMIN_REFILL;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.ADMIN_REFILL_PAGE;

/**
 * This class represents admin refill page post method request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminRefillSubmitCommand extends CommandWrapperTemplate {
    private static final Logger logger = Logger.getLogger(AdminRefillSubmitCommand.class);

    private static final String ITEMS_ADDED = "Coffe" +
            "e-machine was refilled by admin id=%s. %s";

    private final DrinkService drinkService = DrinkServiceImpl.getInstance();
    private final AddonService addonService = AddonServiceImpl.getInstance();
    private final AccountService accountService = AccountServiceImpl.getInstance();

    private final Pattern PATTERN_ITEM = Pattern.compile(RegExp.REGEXP_ANY_ITEM);
    private final Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
    private final Pattern patternAddon = Pattern.compile(RegExp.REGEXP_ADDON_PARAM);

    private RequestDataExtractor dataExtractorHelper = new RequestDataExtractor();

    private RefillService coffeeMachine = RefillServiceImpl.getInstance();

    public AdminRefillSubmitCommand() {
        super(ADMIN_REFILL_PAGE);
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_ADMIN_REFILL);
        request.setAttribute(COFFEE_MACHINE_BALANCE,
                accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)
                        .orElseThrow(IllegalStateException::new)
                        .getRealAmount());
        request.setAttribute(DRINKS, drinkService.getAll());
        request.setAttribute(ADDONS, addonService.getAll());

    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) {

        saveFormData(request);

        ItemReceipt receipt = getReceiptFromRequest(request);

        coffeeMachine.refill(receipt);
        placeMessageToRequest(request);
        logRefillingDetails(request, receipt);
        clearFormData(request);

        return ADMIN_REFILL_PAGE;
    }

    private void saveFormData(HttpServletRequest request) {
        request.setAttribute(PREVIOUS_VALUES_TABLE,
                dataExtractorHelper.getParametersFromRequestByPattern(request, PATTERN_ITEM));
    }

    private ItemReceipt getReceiptFromRequest(HttpServletRequest request) {
        List<Drink> drinksToAdd = getDrinksToAddFromRequest(request);
        List<Item> addonsToAdd = getAddonsToAddFromRequest(request);
        return new ItemReceipt(drinksToAdd, addonsToAdd);
    }

    private List<Drink> getDrinksToAddFromRequest(HttpServletRequest request) {
        Map<Integer, Integer> drinksQuantitiesByIds = getItemQuantityByIdFromRequest(request, patternDrink);
        return getDrinksByIdsAndQuantities(drinksQuantitiesByIds);
    }

    private Map<Integer, Integer> getItemQuantityByIdFromRequest(HttpServletRequest request,
                                                                 Pattern itemParameterPattern) {
        Enumeration<String> params = request.getParameterNames();
        Map<Integer, Integer> itemQuantityByIds = new HashMap<>();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            Matcher matcher = itemParameterPattern.matcher(param);
            if (matcher.matches()) {
                int itemQuantity = dataExtractorHelper
                        .getIntFromRequestByParameter(request, param, QUANTITY_SHOULD_BE_NON_NEGATIVE);
                int itemId = dataExtractorHelper.getFirstNumberFromParameterName(param);
                itemQuantityByIds.put(itemId, itemQuantity);
            }
        }
        return itemQuantityByIds;
    }

    private List<Drink> getDrinksByIdsAndQuantities(Map<Integer, Integer> drinkQuantityByIds) {
        return drinkQuantityByIds.entrySet().stream()
                .map(entry -> new Drink.Builder()
                        .setId(entry.getKey())
                        .setQuantity(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private List<Item> getAddonsToAddFromRequest(HttpServletRequest request) {
        Map<Integer, Integer> addonAddQuantityByIds = getItemQuantityByIdFromRequest(request, patternAddon);
        return getAddonsByIdsAndQuantities(addonAddQuantityByIds);
    }

    private List<Item> getAddonsByIdsAndQuantities(Map<Integer, Integer> addonsQuantityByIds) {
        return addonsQuantityByIds.entrySet().stream()
                .map(entry -> new Item.Builder()
                        .setId(entry.getKey())
                        .setQuantity(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private void placeMessageToRequest(HttpServletRequest request) {
        request.setAttribute(USUAL_MESSAGE, ADMIN_REFILL_SUCCESSFUL);
    }

    private void logRefillingDetails(HttpServletRequest request, ItemReceipt receipt) {
        logger.info(String.format(ITEMS_ADDED, request.getSession().getAttribute(ADMIN_ID).toString(),
                receipt));
    }

    private void clearFormData(HttpServletRequest request) {
        request.removeAttribute(PREVIOUS_VALUES_TABLE);
    }

    public void setCoffeeMachineRefillService(RefillService coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }
}

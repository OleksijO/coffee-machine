package coffee.machine.controller.command.admin.refill;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.RegExp;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.controller.command.request.data.extractor.ItemsStringFormDataExtractor;
import coffee.machine.controller.command.request.data.extractor.impl.ItemsStringFormDataExtractorImpl;
import coffee.machine.controller.exception.ControllerException;
import coffee.machine.i18n.message.key.GeneralKey;
import coffee.machine.i18n.message.key.error.CommandErrorKey;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.entity.item.ItemReceipt;
import coffee.machine.service.AccountService;
import coffee.machine.service.AddonService;
import coffee.machine.service.CoffeeMachineRefillService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.AddonServiceImpl;
import coffee.machine.service.impl.CoffeeMachineRefillServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
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

import static coffee.machine.i18n.message.key.CommandKey.ADMIN_REFILL_SUCCESSFUL;
import static coffee.machine.i18n.message.key.GeneralKey.TITLE_ADMIN_REFILL;
import static coffee.machine.i18n.message.key.error.CommandErrorKey.ADMIN_REFILL_NOTHING_TO_ADD;
import static coffee.machine.view.Attributes.*;

/**
 * This class represents admin refill page post method request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminRefillSubmitCommand extends CommandWrapperTemplate {
    private static final Logger logger = Logger.getLogger(AdminRefillSubmitCommand.class);

    private static final String ITEMS_ADDED = "Coffee-machine was refilled by admin id=%d. %s";
    private static final String PROBLEMS_WITH_PARSING_INT_FROM_PARAMETER_FORMAT =
            "Problems with parsing INT from parameter '%s', its value ='%s'";

    private final CoffeeMachineRefillService coffeeMachine = CoffeeMachineRefillServiceImpl.getInstance();
    private final DrinkService drinkService = DrinkServiceImpl.getInstance();
    private final AddonService addonService = AddonServiceImpl.getInstance();
    private final AccountService accountService = AccountServiceImpl.getInstance();

    private final ItemsStringFormDataExtractor formStringDataExtractor = new ItemsStringFormDataExtractorImpl();

    private final Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
    private final Pattern patternAddon = Pattern.compile(RegExp.REGEXP_ADDON_PARAM);
    private final Pattern patternNumber = Pattern.compile(RegExp.REGEXP_NUMBER);

    public AdminRefillSubmitCommand() {
        super(PagesPaths.ADMIN_REFILL_PAGE);
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_ADMIN_REFILL);
        request.setAttribute(COFFEE_MACHINE_BALANCE, accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)
                .getRealAmount());
        request.setAttribute(DRINKS, drinkService.getAll());
        request.setAttribute(ADDONS, addonService.getAll());

    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) {

        saveFormData(request);

        ItemReceipt receipt = getReceiptFromRequest(request);
        if (receipt.isEmpty()) {
            request.setAttribute(ERROR_MESSAGE, ADMIN_REFILL_NOTHING_TO_ADD);
        } else {
            coffeeMachine.refill(receipt);
            request.setAttribute(USUAL_MESSAGE, ADMIN_REFILL_SUCCESSFUL);
            logRefillingDetails(request, receipt);
            clearFormData(request);
        }

        return PagesPaths.ADMIN_REFILL_PAGE;

    }

    private void saveFormData(HttpServletRequest request) {
        request.setAttribute(Attributes.PREVIOUS_VALUES_TABLE,
                formStringDataExtractor.getAllItemParameterValuesFromRequest(request));
    }

    private ItemReceipt getReceiptFromRequest(HttpServletRequest request){
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
                int itemQuantity = getIntFromRequestByParameter(param, request);
                int itemId = getItemIdFromParam(param);
                itemQuantityByIds.put(itemId, itemQuantity);
            }
        }
        return itemQuantityByIds;
    }

    private int getIntFromRequestByParameter(String param, HttpServletRequest request) {
        try {

            return Integer.parseInt(request.getParameter(param));

        } catch (Exception e) {
            logger.error(String.format(PROBLEMS_WITH_PARSING_INT_FROM_PARAMETER_FORMAT,
                    param, request.getParameter(param)));
            throw new ControllerException(CommandErrorKey.QUANTITY_SHOULD_BE_INT);
        }
    }

    private int getItemIdFromParam(String param) {
        Matcher matcher = patternNumber.matcher(param);
        if (matcher.find(0)) {

            return Integer.parseInt(param.substring(matcher.start(), matcher.end()));

        } else {
            throw new ControllerException(GeneralKey.ERROR_UNKNOWN); //this normally should not ever happen
        }
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

    private void logRefillingDetails(HttpServletRequest request, ItemReceipt receipt) {
        logger.info(String.format(ITEMS_ADDED, (int) request.getSession().getAttribute(Attributes.ADMIN_ID),
                receipt));
    }

    private void clearFormData(HttpServletRequest request) {
        request.removeAttribute(Attributes.PREVIOUS_VALUES_TABLE);
    }

}

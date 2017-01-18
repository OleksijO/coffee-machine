package coffee.machine.controller.command.admin.refill;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.RegExp;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.controller.command.helper.RequestDataExtractor;
import coffee.machine.controller.validation.Notification;
import coffee.machine.controller.validation.ProductsReceiptValidator;
import coffee.machine.controller.validation.Validator;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.model.value.object.ProductsReceipt;
import coffee.machine.service.AccountService;
import coffee.machine.service.AddonService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.RefillService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.AddonServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;
import coffee.machine.service.impl.RefillServiceImpl;
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
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.TITLE_ADMIN_REFILL;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.QUANTITY_SHOULD_BE_NON_NEGATIVE;
import static coffee.machine.view.Attributes.*;
import static coffee.machine.view.PagesPaths.ADMIN_REFILL_PAGE;

/**
 * This class represents admin refill page post method request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminRefillSubmitCommand extends CommandWrapperTemplate {
    private static final Logger logger = Logger.getLogger(AdminRefillSubmitCommand.class);

    private static final String PRODUCTS_ADDED = "Coffee-machine was refilled by admin id=%s. %s";

    private final DrinkService drinkService = DrinkServiceImpl.getInstance();
    private final AddonService addonService = AddonServiceImpl.getInstance();
    private final AccountService accountService = AccountServiceImpl.getInstance();

    private final Pattern PATTERN_PRODUCT = Pattern.compile(RegExp.REGEXP_ANY_PRODUCT);
    private final Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
    private final Pattern patternAddon = Pattern.compile(RegExp.REGEXP_ADDON_PARAM);

    private RequestDataExtractor dataExtractorHelper = new RequestDataExtractor();
    private Validator<ProductsReceipt> productsReceiptValidator = new ProductsReceiptValidator();

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

        ProductsReceipt receipt = getReceiptFromRequest(request);
        receipt.clearProductsWithZeroQuantity();

        Notification notification = productsReceiptValidator.validate(receipt);
        if (notification.hasMessages()) {
            processValidationErrors(notification, request);
            return ADMIN_REFILL_PAGE;
        }

        coffeeMachine.refill(receipt);
        placeMessageToRequest(request);
        logRefillingDetails(request, receipt);
        clearFormData(request);

        return ADMIN_REFILL_PAGE;
    }

    private void saveFormData(HttpServletRequest request) {
        request.setAttribute(PREVIOUS_VALUES_TABLE,
                dataExtractorHelper.getParametersFromRequestByPattern(request, PATTERN_PRODUCT));
    }

    private ProductsReceipt getReceiptFromRequest(HttpServletRequest request) {
        List<Drink> drinksToAdd = getDrinksToAddFromRequest(request);
        List<Product> addonsToAdd = getAddonsToAddFromRequest(request);
        return new ProductsReceipt(drinksToAdd, addonsToAdd);
    }

    private List<Drink> getDrinksToAddFromRequest(HttpServletRequest request) {
        Map<Integer, Integer> drinksQuantitiesByIds = getProductQuantityByIdFromRequest(request, patternDrink);
        return getDrinksByIdsAndQuantities(drinksQuantitiesByIds);
    }

    private Map<Integer, Integer> getProductQuantityByIdFromRequest(HttpServletRequest request,
                                                                    Pattern productParameterPattern) {
        Enumeration<String> params = request.getParameterNames();
        Map<Integer, Integer> productQuantityByIds = new HashMap<>();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            Matcher matcher = productParameterPattern.matcher(param);
            if (matcher.matches()) {
                int productQuantity = dataExtractorHelper
                        .getIntFromRequestByParameter(request, param, QUANTITY_SHOULD_BE_NON_NEGATIVE);
                int productId = dataExtractorHelper.getFirstNumberFromParameterName(param);
                productQuantityByIds.put(productId, productQuantity);
            }
        }
        return productQuantityByIds;
    }

    private List<Drink> getDrinksByIdsAndQuantities(Map<Integer, Integer> drinkQuantityByIds) {
        return drinkQuantityByIds.entrySet().stream()
                .map(entry ->
                        new Drink.Builder()
                                .setId(entry.getKey())
                                .setQuantity(entry.getValue())
                                .build())
                .collect(Collectors.toList());
    }

    private List<Product> getAddonsToAddFromRequest(HttpServletRequest request) {
        Map<Integer, Integer> addonAddQuantityByIds = getProductQuantityByIdFromRequest(request, patternAddon);
        return getAddonsByIdsAndQuantities(addonAddQuantityByIds);
    }

    private List<Product> getAddonsByIdsAndQuantities(Map<Integer, Integer> addonsQuantityByIds) {
        return addonsQuantityByIds.entrySet().stream()
                .map(entry -> new Product.Builder()
                        .setId(entry.getKey())
                        .setQuantity(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private void placeMessageToRequest(HttpServletRequest request) {
        request.setAttribute(USUAL_MESSAGE, ADMIN_REFILL_SUCCESSFUL);
    }

    private void logRefillingDetails(HttpServletRequest request, ProductsReceipt receipt) {
        logger.info(String.format(PRODUCTS_ADDED, request.getSession().getAttribute(ADMIN_ID).toString(),
                receipt));
    }

    private void clearFormData(HttpServletRequest request) {
        request.removeAttribute(PREVIOUS_VALUES_TABLE);
    }

    public void setCoffeeMachineRefillService(RefillService coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    public void setProductsReceiptValidator(Validator<ProductsReceipt> productsReceiptValidator) {
        this.productsReceiptValidator = productsReceiptValidator;
    }
}

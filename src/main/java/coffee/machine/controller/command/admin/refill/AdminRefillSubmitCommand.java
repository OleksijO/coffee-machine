package coffee.machine.controller.command.admin.refill;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.command.CommandWrapperTemplate;
import coffee.machine.controller.command.request.data.extractor.ItemsStringFormDataExtractor;
import coffee.machine.controller.command.request.data.extractor.RefillFormDataExtractor;
import coffee.machine.controller.command.request.data.extractor.impl.ItemsStringFormDataExtractorImpl;
import coffee.machine.controller.command.request.data.extractor.impl.RefillFormExtractorImpl;
import coffee.machine.i18n.message.key.CommandKey;
import coffee.machine.i18n.message.key.error.CommandErrorKey;
import coffee.machine.service.AccountService;
import coffee.machine.service.AddonService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.impl.AccountServiceImpl;
import coffee.machine.service.impl.AddonServiceImpl;
import coffee.machine.service.impl.DrinkServiceImpl;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static coffee.machine.i18n.message.key.GeneralKey.TITLE_ADMIN_REFILL;
import static coffee.machine.view.Attributes.*;

/**
 * This class represents admin refill page post method request handler command.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AdminRefillSubmitCommand extends CommandWrapperTemplate {
    private static final Logger logger = Logger.getLogger(AdminRefillSubmitCommand.class);
    private static final String ITEMS_ADDED = "Coffee-machine was refilled by admin id=%d. Added drinks: %s, addons %s";

    private final DrinkService drinkService = DrinkServiceImpl.getInstance();
    private final AddonService addonService = AddonServiceImpl.getInstance();
    private final AccountService accountService = AccountServiceImpl.getInstance();

    private final RefillFormDataExtractor formDataExtractor = new RefillFormExtractorImpl();
    private final ItemsStringFormDataExtractor formStringDataExtractor = new ItemsStringFormDataExtractorImpl();

    public AdminRefillSubmitCommand() {
        super(PagesPaths.ADMIN_REFILL_PAGE);
    }

    @Override
    protected String performExecute(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute(Attributes.PREVIOUS_VALUES_TABLE,
                formStringDataExtractor.getAllItemParameterValuesFromRequest(request));


        Map<Integer, Integer> drinkAddQuantityByIds = formDataExtractor.getDrinksQuantityByIdFromRequest(request);
        Map<Integer, Integer> addonAddQuantityByIds = formDataExtractor.getAddonsQuantityByIdFromRequest(request);


        boolean anyItemWereAdded = performRefilling(drinkAddQuantityByIds, addonAddQuantityByIds);

        if (anyItemWereAdded) {
            request.setAttribute(USUAL_MESSAGE, CommandKey.ADMIN_REFILL_SUCCESSFUL);
            logger.info(String.format(ITEMS_ADDED, (int) request.getSession().getAttribute(Attributes.ADMIN_ID),
                    drinkAddQuantityByIds, addonAddQuantityByIds));
        } else {
            request.setAttribute(ERROR_MESSAGE, CommandErrorKey.ADMIN_REFILL_NOTHING_TO_ADD);
        }


        request.removeAttribute(Attributes.PREVIOUS_VALUES_TABLE);     //clearing form in case of success refilling

        return PagesPaths.ADMIN_REFILL_PAGE;
    }

    private boolean performRefilling(Map<Integer, Integer> drinkAddQuantityByIds,
                                     Map<Integer, Integer> addonAddQuantityByIds) {
        boolean anyItemWereAdded = false;
        if ((drinkAddQuantityByIds != null) && (drinkAddQuantityByIds.size() > 0)) {
            drinkService.refill(drinkAddQuantityByIds);
            anyItemWereAdded = true;

        }
        if ((addonAddQuantityByIds != null) && (addonAddQuantityByIds.size() > 0)) {
            addonService.refill(addonAddQuantityByIds);
            anyItemWereAdded = true;
        }
        return anyItemWereAdded;
    }

    @Override
    protected void placeNecessaryDataToRequest(HttpServletRequest request) {
        request.setAttribute(PAGE_TITLE, TITLE_ADMIN_REFILL);

        request.setAttribute(COFFEE_MACHINE_BALANCE, accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)
                .getRealAmount());
        request.setAttribute(DRINKS, drinkService.getAll());
        request.setAttribute(ADDONS, addonService.getAll());

    }
}
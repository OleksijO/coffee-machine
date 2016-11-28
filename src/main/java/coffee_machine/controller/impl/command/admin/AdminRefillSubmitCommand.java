package coffee_machine.controller.impl.command.admin;

import coffee_machine.CoffeeMachineConfig;
import coffee_machine.controller.Command;
import coffee_machine.controller.RegExp;
import coffee_machine.controller.exception.ControllerException;
import coffee_machine.controller.impl.command.abstracts.AbstractCommand;
import coffee_machine.exception.ApplicationException;
import coffee_machine.i18n.message.key.CommandKey;
import coffee_machine.i18n.message.key.GeneralKey;
import coffee_machine.i18n.message.key.error.CommandErrorKey;
import coffee_machine.service.AccountService;
import coffee_machine.service.AddonService;
import coffee_machine.service.DrinkService;
import coffee_machine.service.impl.AccountServiceImpl;
import coffee_machine.service.impl.AddonServiceImpl;
import coffee_machine.service.impl.DrinkServiceImpl;
import coffee_machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static coffee_machine.view.Attributes.*;
import static coffee_machine.view.PagesPaths.ADMIN_REFILL_PAGE;

public class AdminRefillSubmitCommand extends AbstractCommand implements Command {
    private static final Logger logger = Logger.getLogger(AdminRefillCommand.class);

    private final DrinkService drinkService = DrinkServiceImpl.getInstance();
    private final AddonService addonService = AddonServiceImpl.getInstance();
    private final AccountService accountService = AccountServiceImpl.getInstance();

    private final Pattern patternNumber = Pattern.compile(RegExp.REGEXP_NUMBER);
    private final Pattern patternDrink = Pattern.compile(RegExp.REGEXP_DRINK_PARAM);
    private final Pattern patternAddon = Pattern.compile(RegExp.REGEXP_ADDON_PARAM);

    public AdminRefillSubmitCommand() {
        super(logger);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(Attributes.PAGE_TITLE, GeneralKey.TITLE_ADMIN_REFILL);
        try {
            Map<Integer, Integer> drinkAddQuantityByIds = getGoodsQuantityByIdFromRequest(request, patternDrink);
            Map<Integer, Integer> addonAddQuantityByIds = getGoodsQuantityByIdFromRequest(request, patternAddon);

            boolean drinksAdded = false;
            boolean addonsAdded = false;
            if ((drinkAddQuantityByIds != null) && (drinkAddQuantityByIds.size() > 0)) {
                drinkService.refill(drinkAddQuantityByIds);
                drinksAdded = true;
            }
            if ((addonAddQuantityByIds != null) && (addonAddQuantityByIds.size() > 0)) {
                addonService.refill(addonAddQuantityByIds);
                addonsAdded = true;
            }

            if (drinksAdded || addonsAdded) {
                request.setAttribute(USUAL_MESSAGE, CommandKey.ADMIN_REFILL_SUCCESSFULL);

            } else {
                request.setAttribute(ERROR_MESSAGE, CommandErrorKey.ADMIN_REFILL_NOTHING_TO_ADD);
            }

            request.setAttribute(COFFEE_MACHINE_BALANCE, accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)
                    .getRealAmount());
            request.setAttribute(REFILL_DRINKS, drinkService.getAll());
            request.setAttribute(REFILL_ADDONS, addonService.getAll());

        } catch (ApplicationException e) {
            logApplicationError(e);
            request.setAttribute(ERROR_MESSAGE, e.getMessage());
            request.setAttribute(ERROR_ADDITIONAL_MESSAGE, e.getAdditionalMessage());
        } catch (Exception e) {
            logError(e);
            request.setAttribute(ERROR_MESSAGE, GeneralKey.ERROR_UNKNOWN);
        }


        return ADMIN_REFILL_PAGE;
    }

    private Map<Integer, Integer> getGoodsQuantityByIdFromRequest(HttpServletRequest request,
                                                                  Pattern goodsParameterPattern) {
        Enumeration<String> params = request.getParameterNames();
        Map<Integer, Integer> goodsQuantityByIds = new HashMap<>();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            Matcher matcher = goodsParameterPattern.matcher(param);
            if (matcher.matches()) {
                int goodsQuantity = getIntFromRequestByParameter(param, request);
                if (goodsQuantity > 0) {
                    int goodsId = getGoodsIdFromParam(param);
                    goodsQuantityByIds.put(goodsId, goodsQuantity);
                } else if (goodsQuantity < 0) {
                    throw new ControllerException(CommandErrorKey.QUANTITY_SHOULD_BE_NON_NEGATIVE);
                }
            }
        }
        return goodsQuantityByIds;
    }


    private int getIntFromRequestByParameter(String param, HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter(param));
        } catch (Exception e) {
            throw new ControllerException(CommandErrorKey.QUANTITY_SHOULD_BE_INT);
        }

    }

    private int getGoodsIdFromParam(String param) {
        Matcher matcher = patternNumber.matcher(param);
        if (matcher.find(0)) {
            return Integer.parseInt(param.substring(matcher.start(), matcher.end()));
        } else {
            throw new ControllerException(GeneralKey.ERROR_UNKNOWN);
        }
    }

}

package coffee.machine.service.impl;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class CoffeeMachineServiceImplTest {
//    private static final String LIST_OF_ADDONS_TO_UPDATE_SHOULD_BE_NOT_NULL =
//            "List of addons to update should be not null";
//    private static final String LIST_OF_DRINKS_TO_UPDATE_SHOULD_BE_NOT_NULL =
//            "List of drinks to update should be not null";
//    private static final String ADDON_QUANTITY_SHOULD_DECREASE_FORMAT =
//            "Addon quantity should decrease on %d, addon id=%d";
//    private static final String DRINK_QUANTITY_SHOULD_DECREASE_FORMAT =
//            "Drink quantity should decrease on %d, drink id=%d";
//    private static final String HERE_SHOULD_BE_APPLICATION_EXCEPTION = "Here should be application exception";
//    private static final String COFFEE_MACHINE_BALANCE_MISMATCH = "CoffeeMachine balance mismatch";
//    private static final String USER_BALANCE_MISMATCH = "User balance mismatch";
//
//    @Mock
//    private DaoFactory daoFactory;
//    @Mock
//    private DrinkDao drinkDao;
//    @Mock
//    private AddonDao addonDao;
//    @Mock
//    private AccountDao accountDao;
//    @Mock
//    private OrderDao orderDao;
//    @Mock
//    private AbstractConnection connection;
//    @Captor
//    private ArgumentCaptor<Account> accountCaptor;
//    @Captor
//    private ArgumentCaptor<List<Drink>> drinkListCaptor;
//    @Captor
//    private ArgumentCaptor<List<Item>> addonListCaptor;
//
//    private CoffeeMachineOrderService service;
//
//    Map<Integer, Integer> drinkQuantitiesById = DrinksData.getQuantitiesByIds();
//    Map<Integer, Integer> addonQuantitiesById = AddonsData.getQuantitiesByIds();
//
//    Account coffeeMachineAccount = Accounts.COFFEE_MACHINE.getCopy();
//    Account userAccount = Accounts.USER_A.getCopy();
//
//    List<Drink> drinksToBuy;
//    long sumAmount;
//    long userAccountInitialAmount;
//    long cmAccountAmount;
//    int drinkQuantity;
//    int addonQuantity;
//    int userId;
//
//
//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//
//        when(daoFactory.getAccountDao(connection)).thenReturn(accountDao);
//        when(daoFactory.getOrderDao(connection)).thenReturn(orderDao);
//        when(daoFactory.getAddonDao(connection)).thenReturn(addonDao);
//        when(daoFactory.getDrinkDao(connection)).thenReturn(drinkDao);
//        when(daoFactory.getConnection()).thenReturn(connection);
//
//
//        when(accountDao.getByUserId(2)).thenReturn(java.util.Optional.ofNullable(userAccount));
//        when(accountDao.getById(1)).thenReturn(coffeeMachineAccount);
//
//        List<Drink> baseDrinksToBuy = new ArrayList<>();
//        baseDrinksToBuy.add(DrinksData.BORJOMI.drink);
//        baseDrinksToBuy.add(DrinksData.ESPRESSO.drink);
//        baseDrinksToBuy.add(DrinksData.MOCACCINO.drink);
//        when(drinkDao.getAllFromList(any())).thenReturn(baseDrinksToBuy);
//
//        List<Item> addonsToBuy = new ArrayList<>();
//        addonsToBuy.add(AddonsData.MILK.addon);
//        addonsToBuy.add(AddonsData.SUGAR.addon);
//        when(addonDao.getAllFromList(any())).thenReturn(addonsToBuy);
//
//        service = CoffeeMachineOrderServiceImpl.getInstance();
//        ((CoffeeMachineOrderServiceImpl)service).daoFactory = daoFactory;
//
//        userAccountInitialAmount = userAccount.getAmount();
//        cmAccountAmount = coffeeMachineAccount.getAmount();
//    }
//
//
//
//
//    @Test
//    public void testPrepareDrinksForUserDrinksToBuyWithoutAddons() throws Exception {
//
//        prepareDataForTestDrinksWithoutAddons(2, 2);
//
//        service.prepareOrder(drinksToBuy);
//
//        verifyDaoAccessionTimesAndCaptureCalledMethodArgs(1, 0, 2);
//        verifyTestResultsDrinksWithoutAddons();
//
//    }
//
//    /**
//     * @param drinkQuantity     Quantity of each test drink to buy
//     * @param userId            User's id, who purchases drinks
//     */
//    private void prepareDataForTestDrinksWithoutAddons(int drinkQuantity, int userId) {
//
//        this.drinkQuantity = drinkQuantity;
//        this.userId = userId;
//        drinksToBuy = getTestDrinksWithoutAddons();
//        setDrinksQuantity(drinksToBuy, drinkQuantity);
//        sumAmount = getSumAmount(drinksToBuy);
//    }
//
//    private void setDrinksQuantity(List<Drink> drinks, int quantity) {
//        for (Drink drink : drinks) {
//            drink.setQuantity(quantity);
//        }
//    }
//
//    private long getSumAmount(List<Drink> drinks) {
//        long sumAmount = 0;
//        for (Drink drink : drinks) {
//            sumAmount += drink.getTotalPrice() * drink.getQuantity();
//        }
//        return sumAmount;
//    }
//
//    private List<Drink> getTestDrinksWithoutAddons() {
//        List<Drink> drinks = new ArrayList<>();
//        drinks.add(DrinksData.BORJOMI.getCopy().getBaseDrink());
//        drinks.add(DrinksData.ESPRESSO.getCopy().getBaseDrink());
//        drinks.add(DrinksData.MOCACCINO.getCopy().getBaseDrink());
//        return drinks;
//    }
//
//    /**
//     * Verifies, that correct data was directed to DAO layer
//     */
//    private void verifyTestResultsDrinksWithoutAddons() {
//        List<Account> capturedAccounts = accountCaptor.getAllValues();
//        Account cmAccount = capturedAccounts.get(0);
//        Account userAccount = capturedAccounts.get(1);
//        List<Drink> updatedDrinks = drinkListCaptor.getValue();
//
//        assertNotNull(LIST_OF_DRINKS_TO_UPDATE_SHOULD_BE_NOT_NULL, updatedDrinks);
//        updatedDrinks.forEach(drink1 ->
//                assertEquals(String.format(DRINK_QUANTITY_SHOULD_DECREASE_FORMAT,drinkQuantity,drink1.getId()),
//                        drinkQuantitiesById.get(drink1.getId()) - drinkQuantity,
//                        drink1.getQuantity()));
//
//        assertEquals(COFFEE_MACHINE_BALANCE_MISMATCH, cmAccountAmount + sumAmount, cmAccount.getAmount());
//        assertEquals(USER_BALANCE_MISMATCH, userAccountInitialAmount - sumAmount, userAccount.getAmount());
//    }
//
//    // Verifies, that each dao was called exactly specified number of times and captures called method args
//    private void verifyDaoAccessionTimesAndCaptureCalledMethodArgs(int drinkTimes, int addonTimes, int accountTimes) {
//        verify(drinkDao, times(drinkTimes)).updateQuantityAllInList(drinkListCaptor.capture());
//        verify(addonDao, times(addonTimes)).updateQuantityAllInList(addonListCaptor.capture());
//        verify(accountDao, times(accountTimes)).update(accountCaptor.capture());
//    }
//
//    @Test
//    public void testPrepareDrinksForUserDrinksToBuyWithAddons() throws Exception {
//        prepareDataForTestDrinkWithAddons(4, 1, 2);
//
//        service.prepareOrder(drinksToBuy);
//
//        verifyDaoAccessionTimesAndCaptureCalledMethodArgs(1, 1, 2);
//        verifyTestResultsDrinksWithoutAddons();
//        verifyTestResultsDrinksWithAddons();
//    }
//
//    /**
//     * @param drinkQuantity     Quantity of each test drink to buy
//     * @param addonQuantity     Quantity of each addon in each drink to buy
//     * @param userId            User's id, who purchases drinks
//     */
//    private void prepareDataForTestDrinkWithAddons(int drinkQuantity, int addonQuantity, int userId) {
//        prepareDataForTestDrinksWithoutAddons(drinkQuantity, userId);
//        this.addonQuantity = addonQuantity;
//        drinksToBuy = getTestDrinksWithAddons(addonQuantity);
//        setDrinksQuantity(drinksToBuy, drinkQuantity);
//        sumAmount = getSumAmount(drinksToBuy);
//    }
//
//    /**
//     * Verifies, that correct data was directed to DAO layer
//     */
//    private void verifyTestResultsDrinksWithAddons() {
//        List<Item> updatedAddons = addonListCaptor.getValue();
//        assertNotNull(LIST_OF_ADDONS_TO_UPDATE_SHOULD_BE_NOT_NULL, updatedAddons);
//        if (addonQuantity > 0) {
//            updatedAddons.forEach(addon ->
//                    assertEquals(String.format(ADDON_QUANTITY_SHOULD_DECREASE_FORMAT,addonQuantity, addon.getId()),
//                            addonQuantitiesById.get(addon.getId()) - addonQuantity * drinkQuantity,
//                            addon.getQuantity()));
//        }
//    }
//
//    private List<Drink> getTestDrinksWithAddons(int addonQuantity) {
//        List<Drink> drinks = new ArrayList<>();
//        drinks.add(DrinksData.BORJOMI.drink.getBaseDrink());
//        Drink drink = DrinksData.ESPRESSO.drink.getBaseDrink();
//        drink.getAddons().iterator().next().setQuantity(addonQuantity);
//        drinks.add(drink);
//        drink = DrinksData.MOCACCINO.drink.getBaseDrink();
//        Iterator<Item> iterator = drink.getAddons().iterator();
//        iterator.next();
//        iterator.next().setQuantity(addonQuantity);
//        drinks.add(drink);
//        return drinks;
//
//    }
//
//    @Test
//    public void testPrepareDrinksForUserNotEnoughMoney() throws Exception {
//
//        prepareDataForTestDrinkWithAddons(4, 1, 2);
//        userAccount.withdraw(userAccountInitialAmount);
//
//        try {
//            service.prepareOrder(drinksToBuy);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            Assert.assertEquals(NOT_ENOUGH_MONEY, e.getMessageKey());
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail(HERE_SHOULD_BE_APPLICATION_EXCEPTION);
//        } finally {
//            userAccount.add(userAccountInitialAmount);
//        }
//
//        verifyDaoAccessionTimesAndCaptureCalledMethodArgs(0, 0, 0);
//    }
//
//    @Test
//    public void testPrepareDrinksForUserNotEnoughDrinks() throws Exception {
//
//        prepareDataForTestDrinkWithAddons(4, 1, 2);
//        int memory = DrinksData.ESPRESSO.drink.getQuantity();
//        DrinksData.ESPRESSO.drink.setQuantity(0);
//        try {
//            service.prepareOrder(drinksToBuy);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            Assert.assertEquals(ITEM_NO_LONGER_AVAILABLE, e.getMessageKey());
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail(HERE_SHOULD_BE_APPLICATION_EXCEPTION);
//        } finally {
//            DrinksData.ESPRESSO.drink.setQuantity(memory);
//        }
//
//        verifyDaoAccessionTimesAndCaptureCalledMethodArgs(0, 0, 0);
//    }
//
//    @Test
//    public void testPrepareDrinksForUserEmptyDrinks() throws Exception {
//        prepareDataForTestDrinkWithAddons(4, 1, 2);
//        drinksToBuy.clear();
//        try {
//            service.prepareOrder(drinksToBuy);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            Assert.assertEquals(YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY, e.getMessageKey());
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail(HERE_SHOULD_BE_APPLICATION_EXCEPTION);
//        }
//
//        verifyDaoAccessionTimesAndCaptureCalledMethodArgs(0, 0, 0);
//    }

}
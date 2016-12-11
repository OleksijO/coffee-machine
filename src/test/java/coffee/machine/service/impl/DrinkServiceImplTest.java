package coffee.machine.service.impl;

import coffee.machine.model.entity.Account;
import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.DrinkDao;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.service.DrinkService;
import data.test.entity.Drinks;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class DrinkServiceImplTest {
    private static final String DRINK_QUANTITY_MISMATCH = " drink quantity mismatch";
    private static final String FIRST = "First";
    private static final String SECOND = "Second";
    private static final String THE_ONLY = "The only";

    @Mock
    private DaoFactory daoFactory;
    @Mock
    private DrinkDao drinkDao;

    @Mock
    private AbstractConnection connection;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<List<Drink>> drinkListCaptor;

    private DrinkService service;

    List<Drink> drinksToUpdate;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(daoFactory.getDrinkDao(connection)).thenReturn(drinkDao);
        when(daoFactory.getConnection()).thenReturn(connection);

        when(drinkDao.getAllFromList(any())).thenReturn(drinksToUpdate);

        service = DrinkServiceImpl.getInstance();
        DrinkServiceImpl.daoFactory = daoFactory;

    }


    @Test
    public void testRefillEmpty() throws Exception {
        service.refill(null);
        verify(daoFactory, times(0)).getDrinkDao(connection);
        service.refill(new HashMap<>());
        verify(daoFactory, times(0)).getDrinkDao(connection);

    }

    @Test
    public void testRefillTwo() throws Exception {
        drinksToUpdate = new ArrayList<>();
        drinksToUpdate.add(Drinks.MOCACCINO.drink);
        drinksToUpdate.add(Drinks.BORJOMI.drink);
        int valueToAdd = 10;
        int memory1 = drinksToUpdate.get(0).getQuantity();
        int memory2 = drinksToUpdate.get(1).getQuantity();

        when(drinkDao.getAllByIds(new TreeSet<Integer>() {{
            add(2);
            add(11);
        }})).thenReturn(drinksToUpdate);
        doNothing().when(drinkDao).updateQuantityAllInList(drinkListCaptor.capture());
        Map<Integer, Integer> quantitiesToAdd = new HashMap<Integer, Integer>() {{
            put(11, valueToAdd);
            put(2, valueToAdd);
        }};

        service.refill(quantitiesToAdd);

        verify(daoFactory, times(1)).getDrinkDao(connection);
        verify(drinkDao, times(1)).getAllByIds(new TreeSet<Integer>() {{
            add(2);
            add(11);
        }});
        Assert.assertEquals(FIRST + DRINK_QUANTITY_MISMATCH,
                memory1 + valueToAdd,
                drinkListCaptor.getAllValues()
                        .get(drinkListCaptor.getAllValues().size() - 1).get(0).getQuantity());

        Assert.assertEquals(SECOND + DRINK_QUANTITY_MISMATCH,
                memory2 + valueToAdd,
                drinkListCaptor.getAllValues()
                        .get(drinkListCaptor.getAllValues().size() - 1).get(1).getQuantity());

    }


    @Test
    public void testRefillOne() throws Exception {
        drinksToUpdate = new ArrayList<>();
        drinksToUpdate.add(Drinks.MOCACCINO.drink);

        int valueToAdd = 10;
        int memory1 = drinksToUpdate.get(0).getQuantity();


        when(drinkDao.getAllByIds(new TreeSet<Integer>() {{
            add(11);
        }})).thenReturn(drinksToUpdate);
        doNothing().when(drinkDao).updateQuantityAllInList(drinkListCaptor.capture());
        Map<Integer, Integer> quantitiesToAdd = new HashMap<Integer, Integer>() {{
            put(11, valueToAdd);
        }};

        service.refill(quantitiesToAdd);

        verify(daoFactory, times(1)).getDrinkDao(connection);
        verify(drinkDao, times(1)).getAllByIds(new TreeSet<Integer>() {{
            add(11);
        }});
        Assert.assertEquals(THE_ONLY + DRINK_QUANTITY_MISMATCH,
                memory1 + valueToAdd,
                drinkListCaptor.getAllValues()
                        .get(drinkListCaptor.getAllValues().size() - 1).get(0).getQuantity());

    }

}
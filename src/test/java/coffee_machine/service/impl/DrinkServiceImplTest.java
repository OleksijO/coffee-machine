package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.DrinkDao;
import coffee_machine.model.entity.Account;
import coffee_machine.model.entity.item.Drink;
import coffee_machine.service.DrinkService;
import data.entity.Drinks;
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
 * Created by oleksij.onysymchuk@gmail on 28.11.2016.
 */
public class DrinkServiceImplTest {
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

        when(drinkDao.getAllByIds(new HashSet<Integer>() {{
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
        verify(drinkDao, times(1)).getAllByIds(new HashSet<Integer>() {{
            add(2);
            add(11);
        }});
        Assert.assertEquals(
                memory1 + valueToAdd,
                drinkListCaptor.getAllValues()
                        .get(drinkListCaptor.getAllValues().size() - 1).get(0).getQuantity());

        Assert.assertEquals(
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


        when(drinkDao.getAllByIds(new HashSet<Integer>() {{
            add(11);
        }})).thenReturn(drinksToUpdate);
        doNothing().when(drinkDao).updateQuantityAllInList(drinkListCaptor.capture());
        Map<Integer, Integer> quantitiesToAdd = new HashMap<Integer, Integer>() {{
            put(11, valueToAdd);
        }};

        service.refill(quantitiesToAdd);

        verify(daoFactory, times(1)).getDrinkDao(connection);
        verify(drinkDao, times(1)).getAllByIds(new HashSet<Integer>() {{
            add(11);
        }});
        Assert.assertEquals(
                memory1 + valueToAdd,
                drinkListCaptor.getAllValues()
                        .get(drinkListCaptor.getAllValues().size() - 1).get(0).getQuantity());

    }

}
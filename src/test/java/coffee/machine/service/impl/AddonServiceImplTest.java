package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.AddonService;
import data.entity.Addons;
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
public class AddonServiceImplTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private AddonDao addonDao;

    @Mock
    private AbstractConnection connection;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<List<Item>> addonListCaptor;

    private AddonService service;

    List<Item> addonsToUpdate;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(daoFactory.getAddonDao(connection)).thenReturn(addonDao);
        when(daoFactory.getConnection()).thenReturn(connection);

        when(addonDao.getAllFromList(any())).thenReturn(addonsToUpdate);

        service = AddonServiceImpl.getInstance();
        AddonServiceImpl.daoFactory = daoFactory;

    }


    @Test
    public void testRefillEmpty() throws Exception {
        service.refill(null);
        verify(daoFactory, never()).getAddonDao(connection);
        service.refill(new HashMap<>());
        verify(daoFactory, never()).getAddonDao(connection);

    }

    @Test
    public void testRefillTwo() throws Exception {
        addonsToUpdate = new ArrayList<>();
        addonsToUpdate.add(Addons.LEMON.addon);
        addonsToUpdate.add(Addons.CINNAMON.addon);
        int valueToAdd = 10;
        int memory1 = addonsToUpdate.get(0).getQuantity();
        int memory2 = addonsToUpdate.get(1).getQuantity();

        when(addonDao.getAllByIds(new TreeSet<Integer>() {{
            add(5);
            add(13);
        }})).thenReturn(addonsToUpdate);
        doNothing().when(addonDao).updateQuantityAllInList(addonListCaptor.capture());
        Map<Integer, Integer> quantitiesToAdd = new HashMap<Integer, Integer>() {{
            put(13, valueToAdd);
            put(5, valueToAdd);
        }};

        service.refill(quantitiesToAdd);

        verify(daoFactory, times(1)).getAddonDao(connection);
        verify(addonDao, times(1)).getAllByIds(new TreeSet<Integer>() {{
            add(5);
            add(13);
        }});
        Assert.assertEquals(
                memory1 + valueToAdd,
                addonListCaptor.getAllValues()
                        .get(addonListCaptor.getAllValues().size() - 1).get(0).getQuantity());

        Assert.assertEquals(
                memory2 + valueToAdd,
                addonListCaptor.getAllValues()
                        .get(addonListCaptor.getAllValues().size() - 1).get(1).getQuantity());

    }


    @Test
    public void testRefillOne() throws Exception {
        addonsToUpdate = new ArrayList<>();
        addonsToUpdate.add(Addons.CINNAMON.addon);

        int valueToAdd = 10;
        int memory1 = addonsToUpdate.get(0).getQuantity();


        when(addonDao.getAllByIds(new TreeSet<Integer>() {{
            add(13);
        }})).thenReturn(addonsToUpdate);
        doNothing().when(addonDao).updateQuantityAllInList(addonListCaptor.capture());
        Map<Integer, Integer> quantitiesToAdd = new HashMap<Integer, Integer>() {{
            put(13, valueToAdd);
        }};

        service.refill(quantitiesToAdd);

        verify(daoFactory, times(1)).getAddonDao(connection);
        verify(addonDao, times(1)).getAllByIds(new TreeSet<Integer>() {{
            add(13);
        }});
        Assert.assertEquals(
                memory1 + valueToAdd,
                addonListCaptor.getAllValues()
                        .get(addonListCaptor.getAllValues().size() - 1).get(0).getQuantity());

    }

}
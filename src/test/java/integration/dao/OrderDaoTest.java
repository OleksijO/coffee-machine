package integration.dao;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.OrderDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Order;
import data.test.entity.OrdersData;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Performs tests of corresponding DAO on real test database (it should be already created)
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class OrderDaoTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private List<Order> testOrders = new ArrayList<>();
    private AbstractConnection connection;
    private OrderDao orderDao;

    {
        for (OrdersData oderEnum : OrdersData.values()) {
            testOrders.add(oderEnum.order);
            Collections.sort(testOrders,(o1,o2)-> o2.getDate().compareTo(o1.getDate()));
        }
    }

    @BeforeClass
    public static void initTestDataBase() throws Exception {
        new TestDatabaseInitializer().initTestJdbcDB();
    }

    @Before
    public void init() {
        connection = daoFactory.getConnection();
        orderDao = daoFactory.getOrderDao(connection);
        connection.beginTransaction();
    }

    @After
    public void tearDown() {
        connection.commitTransaction();
        connection.close();
    }

    @Test
    public void testGetAll() {

        List<Order> orders = orderDao.getAll();
        assertEquals(testOrders, orders);

    }

    @Test
    public void testGetById() {
        Order testOrder = OrdersData.A2.order;
        Order order = orderDao.getById(testOrder.getId()).get();
        assertEquals("Order gotten by id should be identical to test one",
                testOrder, order);
        assertFalse(orderDao.getById(7).isPresent());
    }

    @Test
    public void testGetByIdIsNotPresent() {
        assertFalse(orderDao.getById(100500).isPresent());
    }



    @Test
    public void testInsertDelete() {
        Order order = OrdersData.A1.order;
        int savedId = order.getId();
        order.setId(0);
        int newOrderId = orderDao.insert(order).getId();

        assertEquals("New entity should be placed to DB and be the same to test one",
                order, orderDao.getById(newOrderId).get());
        order.setId(savedId);
        assertEquals("Total count of entities should increase by 1", testOrders.size() + 1, orderDao.getAll().size());
        orderDao.deleteById(newOrderId);
        assertFalse("Inserted entity should be deleted", orderDao.getById(newOrderId).isPresent());
        assertEquals("Total count of entities should decrease by 1", testOrders.size(), orderDao.getAll().size());

    }

    @Test
    public void testGetAllByUserId() {
        List<Order> resultList = new ArrayList<>();
        resultList.addAll(orderDao.getAllByUserId(2));
        assertEquals("Total entity count should be the same to test one",
                testOrders.size(), resultList.size());
        assertEquals("List of retrieved entities should be the same to test one ",
                testOrders, resultList);
    }

    @Test
    public void testGetAllByUserIdEmptyList() {
        List<Order> resultList = new ArrayList<>();
        resultList.addAll(orderDao.getAllByUserId(1));
        assertTrue(resultList.isEmpty());
    }
}

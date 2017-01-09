package integration.dao;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.OrderDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Order;
import data.test.entity.Orders;
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
        for (Orders oderEnum : Orders.values()) {
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
        assertEquals(testOrders.toString(), orders.toString());

    }

    @Test
    public void testGetById() {

        Order order = orderDao.getById(2);
        assertEquals("Must be identical", testOrders.get(0).toString(), order.toString());
        order = orderDao.getById(7);
        assertNull("Must be null", order);
    }

    @Test
    public void testInsertDelete() {
        Order order = testOrders.get(1);
        order.setId(0);
        int newOrderId = orderDao.insert(order).getId();
        assertTrue("new Id must be greater of total number of present records", newOrderId > 2);
        order.setId(newOrderId);

        assertEquals("1", order.toString(), orderDao.getById(newOrderId).toString());
        order.setId(1);
        assertEquals("2", testOrders.size() + 1, orderDao.getAll().size());
        orderDao.deleteById(newOrderId);
        assertNull("3", orderDao.getById(newOrderId));
        assertEquals("4", testOrders.size(), orderDao.getAll().size());

    }

    @Test
    public void testGetAllByUserId() {
        List<Order> resultList = new ArrayList<>();
        resultList.addAll(orderDao.getAllByUserId(1));
        assertEquals("1.", 0, resultList.size());
        resultList.addAll(orderDao.getAllByUserId(2));
        assertEquals("2.", 2, resultList.size());
        assertEquals("3", testOrders.toString(), resultList.toString());
    }
}

package integration.dao;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.OrderDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Order;
import data.test.entity.Orders;
import org.junit.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
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
        }
    }

    @BeforeClass
    public static void initTestDataBase() throws SQLException, ClassNotFoundException, InterruptedException, IOException {
        new TestDatabaseInitializer().initTestJdbcDB();
    }

    @Before
    public void init() {
        connection = daoFactory.getConnection();
        orderDao = daoFactory.getOrderDao(connection);
        connection.beginTransaction();
    }

    @After
    public void post() {
        connection.commitTransaction();
        connection.close();
    }

    @Test
    public void testGetAll() {

        List<Order> orders = orderDao.getAll();
        System.out.println(testOrders);
        System.out.println(orders);
        assertEquals(testOrders.toString(), orders.toString());

    }

    @Test
    public void testGetById() {

        Order order = orderDao.getById(2);
        System.out.println(testOrders.get(1));
        System.out.println(order);
        assertEquals("Must be identical", testOrders.get(1).toString(), order.toString());
        order = orderDao.getById(7);
        assertNull("Must be null", order);
    }

    @Ignore //method not implemented and throws UnsupportedOperationException
    @Test
    public void testUpdate() {

    }

    @Test
    public void testInsertDelete() {
        Order order = testOrders.get(1);
        order.setId(0);
        int newOrderId = orderDao.insert(order).getId();
        assertTrue("new Id must be greater of total number of present records", newOrderId > 2);
        order.setId(newOrderId);

        assertEquals("1", order.toString(), orderDao.getById(newOrderId).toString());
        order.setId(2);
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

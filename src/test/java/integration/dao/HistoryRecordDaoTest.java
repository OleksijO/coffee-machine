package integration.dao;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.HistoryRecordDao;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.HistoryRecord;
import data.HistoryRecords;
import org.junit.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
 */
@Ignore
public class HistoryRecordDaoTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private List<HistoryRecord> testHistoryRecords = new ArrayList<>();
    private AbstractConnection connection;
    private HistoryRecordDao historyRecordDao;

    {
        for (HistoryRecords historyRecordEnum : HistoryRecords.values()) {
            testHistoryRecords.add(historyRecordEnum.historyRecord);
        }
    }

    @BeforeClass
    public static void initTestDataBase() throws SQLException, ClassNotFoundException, InterruptedException, IOException {
        new TestDataBaseInitializer().initTestJdbcDB();
    }

    @Before
    public void init() {
        connection = daoFactory.getConnection();
        historyRecordDao = daoFactory.getHistoryRecordDao(connection);
        connection.beginTransaction();
    }

    @After
    public void post() {
        connection.commitTransaction();
        connection.close();
    }

    @Test
    public void testGetAll() {

        List<HistoryRecord> historyRecords = historyRecordDao.getAll();
        System.out.println(testHistoryRecords);
        System.out.println(historyRecords);
        assertEquals(testHistoryRecords, historyRecords);

    }

    @Test
    public void testGetById() {

        HistoryRecord historyRecord = historyRecordDao.getById(2);
        System.out.println(testHistoryRecords.get(1));
        System.out.println(historyRecord);
        assertEquals("Not null", testHistoryRecords.get(1), historyRecord);
        historyRecord = historyRecordDao.getById(7);
        assertNull("Null", historyRecord);
    }

    @Test
    public void testUpdate() {
        HistoryRecord historyRecord = testHistoryRecords.get(1);
        long amount = historyRecord.getAmount();
        historyRecord.setAmount(0);

        historyRecordDao.update(historyRecord);

        assertEquals("1", 0, historyRecordDao.getById(2).getAmount());
        historyRecord.setAmount(amount);
        historyRecordDao.update(historyRecord);
        assertEquals("2", testHistoryRecords.get(1).getAmount(), historyRecordDao.getById(2).getAmount());

    }

    @Test
    public void testInsertDelete() {
        HistoryRecord historyRecord = testHistoryRecords.get(1);
        historyRecord.setId(0);
        int newHistoryRecordId = historyRecordDao.insert(historyRecord).getId();

        historyRecord.setId(newHistoryRecordId);

        assertEquals("1", historyRecord, historyRecordDao.getById(newHistoryRecordId));
        historyRecord.setId(2);
        assertEquals("2", testHistoryRecords.size()+1, historyRecordDao.getAll().size());
        historyRecordDao.deleteById(newHistoryRecordId);
        assertNull("3", historyRecordDao.getById(newHistoryRecordId));
        assertEquals("4", testHistoryRecords.size(), historyRecordDao.getAll().size());

    }

    @Test
    public void testGetAllByUserId() {
        List<HistoryRecord> resultList=new ArrayList<>();
        resultList.addAll(historyRecordDao.getAllByUserId(1));
        assertEquals("1.", 3, resultList.size());
        resultList.addAll(historyRecordDao.getAllByUserId(2));
        assertEquals("2.", 6, resultList.size());

        assertEquals("3", testHistoryRecords, resultList);
    }
}

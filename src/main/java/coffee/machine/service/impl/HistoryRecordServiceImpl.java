package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.HistoryRecordDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.HistoryRecord;
import coffee.machine.service.HistoryRecordService;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author oleksij.onysymchuk@gmail.com 15.11.2016.
 */
public class HistoryRecordServiceImpl implements HistoryRecordService {
    private static final Logger logger = Logger.getLogger(HistoryRecordServiceImpl.class);

    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private static class InstanceHolder {
        private static final HistoryRecordService instance = new HistoryRecordServiceImpl();
    }

    public static HistoryRecordService getInstance() {
        return HistoryRecordServiceImpl.InstanceHolder.instance;
    }

    @Override
    public List<HistoryRecord> getAllByUserId(int userId) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            HistoryRecordDao historyRecordDao = daoFactory.getHistoryRecordDao(connection);
            connection.beginTransaction();
            List<HistoryRecord> userHistory =  historyRecordDao.getAllByUserId(userId);
            connection.commitTransaction();
            return userHistory;

        }
    }

}

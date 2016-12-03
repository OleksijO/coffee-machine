package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.HistoryRecordDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.HistoryRecord;
import coffee.machine.service.HistoryRecordService;

import java.util.List;

/**
 * This class is an implementation of HistoryRecordService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class HistoryRecordServiceImpl implements HistoryRecordService {
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

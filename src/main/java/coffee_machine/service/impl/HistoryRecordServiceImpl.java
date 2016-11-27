package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.HistoryRecordDao;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.HistoryRecord;
import coffee_machine.service.HistoryRecordService;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class HistoryRecordServiceImpl extends AbstractService implements HistoryRecordService {
    private static final Logger logger = Logger.getLogger(HistoryRecordServiceImpl.class);

    private static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    public HistoryRecordServiceImpl() {
        super(logger);
    }

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
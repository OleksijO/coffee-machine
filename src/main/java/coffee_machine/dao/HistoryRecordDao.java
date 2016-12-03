package coffee_machine.dao;

import coffee_machine.model.entity.HistoryRecord;

import java.util.List;

/**
 * This class represents history record entity DAO functionality
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface HistoryRecordDao extends GenericDao<HistoryRecord> {

    /**
     * @param userId user's id
     * @return list of history record corresponding specified user id
     */
    List<HistoryRecord> getAllByUserId(int userId);

}
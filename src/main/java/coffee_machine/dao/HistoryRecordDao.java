package coffee_machine.dao;

import java.util.List;

import coffee_machine.model.entity.HistoryRecord;

/**
 * This class represents history record entity DAO functionality
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface HistoryRecordDao extends GenericDao<HistoryRecord> {

    List<HistoryRecord> getAllByUserId(int userId);

}
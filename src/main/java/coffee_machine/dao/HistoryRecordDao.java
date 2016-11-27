package coffee_machine.dao;

import coffee_machine.model.entity.HistoryRecord;

import java.util.List;

public interface HistoryRecordDao extends GenericDao<HistoryRecord> {

    List<HistoryRecord> getAllByUserId(int userId);

}
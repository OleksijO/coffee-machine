package coffee_machine.service;

import coffee_machine.model.entity.HistoryRecord;

import java.util.List;

/**
 * @author oleksij.onysymchuk@gmail.com 27.11.2016.
 */
public interface HistoryRecordService {
    List<HistoryRecord> getAllByUserId(int userId);
}

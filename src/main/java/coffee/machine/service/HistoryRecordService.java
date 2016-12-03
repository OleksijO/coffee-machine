package coffee.machine.service;

import coffee.machine.model.entity.HistoryRecord;

import java.util.List;

/**
 * This class represents history record service
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface HistoryRecordService {
    List<HistoryRecord> getAllByUserId(int userId);
}

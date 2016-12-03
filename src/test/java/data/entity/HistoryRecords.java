package data.entity;

import coffee.machine.model.entity.HistoryRecord;

import java.util.Date;

import static data.entity.HistoryRecords.ConstHolder.*;

/**
 * @author oleksij.onysymchuk@gmail.com 24.11.2016.
 */
public enum HistoryRecords {
    A1(1, 1, new Date(YEAR2016 + 6 * MONTH + 12 * HOUR + 5 * MIN), "Some order description 1", 1000),
    A2(2, 1, new Date(YEAR2016 + 6 * MONTH + 12 * HOUR + 15 * MIN), "Some order description 2", 2000),
    A3(3, 1, new Date(YEAR2016 + 6 * MONTH + 12 * HOUR + 25 * MIN), "Some order description 3", 3000),
    A4(4, 2, new Date(YEAR2016 + 6 * MONTH + 12 * HOUR + 35 * MIN), "Some order description 4", 4000),
    A5(5, 2, new Date(YEAR2016 + 6 * MONTH + 12 * HOUR + 45 * MIN), "Some order description 5", 5000),
    A6(6, 2, new Date(YEAR2016 + 6 * MONTH + 12 * HOUR + 55 * MIN), "Some order description 6", 6000);


    public final HistoryRecord historyRecord;

    static class ConstHolder {
        static final long YEAR = 365L * 24 * 3600 * 1000;
        static final long MONTH = 30L * 24 * 3600 * 1000;
        static final long DAY = 24L * 3600 * 1000;
        static final long HOUR = 3600L * 1000;
        static final long MIN = 60L * 1000;
        static final long YEAR2016 = YEAR * (2016 - 1970);
    }


    HistoryRecords(int id, int userId, Date date, String desc, long amount) {
        historyRecord = new HistoryRecord();
        historyRecord.setId(id);
        historyRecord.setUserId(userId);
        historyRecord.setDate(date);
        historyRecord.setOrderDescription(desc);
        historyRecord.setAmount(amount);
    }
}



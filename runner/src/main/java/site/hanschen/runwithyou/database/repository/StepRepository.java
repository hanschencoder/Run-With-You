package site.hanschen.runwithyou.database.repository;

import site.hanschen.runwithyou.bean.StepRecord;

/**
 * @author HansChen
 */
public interface StepRepository {

    void insertStepRecord(StepRecord stepRecord);

    StepRecord getStepRecord();
}

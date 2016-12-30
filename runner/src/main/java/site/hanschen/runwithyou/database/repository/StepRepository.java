package site.hanschen.runwithyou.database.repository;

import android.support.annotation.Nullable;

import site.hanschen.runwithyou.bean.StepRecord;

/**
 * @author HansChen
 */
public interface StepRepository {

    void insertRecord(StepRecord stepRecord);

    @Nullable
    StepRecord getLatestRecord();
}

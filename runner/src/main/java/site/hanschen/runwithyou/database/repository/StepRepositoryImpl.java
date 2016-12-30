package site.hanschen.runwithyou.database.repository;


import android.support.annotation.Nullable;
import android.util.Log;

import site.hanschen.runwithyou.bean.StepRecord;
import site.hanschen.runwithyou.database.entity.StepRecordEntity;
import site.hanschen.runwithyou.database.gen.StepRecordEntityDao;

/**
 * @author HansChen
 */
public class StepRepositoryImpl implements StepRepository {

    private StepRecordEntityDao mDao;

    public StepRepositoryImpl(StepRecordEntityDao dao) {
        this.mDao = dao;
    }

    @Override
    public void insertRecord(StepRecord stepRecord) {
        StepRecordEntity entity = new StepRecordEntity();
        entity.setCountSinceReboot(stepRecord.getCountSinceReboot());
        entity.setStepCount(stepRecord.getStepCount());
        entity.setStepTime(stepRecord.getStepTime());
        mDao.insertOrReplace(entity);
    }

    @Nullable
    @Override
    public StepRecord getLatestRecord() {
        StepRecordEntity entity = mDao.queryBuilder().orderDesc(StepRecordEntityDao.Properties.Id).unique();
        if (entity == null) {
            return null;
        }
        return new StepRecord(entity.getCountSinceReboot(), entity.getStepTime(), entity.getStepCount());
    }
}

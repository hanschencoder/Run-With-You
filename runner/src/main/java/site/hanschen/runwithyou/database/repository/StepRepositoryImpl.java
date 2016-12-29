package site.hanschen.runwithyou.database.repository;

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
    public void insertStepRecord(StepRecord stepRecord) {
        StepRecordEntity entity = new StepRecordEntity();
        entity.setCountSinceReboot(stepRecord.getCountSinceReboot());
        entity.setStepCount(stepRecord.getStepCount());
        entity.setStepTime(stepRecord.getStepTime());
        mDao.insert(entity);
    }

    @Override
    public StepRecord getStepRecord() {
        return null;
    }
}

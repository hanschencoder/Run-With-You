package site.hanschen.runwithyou.database.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author HansChen
 */
@Entity
public class StepRecordEntity {

    @Id(autoincrement = true)
    private long id;
    private long countSinceReboot;
    private long stepTime;
    private long stepCount;

    @Generated(hash = 2058002233)
    public StepRecordEntity(long id, long countSinceReboot, long stepTime, long stepCount) {
        this.id = id;
        this.countSinceReboot = countSinceReboot;
        this.stepTime = stepTime;
        this.stepCount = stepCount;
    }

    @Generated(hash = 390806776)
    public StepRecordEntity() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCountSinceReboot() {
        return this.countSinceReboot;
    }

    public void setCountSinceReboot(long countSinceReboot) {
        this.countSinceReboot = countSinceReboot;
    }

    public long getStepTime() {
        return this.stepTime;
    }

    public void setStepTime(long stepTime) {
        this.stepTime = stepTime;
    }

    public long getStepCount() {
        return this.stepCount;
    }

    public void setStepCount(long stepCount) {
        this.stepCount = stepCount;
    }
}

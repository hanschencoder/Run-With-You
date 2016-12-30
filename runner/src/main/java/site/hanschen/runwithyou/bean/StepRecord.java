package site.hanschen.runwithyou.bean;


/**
 * @author HansChen
 */
public class StepRecord {

    private long mCountSinceReboot;
    private long mStepTime;
    private long mStepCount;

    public StepRecord(long countSinceReboot, long stepTime, long stepCount) {
        this.mCountSinceReboot = countSinceReboot;
        this.mStepTime = stepTime;
        this.mStepCount = stepCount;
    }

    public long getCountSinceReboot() {
        return mCountSinceReboot;
    }

    public void setCountSinceReboot(long countSinceReboot) {
        this.mCountSinceReboot = countSinceReboot;
    }

    public long getStepTime() {
        return this.mStepTime;
    }

    public void setStepTime(long stepTime) {
        this.mStepTime = stepTime;
    }

    public long getStepCount() {
        return this.mStepCount;
    }

    public void setStepCount(long stepCount) {
        this.mStepCount = stepCount;
    }

    @Override
    public String toString() {
        return "StepRecord{" +
                "mCountSinceReboot=" + mCountSinceReboot +
                ", mStepTime=" + mStepTime +
                ", mStepCount=" + mStepCount +
                '}';
    }
}

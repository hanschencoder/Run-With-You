package site.hanschen.runwithyou.service;
import site.hanschen.runwithyou.service.RunnerCallback;

/**
 * @author HansChen
 */
interface RunnerManager {

    long getStepCount();

    void registerCallback(RunnerCallback callback);

    void unregisterCallback(RunnerCallback callback);
}

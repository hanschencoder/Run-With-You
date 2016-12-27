package site.hanschen.runwithyou.service;
import site.hanschen.runwithyou.service.RunnerCallback;

/**
 * @author HansChen
 */
interface RunnerManager {

    int getStepCount();

    void registerCallback(RunnerCallback callback);

    void unregisterCallback(RunnerCallback callback);
}

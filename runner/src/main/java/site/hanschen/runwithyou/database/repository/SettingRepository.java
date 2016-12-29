package site.hanschen.runwithyou.database.repository;

/**
 * @author HansChen
 */
public interface SettingRepository {

    int getTargetStep();

    boolean isForegroundService();
}

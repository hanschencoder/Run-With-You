package site.hanschen.runwithyou.main.today;

import dagger.Module;
import dagger.Provides;

/**
 * @author HansChen
 */
@Module
public class TodayPresenterModule {

    private final TodayContract.View mView;

    public TodayPresenterModule(TodayContract.View view) {
        mView = view;
    }

    @Provides
    TodayContract.View provideTodayContractView() {
        return mView;
    }
}

package site.hanschen.runwithyou.ui.home.together;

import dagger.Module;
import dagger.Provides;

/**
 * @author HansChen
 */
@Module
class TogetherModule {

    private final TogetherContract.View mView;

    TogetherModule(TogetherContract.View view) {
        mView = view;
    }

    @Provides
    TogetherContract.View provideTogetherContractView() {
        return mView;
    }
}

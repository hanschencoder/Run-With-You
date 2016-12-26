package site.hanschen.runwithyou.main.today;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import site.hanschen.common.utils.PreconditionUtils;
import site.hanschen.runwithyou.service.RunnerManager;

/**
 * @author HansChen
 */
class TodayPresenter implements TodayContract.Presenter {

    private TodayContract.View mView;
    private RunnerManager      mRunnerManager;

    @Inject
    TodayPresenter(@NonNull TodayContract.View view, @NonNull RunnerManager runnerManager) {
        this.mView = PreconditionUtils.checkNotNull(view, "TodayContract.View cannot be null!");
        this.mRunnerManager = PreconditionUtils.checkNotNull(runnerManager, "RunnerManager cannot be null!");
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void loadStepCount() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(mRunnerManager.getStepCount());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer count) {
                mView.onStepLoadSuccess(count);
            }

            @Override
            public void onError(Throwable e) {
                mView.onStepLoadFailed();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}

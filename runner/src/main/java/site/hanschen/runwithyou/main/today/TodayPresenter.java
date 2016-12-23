package site.hanschen.runwithyou.main.today;

import android.os.RemoteException;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    subscriber.onNext(mRunnerManager.getStepCount());
                    subscriber.onCompleted();
                } catch (RemoteException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Integer>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.onStepUpdateFailure();
            }

            @Override
            public void onNext(Integer count) {
                mView.onStepUpdateSuccess(count);
            }
        });
    }
}

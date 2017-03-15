package site.hanschen.runwithyou.main.today;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import site.hanschen.common.utils.PreconditionUtils;
import site.hanschen.runwithyou.database.repository.SettingRepository;
import site.hanschen.runwithyou.service.RunnerManager;

/**
 * @author HansChen
 */
class TodayPresenter implements TodayContract.Presenter {

    private TodayContract.View mView;
    private RunnerManager      mRunnerManager;
    private SettingRepository  mSettingRepository;

    @Inject
    TodayPresenter(@NonNull TodayContract.View view,
                   @NonNull RunnerManager runnerManager,
                   @NonNull SettingRepository settingRepository) {
        this.mView = PreconditionUtils.checkNotNull(view, "TodayContract.View cannot be null!");
        this.mRunnerManager = PreconditionUtils.checkNotNull(runnerManager, "RunnerManager cannot be null!");
        this.mSettingRepository = PreconditionUtils.checkNotNull(settingRepository,
                                                                 "SettingRepository cannot be null!");
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void detach() {

    }

    @Override
    public void loadStepCount() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d("Hans", "Thread:" + Thread.currentThread() + "getTargetStep");
                emitter.onNext(mSettingRepository.getTargetStep());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer max) throws Exception {
                Log.d("Hans", "Thread:" + Thread.currentThread() + "showMaxCount");
                mView.showMaxCount(max);
                return 1;
            }
        }).observeOn(Schedulers.io()).map(new Function<Integer, Long>() {
            @Override
            public Long apply(Integer aVoid) throws Exception {
                Log.d("Hans", "Thread:" + Thread.currentThread() + "getStepCount");
                long count = mRunnerManager.getStepCount();
                if (count >= 0) {
                    return count;
                }
                throw new IllegalStateException("count < 0");
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long count) {
                Log.d("Hans", "Thread:" + Thread.currentThread() + "showCurrentStepCount");
                mView.showCurrentStepCount(count);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.showStepLoadFailInfo();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}

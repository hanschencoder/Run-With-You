package site.hanschen.runwithyou.main.today;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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
        this.mSettingRepository = PreconditionUtils.checkNotNull(settingRepository, "SettingRepository cannot be null!");
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
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(mSettingRepository.getTargetStep());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer max) throws Exception {
                mView.showMaxCount(max);
            }
        }).observeOn(Schedulers.io()).flatMap(new Function<Integer, ObservableSource<Long>>() {
            @Override
            public ObservableSource<Long> apply(Integer max) throws Exception {

                return Observable.create(new ObservableOnSubscribe<Long>() {
                    @Override
                    public void subscribe(ObservableEmitter<Long> e) throws Exception {
                        long count = mRunnerManager.getStepCount();
                        if (count < 0) {
                            throw new IllegalStateException("count < 0");
                        }
                        e.onNext(count);
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long count) {
                mView.showCurrentStepCount(count);
            }

            @Override
            public void onError(Throwable e) {
                mView.showStepLoadFailInfo();
            }

            @Override
            public void onComplete() {

            }
        });
    }
}

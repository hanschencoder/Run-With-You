package site.hanschen.api.user;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author HansChen
 */
public class UserCenterApiWrapper {

    private UserCenterApi mApi;

    public UserCenterApiWrapper(UserCenterApi api) {
        this.mApi = api;
    }

    public Observable<LoginReply> login(final String username, final String password) {
        return new ObservableApi<LoginReply>() {
            @Override
            protected LoginReply getResult() throws Exception {
                return mApi.login(username, password);
            }
        }.create();
    }

    public Observable<VerificationReply> requestVerificationCode(final String email) {
        return new ObservableApi<VerificationReply>() {
            @Override
            protected VerificationReply getResult() throws Exception {
                return mApi.requestVerificationCode(email);
            }
        }.create();
    }

    public Observable<RegisterReply> register(final String email, final String verificationCode, final String password) {
        return new ObservableApi<RegisterReply>() {
            @Override
            protected RegisterReply getResult() throws Exception {
                return mApi.register(email, verificationCode, password);
            }
        }.create();
    }

    public Observable<AuthorizationReply> requestAuthorization(final String username, final String password) {
        return new ObservableApi<AuthorizationReply>() {
            @Override
            protected AuthorizationReply getResult() throws Exception {
                return mApi.requestAuthorization(username, password);
            }
        }.create();
    }

    public Observable<NewPasswordReply> changePassword(final String token, final String newPassword, final String authorization) {
        return new ObservableApi<NewPasswordReply>() {
            @Override
            protected NewPasswordReply getResult() throws Exception {
                return mApi.changePassword(token, newPassword, authorization);
            }
        }.create();
    }

    public Observable<UserInfo> requestUserInfo(final String token) {
        return new ObservableApi<UserInfo>() {
            @Override
            protected UserInfo getResult() throws Exception {
                return mApi.requestUserInfo(token);
            }
        }.create();
    }

    public Observable<ResultReply> updateUserInfo(final String token, final UserInfo userInfo) {
        return new ObservableApi<ResultReply>() {
            @Override
            protected ResultReply getResult() throws Exception {
                return mApi.updateUserInfo(token, userInfo);
            }
        }.create();
    }


    private static abstract class ObservableApi<T> {

        public Observable<T> create() {
            return Observable.create(new ObservableOnSubscribe<T>() {
                @Override
                public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                    emitter.onNext(getResult());
                    emitter.onComplete();
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }

        protected abstract T getResult() throws Exception;
    }
}

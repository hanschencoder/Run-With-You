package site.hanschen.runwithyou.application;

import android.text.TextUtils;

import site.hanschen.common.utils.PreconditionUtils;

/**
 * @author HansChen
 */
public class AuthManager {

    private String email;
    private String token;

    private static AuthManager mInstance;

    public static AuthManager getInstance() {
        if (mInstance == null) {
            synchronized (AuthManager.class) {
                if (mInstance == null) {
                    mInstance = new AuthManager();
                }
            }
        }
        return mInstance;
    }

    private AuthManager() {
    }

    public void logout() {
        email = null;
        token = null;
    }

    public void setAuth(String email, String token) {
        this.email = PreconditionUtils.checkNotNull(email);
        this.token = PreconditionUtils.checkNotNull(token);
    }

    public boolean isLogined() {
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(token);
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}

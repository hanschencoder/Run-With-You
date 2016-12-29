package site.hanschen.runwithyou.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author HansChen
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface AppContext {
}

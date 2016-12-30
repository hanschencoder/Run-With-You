package site.hanschen.runwithyou.eventbus;

/**
 * @author HansChen
 */
abstract class IterableDispatcher<T> implements CallbackDispatcher {

    private Iterable<T> mCallbacks;

    IterableDispatcher(Iterable<T> callbacks) {
        this.mCallbacks = callbacks;
    }

    abstract void execute(T callback);

    @Override
    public void dispatch() {
        for (T callback : mCallbacks) {
            execute(callback);
        }
    }
}

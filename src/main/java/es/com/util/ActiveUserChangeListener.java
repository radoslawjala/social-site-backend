package es.com.util;

public interface ActiveUserChangeListener {

    /**
     * call when Observable's internal state is changed.
     */
    void notifyActiveUserChange();
}

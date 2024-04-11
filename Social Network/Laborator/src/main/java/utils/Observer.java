package utils;

import utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}

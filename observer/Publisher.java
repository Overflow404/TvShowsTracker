package com.example.unamed.mvc.observer;

/**
 * An interface representing who publish updates.
 */
public interface Publisher {
    void subscribe(Subscriber o);
    void notifySubscriber();
}

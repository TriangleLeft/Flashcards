package com.triangleleft.flashcards;

public interface Subscription {
    void unsubscribe();

    Subscription EMPTY = new Subscription() {
        @Override
        public void unsubscribe() {

        }
    };
}

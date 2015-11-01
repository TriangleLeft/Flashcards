package com.triangleleft.flashcards;

public enum Injector {
    INSTANCE;

    private ApplicationComponent component;

    public void setup(FlashcardsApplication app) {
        // Assert
        component =
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(app))
                        .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}

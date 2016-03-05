package com.triangleleft.flashcards;

import com.triangleleft.flashcards.dagger.MockApplicationComponent;
import com.triangleleft.flashcards.service.provider.IListener;
import com.triangleleft.flashcards.service.login.ILoginModule;
import com.triangleleft.flashcards.service.error.CommonError;
import com.triangleleft.flashcards.android.LoginActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class LoginActivityTestCase extends Assert {

    @Inject
    ILoginModule loginModule;

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class, true, false);

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        FlashcardsApplication app
                = (FlashcardsApplication) instrumentation.getTargetContext().getApplicationContext();
        MockApplicationComponent component = (MockApplicationComponent) app.getComponent();
        component.inject(this);

    }

    @Test
    public void testStuff() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final IListener[] listener = new IListener[1];
        Mockito.doAnswer(answer -> {
            listener[0] = (IListener) answer.getArguments()[0];
            return null;
        }).when(loginModule).registerListener(Mockito.any(IListener.class));
        Mockito.doAnswer(answer -> {
            listener[0].onError(new CommonError("Fuuu"));
            return null;
        }).when(loginModule).login(Mockito.anyString(), Mockito.anyString());

        LoginActivity activity = activityRule.launchActivity(new Intent(Intent.ACTION_MAIN));
        assertNotNull(activity);

        onView(withId(R.id.login_button)).perform(click());

    }


}

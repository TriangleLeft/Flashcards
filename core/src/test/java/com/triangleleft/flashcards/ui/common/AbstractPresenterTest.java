package com.triangleleft.flashcards.ui.common;

import static org.mockito.Mockito.*;

import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.ui.common.view.IView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(JUnit4.class)
public class AbstractPresenterTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    ITestView view;
    @Captor
    ArgumentCaptor<Runnable> runnableCaptor;
    private TestPresenter presenter;

    /**
     * Setup stuff for test.
     */
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        presenter = new TestPresenter();
        presenter.onCreate();
        presenter.onBind(view);
        presenter.onRebind(view);
        presenter.onUnbind();
    }

    /**
     * Test that if we call some view method while we have none bound, it would be called automatically as soon as
     * view is bound again.
     */
    @Test
    public void presenterRememberCallsWhilePaused() {
        presenter.testMethod();
        verify(view, never()).testMethod();
        //verify(view, never()).runOnUiThread(any(Runnable.class));
        presenter.onBind(view);
        presenter.onRebind(view);
        verify(view).testMethod();
    }

    /**
     * Test that calls made to view while none is bound are applied in same order as they were called.
     */
    @Test
    public void presenterRememberCallsWhilePausedInOrder() {
        presenter.testMethod();
        presenter.anotherTestMethod();
        presenter.onBind(view);
        presenter.onRebind(view);
        InOrder inOrder = inOrder(view);
        inOrder.verify(view).testMethod();
        inOrder.verify(view).anotherTestMethod();
    }

    @Test
    public void disallowTouchViewAfterDestroy() {
        presenter.onCreate();
        presenter.onBind(view);
        presenter.onUnbind();
        presenter.onDestroy();

        expectedException.expect(Exception.class);
        presenter.testMethod();
    }

    public interface ITestView extends IView {
        void testMethod();

        void anotherTestMethod();
    }

    private class TestPresenter extends AbstractPresenter<ITestView> {

        public TestPresenter() {
            super(ITestView.class);
        }

        void testMethod() {
            getView().testMethod();
        }

        void anotherTestMethod() {
            getView().anotherTestMethod();
        }
    }

}

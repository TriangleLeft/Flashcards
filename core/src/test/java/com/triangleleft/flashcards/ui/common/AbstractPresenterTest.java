package com.triangleleft.flashcards.ui.common;

import static org.mockito.Mockito.*;

import com.triangleleft.flashcards.ui.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.ui.common.view.IView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractPresenterTest {

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

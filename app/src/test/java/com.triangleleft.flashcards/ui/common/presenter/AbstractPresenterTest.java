package com.triangleleft.flashcards.ui.common.presenter;

import com.triangleleft.flashcards.ui.common.view.IView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class AbstractPresenterTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Spy
    public AbstractPresenter<IView, IPresenterState> presenter;

    @Mock
    public IView view;

    @Test
    public void bindUnbind() {
        assertNull(presenter.getView());
        presenter.onBind(view);
        assertNotNull(presenter.getView());
        presenter.onUnbind(view);
        assertNull(presenter.getView());
    }

    @Test
    public void failToBindTwice() {
        presenter.onBind(view);
        exception.expect(IllegalStateException.class);
        presenter.onBind(view);
    }

    @Test
    public void failToUnbindTwice() {
        presenter.onBind(view);
        presenter.onUnbind(view);
        exception.expect(IllegalStateException.class);
        presenter.onUnbind(view);
    }

    @Test
    public void failToUnbindWithoutBind() {
        exception.expect(IllegalStateException.class);
        presenter.onUnbind(view);
    }

    @Test
    public void failToUnbindForeignView() {
        presenter.onBind(view);
        IView foreignView = Mockito.mock(IView.class);
        exception.expect(IllegalArgumentException.class);
        presenter.onUnbind(foreignView);
    }

    @Test
    public void failToGetViewWhileNotBound() {
        exception.expect(IllegalStateException.class);
        IView view = presenter.getView();
    }

}

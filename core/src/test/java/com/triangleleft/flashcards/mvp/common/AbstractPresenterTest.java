package com.triangleleft.flashcards.mvp.common;

import com.triangleleft.flashcards.mvp.common.presenter.AbstractPresenter;
import com.triangleleft.flashcards.mvp.common.view.IView;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractPresenterTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Spy
    public AbstractPresenter<IView> presenter;

    @Mock
    public IView view;

}

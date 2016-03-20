package com.triangleleft.flashcards.android.vocabular;

import com.google.common.base.Preconditions;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.android.BaseFragment;
import com.triangleleft.flashcards.android.main.MainActivity;
import com.triangleleft.flashcards.mvp.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.vocabular.di.VocabularWordComponent;
import com.triangleleft.flashcards.mvp.vocabular.presenter.IVocabularWordPresenter;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularWordView;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VocabularWordFragment
        extends BaseFragment<VocabularWordComponent, IVocabularWordView, IVocabularWordPresenter>
        implements IVocabularWordView {

    private static final Logger logger = LoggerFactory.getLogger(VocabularWordFragment.class);

    @Bind(R.id.fragment_vocabular_word_title)
    TextView titleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_vocabular_word, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @NonNull
    @Override
    protected VocabularWordComponent buildComponent() {
        logger.debug("buildComponent() called");
        return getApplicationComponent().getApplication().buildVocabularWordComponent(getMainActivityComponent());
    }

    @NonNull
    @Override
    protected IVocabularWordView getMvpView() {
        logger.debug("getMvpView() called");
        return this;
    }

    @NonNull
    private MainPageComponent getMainActivityComponent() {
        logger.debug("getMainActivityComponent() called");
        Preconditions.checkState(getActivity() != null, "Shouldn't call this while activity is not bound");
        MainActivity activity = (MainActivity) getActivity();
        return activity.getComponent();
    }

    @Override
    public void showWord(@NonNull IVocabularWord selectedWord) {
        titleView.setText(selectedWord.getWord());
    }
}

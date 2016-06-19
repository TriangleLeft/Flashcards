package com.triangleleft.flashcards.vocabular;

import com.triangleleft.flashcards.BaseFragment;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.main.MainActivity;
import com.triangleleft.flashcards.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularWordView;
import com.triangleleft.flashcards.mvp.vocabular.VocabularWordPresenter;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;
import com.triangleleft.flashcards.vocabular.di.DaggerVocabularWordComponent;
import com.triangleleft.flashcards.vocabular.di.VocabularWordComponent;

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
        extends BaseFragment<VocabularWordComponent, IVocabularWordView, VocabularWordPresenter>
        implements IVocabularWordView {

    private static final Logger logger = LoggerFactory.getLogger(VocabularWordFragment.class);
    public static final String TAG = VocabularWordFragment.class.getSimpleName();

    @Bind(R.id.fragment_vocabular_word_title)
    TextView titleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        return DaggerVocabularWordComponent.builder().mainPageComponent(getMainPageComponent()).build();
    }

    private MainPageComponent getMainPageComponent() {
        return ((MainActivity) getActivity()).getComponent();
    }

    @NonNull
    @Override
    protected IVocabularWordView getMvpView() {
        logger.debug("getMvpView() called");
        return this;
    }

    @Override
    public void showWord(VocabularWord word) {
        titleView.setText(word.getWord());
    }
}

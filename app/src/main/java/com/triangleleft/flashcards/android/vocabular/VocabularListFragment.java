package com.triangleleft.flashcards.android.vocabular;

import com.google.common.base.Preconditions;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.android.BaseFragment;
import com.triangleleft.flashcards.android.main.MainActivity;
import com.triangleleft.flashcards.dagger.component.MainActivityComponent;
import com.triangleleft.flashcards.dagger.component.VocabularListComponent;
import com.triangleleft.flashcards.mvp.vocabular.presenter.IVocabularListPresenter;
import com.triangleleft.flashcards.mvp.vocabular.view.IVocabularListView;
import com.triangleleft.flashcards.service.vocabular.IVocabularWord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VocabularListFragment
        extends BaseFragment<VocabularListComponent, IVocabularListView, IVocabularListPresenter>
        implements IVocabularListView {

    private static final Logger logger = LoggerFactory.getLogger(VocabularListFragment.class);

    public static final String TAG = VocabularListFragment.class.getSimpleName();
    private static final int PROGRESS = 0;
    private static final int LIST = 1;
    private static final int ERROR = 2;

    @Inject
    IVocabularListPresenter presenter;
    @Bind(R.id.vocab_list)
    RecyclerView vocabList;
    @Bind(R.id.view_flipper)
    ViewFlipper viewFlipper;
    private VocabularAdapter vocabularAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logger.debug("onCreateView() called with: inflater = [{}], container = [{}], savedInstanceState = [{}]",
                inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_vocabular_list, container, false);
        ButterKnife.bind(this, view);

        vocabularAdapter = new VocabularAdapter();
        vocabularAdapter.setItemClickListener((viewHolder, position) -> {
            IVocabularWord word = vocabularAdapter.getItem(position);
            presenter.onWordSelected(word);
        });
        vocabList.setAdapter(vocabularAdapter);
        vocabList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showWords(@NonNull List<IVocabularWord> words) {
        logger.debug("showWords() called with: words = [{}]", words);
        viewFlipper.setDisplayedChild(LIST);
        vocabularAdapter.setData(words);
    }

    @Override
    public void showProgress() {
        logger.debug("showProgress() called");
        viewFlipper.setDisplayedChild(PROGRESS);
    }

    @Override
    public void showError() {
        logger.debug("showError() called");
        viewFlipper.setDisplayedChild(ERROR);
    }

    @OnClick(R.id.button_retry)
    public void onRetryClick() {
        logger.debug("onRetryClick() called");
        presenter.onRetryClick();
    }

    @Override
    protected void inject() {
        logger.debug("inject() called");
        getComponent().inject(this);
    }

    @NonNull
    @Override
    protected VocabularListComponent buildComponent() {
        logger.debug("buildComponent() called");
        return getApplicationComponent().getApplication().buildVocabularListComponent(getMainActivityComponent());
    }

    @NonNull
    @Override
    protected IVocabularListView getMvpView() {
        logger.debug("getMvpView() called");
        return this;
    }

    @NonNull
    private MainActivityComponent getMainActivityComponent() {
        logger.debug("getMainActivityComponent() called");
        Preconditions.checkState(getActivity() != null, "Shouldn't call this while activity is not bound");
        MainActivity activity = (MainActivity) getActivity();
        return activity.getComponent();
    }
}

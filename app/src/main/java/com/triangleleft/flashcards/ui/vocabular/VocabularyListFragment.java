package com.triangleleft.flashcards.ui.vocabular;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.BaseFragment;
import com.triangleleft.flashcards.ui.common.OnItemClickListener;
import com.triangleleft.flashcards.ui.main.MainActivity;
import com.triangleleft.flashcards.ui.main.di.MainPageComponent;
import com.triangleleft.flashcards.ui.vocabular.di.DaggerVocabularyListComponent;
import com.triangleleft.flashcards.ui.vocabular.di.VocabularyListComponent;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@FunctionsAreNonnullByDefault
public class VocabularyListFragment
        extends BaseFragment<VocabularyListComponent, IVocabularyListView, VocabularyListPresenter>
        implements IVocabularyListView {

    private static final Logger logger = LoggerFactory.getLogger(VocabularyListFragment.class);

    public static final String TAG = VocabularyListFragment.class.getSimpleName();
    private static final int PROGRESS = 0;
    private static final int CONTENT = 1;
    private static final int ERROR = 2;
    private static final int EMPTY = 3;

    @Bind(R.id.vocab_list)
    RecyclerView vocabList;
    @Bind(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefresh;
    private VocabularyAdapter vocabularyAdapter;
    private OnItemClickListener<VocabularyViewHolder> itemClickListener;
    private boolean twoPane;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        logger.debug("onCreateView() called with: inflater = [{}], container = [{}], savedInstanceState = [{}]",
                inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_vocabular_list, container, false);
        ButterKnife.bind(this, view);

        twoPane = getResources().getBoolean(R.bool.two_panes);
        vocabularyAdapter = new VocabularyAdapter(twoPane);
        itemClickListener = (viewHolder, position) -> {
            getPresenter().onWordSelected(position);
            vocabularyAdapter.setSelectedPosition(position);
        };
        vocabularyAdapter.setItemClickListener(itemClickListener);
        vocabList.setAdapter(vocabularyAdapter);
        vocabList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        swipeRefresh.setOnRefreshListener(() -> getPresenter().onRefreshList());
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.green);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showWords(@NonNull List<VocabularyWord> words, int selectedPosition) {
        logger.debug("showWords()");
        viewFlipper.setDisplayedChild(CONTENT);
        vocabularyAdapter.setData(words);
        vocabularyAdapter.setSelectedPosition(selectedPosition);
        // If we are in two pane view, it's first time we show data, and we want to select some valid position
        // simulate click, in order for second panel to display something
        if (twoPane && !swipeRefresh.isRefreshing() && selectedPosition != VocabularyListPresenter.NO_POSITION) {
            itemClickListener.onItemClick(null, selectedPosition);
        }
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showProgress() {
        logger.debug("showProgress() called");
        viewFlipper.setDisplayedChild(PROGRESS);
    }

    @Override
    public void showRefreshError() {
        logger.debug("showError() called");
        viewFlipper.setDisplayedChild(ERROR);
    }

    @Override
    public void showLoadError() {
        Snackbar.make(getView(), "No connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", view -> {
            getPresenter().onLoadList();
        });
    }

    @Override
    public void showEmpty() {
        viewFlipper.setDisplayedChild(EMPTY);
    }


    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @NonNull
    @Override
    protected VocabularyListComponent buildComponent() {
        return DaggerVocabularyListComponent.builder().mainPageComponent(getMainPageComponent()).build();
    }

    private MainPageComponent getMainPageComponent() {
        return ((MainActivity) getActivity()).getComponent();
    }

    @NonNull
    @Override
    protected IVocabularyListView getMvpView() {
        return this;
    }
}

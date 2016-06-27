package com.triangleleft.flashcards.vocabular;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.common.BaseFragment;
import com.triangleleft.flashcards.common.OnItemClickListener;
import com.triangleleft.flashcards.main.MainActivity;
import com.triangleleft.flashcards.main.di.MainPageComponent;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularListView;
import com.triangleleft.flashcards.mvp.vocabular.VocabularListPresenter;
import com.triangleleft.flashcards.service.vocabular.VocabularWord;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.vocabular.di.DaggerVocabularListComponent;
import com.triangleleft.flashcards.vocabular.di.VocabularListComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@FunctionsAreNonnullByDefault
public class VocabularListFragment
        extends BaseFragment<VocabularListComponent, IVocabularListView, VocabularListPresenter>
        implements IVocabularListView {

    private static final Logger logger = LoggerFactory.getLogger(VocabularListFragment.class);

    public static final String TAG = VocabularListFragment.class.getSimpleName();
    private static final int PROGRESS = 0;
    private static final int LIST = 1;
    private static final int ERROR = 2;

    @Bind(R.id.vocab_list)
    RecyclerView vocabList;
    @Bind(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefresh;
    private VocabularAdapter vocabularAdapter;
    private OnItemClickListener<VocabularViewHolder> itemClickListener;
    private boolean twoPane;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        logger.debug("onCreateView() called with: inflater = [{}], container = [{}], savedInstanceState = [{}]",
                inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_vocabular_list, container, false);
        ButterKnife.bind(this, view);

        twoPane = getResources().getBoolean(R.bool.two_panes);
        vocabularAdapter = new VocabularAdapter(twoPane);
        itemClickListener = (viewHolder, position) -> {
            getPresenter().onWordSelected(position);
            vocabularAdapter.setSelectedPosition(position);
        };
        vocabularAdapter.setItemClickListener(itemClickListener);
        vocabList.setAdapter(vocabularAdapter);
        vocabList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        swipeRefresh.setOnRefreshListener(() -> getPresenter().onRefreshList());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showWords(@NonNull List<VocabularWord> words, int selectedPosition) {
        viewFlipper.setDisplayedChild(LIST);
        vocabularAdapter.setData(words);
        vocabularAdapter.setSelectedPosition(selectedPosition);
        // If we are in two pane view, it's first time we show data, and we want to select some valid position
        // simulate click, in order for second panel to display something
        if (twoPane && !swipeRefresh.isRefreshing() && selectedPosition != VocabularListPresenter.NO_POSITION) {
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
    public void showError() {
        logger.debug("showError() called");
        viewFlipper.setDisplayedChild(ERROR);
    }

    @Override
    public void showErrorNoContent() {
        Snackbar.make(getView(), "No connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", view -> {
            getPresenter().onLoadList();
        });
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
        return DaggerVocabularListComponent.builder().mainPageComponent(getMainPageComponent()).build();
    }

    private MainPageComponent getMainPageComponent() {
        return ((MainActivity) getActivity()).getComponent();
    }

    @NonNull
    @Override
    protected IVocabularListView getMvpView() {
        logger.debug("getMvpView() called");
        return this;
    }
}

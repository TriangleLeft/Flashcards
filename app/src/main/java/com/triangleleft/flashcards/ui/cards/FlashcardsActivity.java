package com.triangleleft.flashcards.ui.cards;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ViewFlipper;

import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.di.cards.CardsComponent;
import com.triangleleft.flashcards.di.cards.DaggerCardsComponent;
import com.triangleleft.flashcards.service.cards.FlashcardWord;
import com.triangleleft.flashcards.service.cards.ReviewDirection;
import com.triangleleft.flashcards.ui.FlashcardsApplication;
import com.triangleleft.flashcards.ui.ViewState;
import com.triangleleft.flashcards.ui.common.BaseActivity;
import com.triangleleft.flashcards.util.FunctionsAreNonnullByDefault;
import com.triangleleft.flashcards.util.SimpleAnimatorListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

@FunctionsAreNonnullByDefault
public class FlashcardsActivity extends BaseActivity<CardsComponent, FlashcardsView, ViewState, FlashcardsPresenter>
        implements
        FlashcardsView {

    public static final String REVEAL_X = "revealX";
    public static final String REVEAL_Y = "revealY";
    private static final int PROGRESS = 0;
    private static final int CARDS = 1;
    private static final int RESULT_NO_ERRORS = 2;
    private static final int RESULT_ERRORS = 3;
    private static final int ERROR = 4;
    @Bind(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @Bind(R.id.swipe_deck)
    SwipeDeck swipeDeck;
    @Bind(R.id.flashcard_result_errors_list)
    RecyclerView resultErrorList;
    @Bind(R.id.flashcard_button_container)
    View buttonContainer;
    @Bind(R.id.root_layout)
    View rootLayout;
    @BindInt(android.R.integer.config_mediumAnimTime)
    int animationTime;

    private SwipeDeckAdapter adapter;
    private SparseBooleanArray revealedCards = new SparseBooleanArray();
    private int cx = -1;
    private int cy = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);
        ButterKnife.bind(this);

//        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            rootLayout.setVisibility(View.INVISIBLE);
//
//            cx = getIntent().getIntExtra(REVEAL_X, 0);
//            cy = getIntent().getIntExtra(REVEAL_Y, 0);
//
//            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
//
//            if (viewTreeObserver.isAlive()) {
//                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        circularRevealActivity(rootLayout, cx, cy, true);
//
//                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    }
//                });
//            }
//
//        }

        adapter = new SwipeDeckAdapter(new FlashcardView.IFlashcardListener() {
            @Override
            public void onRightClick() {
                swipeDeck.swipeTopCardRight(500);
            }

            @Override
            public void onWrongClick() {
                swipeDeck.swipeTopCardLeft(500);
            }

            @Override
            public void onRevealed() {
                revealedCards.put((int) swipeDeck.getTopCardItemId(), true);
            }
        });
        swipeDeck.setAdapter(adapter);
        swipeDeck.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long positionInAdapter) {
                FlashcardWord word = adapter.getItem((int) positionInAdapter);
                getPresenter().onWordWrong(word);
                if (positionInAdapter == adapter.getCount() - 1) {
                    getPresenter().onCardsDepleted();
                }
            }

            @Override
            public void cardSwipedRight(long positionInAdapter) {
                FlashcardWord word = adapter.getItem((int) positionInAdapter);
                getPresenter().onWordRight(word);
                if (positionInAdapter == adapter.getCount() - 1) {
                    getPresenter().onCardsDepleted();
                }
            }

            @Override
            public boolean isDragEnabled(long itemId) {
                return revealedCards.get((int) itemId);
            }

//            @Override
//            public void cardsDepleted() {
//                getPresenter().onCardsDepleted();
//            }
//
//
        });

        resultErrorList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Animator circularRevealActivity(View rootView, int cx, int cy, boolean show) {
        float maxRadius = Math.max(rootView.getWidth(), rootView.getHeight());
        float startRadius = show ? 0 : maxRadius;
        float endRadius = show ? maxRadius : 0;

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, startRadius, endRadius);
        circularReveal.setDuration(animationTime);

        // make the view visible and start the animation
        rootView.setVisibility(View.VISIBLE);
        circularReveal.start();
        return circularReveal;
    }

    @Override
    public void onBackPressed() {
        if (cx > 0 && cy > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularRevealActivity(rootLayout, cx, cy, false).addListener(new SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                    overridePendingTransition(-1, -1);
                }
            });
        } else {
            finish();
        }
    }

    @Override
    protected void inject() {
        getComponent().inject(this);
    }

    @NonNull
    @Override
    protected CardsComponent buildComponent() {
        return DaggerCardsComponent.builder()
                .applicationComponent(((FlashcardsApplication) getApplication()).getComponent())
                .build();
    }

    @NonNull
    @Override
    protected FlashcardsView getMvpView() {
        return this;
    }

    @NotNull
    @Override
    public Observable events() {
        return null;
    }

    @Override
    public void render(@NotNull ViewState viewState) {

    }

    @Override
    public void showWords(List<FlashcardWord> wordList, ReviewDirection direction) {
        viewFlipper.setDisplayedChild(CARDS);
        new Handler().post(() -> {
            revealedCards.clear();
            swipeDeck.setAdapterIndex(0);
            adapter.setData(wordList, direction);
        });


        buttonContainer.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        viewFlipper.setDisplayedChild(PROGRESS);
        buttonContainer.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        viewFlipper.setDisplayedChild(ERROR);
        buttonContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResultsNoErrors() {
        viewFlipper.setDisplayedChild(RESULT_NO_ERRORS);
        buttonContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResultErrors(List<FlashcardWord> wordList) {
        viewFlipper.setDisplayedChild(RESULT_ERRORS);
        ResultErrorWordsAdapter adapter = new ResultErrorWordsAdapter(wordList);
        resultErrorList.setAdapter(adapter);
        buttonContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOfflineModeDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.flashcard_error)
                .setMessage(R.string.flashcard_dialog_offline)
                .setPositiveButton(R.string.flashcard_dialog_yes, (dialog, which) -> {
                    getPresenter().onOfflineModeAccept();
                })
                .setNegativeButton(R.string.flashcard_dialog_no, (dialog, which) -> {
                    getPresenter().onOfflineModeDecline();
                })
                .setOnCancelListener(dialog -> getPresenter().onOfflineModeDecline())
                .show();
    }

    @OnClick({R.id.button_retry, R.id.flashcard_button_restart})
    public void onRetryClick() {
        getPresenter().onLoadFlashcards();
    }

    @OnClick(R.id.flashcard_button_back)
    public void onBackClick() {
        onBackPressed();
    }
}

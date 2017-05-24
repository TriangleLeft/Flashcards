package com.triangleleft.flashcards.ui.main;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.account.AccountModule;
import com.triangleleft.flashcards.service.cards.ReviewDirection;
import com.triangleleft.flashcards.service.settings.SettingsModule;
import com.triangleleft.flashcards.service.settings.UserData;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.triangleleft.flashcards.service.cards.ReviewDirection.BACKWARD;


public class FlashcardSettingsDialog extends AlertDialog {

    private static final int MIN_WORDS = 1;
    private static final int MAX_WORDS = 99;
    @Bind(R.id.flashcards_settings_words_amount)
    TextView wordsAmount;
    @Bind(R.id.flashcards_settings_seekbar)
    SeekBar seekBar;
    @Bind(R.id.flashcards_settings_direction_forward)
    RadioButton buttonForward;
    @Bind(R.id.flashcards_settings_direction_backward)
    RadioButton buttonBackward;
    @Inject
    AccountModule accountModule;
    @Inject
    SettingsModule settingsModule;

    public FlashcardSettingsDialog(@NonNull MainActivity activity, @NonNull DialogInterface.OnClickListener clickListener) {
        super(activity);
        activity.getComponent().inject(this);
        setTitle(R.string.review_flashcards);
        View view = activity.getLayoutInflater().inflate(R.layout.view_flashcards_seetings, null);
        setView(view);
        setButton(BUTTON_POSITIVE, activity.getString(R.string.start), clickListener);
        setButton(BUTTON_NEGATIVE, activity.getString(R.string.cancel), clickListener);
        ButterKnife.bind(this, view);

        seekBar.setMax(MAX_WORDS - MIN_WORDS);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress + MIN_WORDS;
                wordsAmount.setText(Integer.toString(progress));
                accountModule.setWordsAmount(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(accountModule.getWordsAmount() - MIN_WORDS);
        switch (accountModule.getWordsReviewDirection()) {
            case FORWARD:
                buttonForward.toggle();
                break;
            case BACKWARD:
                buttonBackward.toggle();
                break;
            default:
                throw new IllegalStateException("Unknown direction: " + accountModule.getWordsReviewDirection().name());
        }

        UserData userData = accountModule.getUserData().get();
        String learnId = userData.getLearningLanguageId().toUpperCase();
        String uiId = userData.getUiLanguageId().toUpperCase();
        buttonForward.setText(activity.getString(R.string.direction, learnId, uiId));
        buttonBackward.setText(activity.getString(R.string.direction, uiId, learnId));
    }

    @OnClick(R.id.flashcards_settings_direction_forward)
    public void onForwardClick() {
        accountModule.setWordsReviewDirection(ReviewDirection.FORWARD);
    }

    @OnClick(R.id.flashcards_settings_direction_backward)
    public void onBackwardClick() {
        accountModule.setWordsReviewDirection(BACKWARD);
    }



}

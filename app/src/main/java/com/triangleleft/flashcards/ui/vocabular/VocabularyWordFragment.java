package com.triangleleft.flashcards.ui.vocabular;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.di.vocabular.DaggerVocabularyWordComponent;
import com.triangleleft.flashcards.di.vocabular.VocabularyWordComponent;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.ui.common.BaseFragment;
import com.triangleleft.flashcards.ui.main.MainActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VocabularyWordFragment
        extends BaseFragment<VocabularyWordComponent, IVocabularyWordView, VocabularyWordPresenter>
        implements IVocabularyWordView {

    public static final String TAG = VocabularyWordFragment.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(VocabularyWordFragment.class);
    private static final int CONTENT = 0;
    private static final int EMPTY = 1;

    @Bind(R.id.vocabulary_word_title)
    TextView titleView;
    @Bind(R.id.vocabulary_word_translation_entry)
    View translationEntry;
    @Bind(R.id.vocabulary_word_translation_value)
    TextView translationView;
    @Bind(R.id.vocabulary_word_strength_value)
    VocabularyStrengthView strengthValue;
    @Bind(R.id.vocabulary_word_gender_entry)
    View genderEntry;
    @Bind(R.id.vocabulary_word_gender_value)
    TextView genderView;
    @Bind(R.id.vocabulary_word_pos_entry)
    View posEntry;
    @Bind(R.id.vocabulary_word_pos_value)
    TextView posView;
    @Bind(R.id.vocabulary_word_flipper)
    ViewFlipper flipper;

    private VoiceOverPlayer voiceOverPlayer = new VoiceOverPlayer();
    private VocabularyWord word;

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
    protected VocabularyWordComponent buildComponent() {
        logger.debug("buildComponent() called");
        return DaggerVocabularyWordComponent.builder()
                .mainPageComponent(((MainActivity) getActivity()).getComponent())
                .build();
    }

    @Override
    public void onPause() {
        super.onPause();
        voiceOverPlayer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        voiceOverPlayer.onResume();
    }

    @NonNull
    @Override
    protected IVocabularyWordView getMvpView() {
        logger.debug("getMvpView() called");
        return this;
    }

    @Override
    public void showWord(VocabularyWord word) {
        flipper.setDisplayedChild(CONTENT);
        this.word = word;
        titleView.setText(word.getWord());
        if (!word.getTranslations().isEmpty()) {
            translationView.setText(word.getTranslations().get(0));
        } else {
            translationView.setText(R.string.vocabulary_word_not_available);
        }
        strengthValue.setStrength(word.getStrength());
        if (word.getGender().isPresent()) {
            genderView.setText(word.getGender().get());
        } else {
            genderView.setText(R.string.vocabulary_word_not_available);
        }
        if (word.getPos().isPresent()) {
            posView.setText(word.getPos().get());
        } else {
            posView.setText(R.string.vocabulary_word_not_available);
        }
    }

    @Override
    public void showEmpty() {
        flipper.setDisplayedChild(EMPTY);
    }

    @OnClick(R.id.vocabular_word_voice)
    public void onVoiceClick() {
        voiceOverPlayer.voice(word);
    }

    private static class VoiceOverPlayer {
        // TODO: move to core module
        private final static String URL = "https://d7mj4aqfscim2.cloudfront.net/tts/%s/token/%s";
        private MediaPlayer mediaPlayer;
        private VocabularyWord currentWord;
        private boolean hasCompleted;

        public void onPause() {
            mediaPlayer.release();
        }

        public void onResume() {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(mp -> hasCompleted = true);
        }

        public void voice(VocabularyWord word) {
            // Start playing only if we want to voice some other word, or if we have completed playing
            if (!word.equals(currentWord) || hasCompleted) {
                hasCompleted = false;
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(String.format(URL, word.getLearningLanguage(), word.getNormalizedWord()));
                } catch (IOException e) {
                    // Do nothing
                }
                mediaPlayer.prepareAsync();
                currentWord = word;
            }
        }
    }
}

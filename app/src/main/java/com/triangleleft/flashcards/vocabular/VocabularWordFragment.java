package com.triangleleft.flashcards.vocabular;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.common.BaseFragment;
import com.triangleleft.flashcards.main.MainActivity;
import com.triangleleft.flashcards.mvp.vocabular.IVocabularWordView;
import com.triangleleft.flashcards.mvp.vocabular.VocabularWordPresenter;
import com.triangleleft.flashcards.service.vocabular.VocabularyWord;
import com.triangleleft.flashcards.vocabular.di.DaggerVocabularWordComponent;
import com.triangleleft.flashcards.vocabular.di.VocabularWordComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VocabularWordFragment
        extends BaseFragment<VocabularWordComponent, IVocabularWordView, VocabularWordPresenter>
        implements IVocabularWordView {

    public static final String TAG = VocabularWordFragment.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(VocabularWordFragment.class);
    @Bind(R.id.vocabular_word_title)
    TextView titleView;
    @Bind(R.id.vocabular_word_translation_value)
    TextView translationView;
    @Bind(R.id.vocabular_word_strength_value)
    VocabularStrengthView strengthValue;
    @Bind(R.id.vocabular_word_gender_value)
    TextView genderView;
    @Bind(R.id.vocabular_word_pos_value)
    TextView posView;
    @Bind(R.id.vocabular_word_gender_entry)
    View genderEntry;
    @Bind(R.id.vocabular_word_pos_entry)
    View posEntry;
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
    protected VocabularWordComponent buildComponent() {
        logger.debug("buildComponent() called");
        return DaggerVocabularWordComponent.builder()
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
    protected IVocabularWordView getMvpView() {
        logger.debug("getMvpView() called");
        return this;
    }

    @Override
    public void showWord(VocabularyWord word) {
        this.word = word;
        titleView.setText(word.getWord());
        String translation = word.getTranslations().size() > 0 ? word.getTranslations().get(0) : "";
        translationView.setText(translation);
        strengthValue.setStrength(word.getStrength());
        String gender = word.getGender();
        if (gender != null) {
            genderView.setText(gender);
        } else {
            genderEntry.setVisibility(View.GONE);
        }
        String pos = word.getPos();
        if (pos != null) {
            posView.setText(pos);
        } else {
            posEntry.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.vocabular_word_voice)
    public void onVoiceClick() {
        voiceOverPlayer.voice(word);
    }

    private static class VoiceOverPlayer {
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

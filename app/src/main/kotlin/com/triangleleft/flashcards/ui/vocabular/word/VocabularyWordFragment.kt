package com.triangleleft.flashcards.ui.vocabular.word

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewFlipper
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.OnClick
import com.triangleleft.flashcards.R
import com.triangleleft.flashcards.di.vocabular.DaggerVocabularyWordComponent
import com.triangleleft.flashcards.di.vocabular.VocabularyWordComponent
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.ViewEvent
import com.triangleleft.flashcards.ui.common.BaseFragment
import com.triangleleft.flashcards.ui.main.MainActivity
import com.triangleleft.flashcards.ui.vocabular.VocabularyStrengthView
import io.reactivex.Observable
import org.slf4j.LoggerFactory
import java.io.IOException

class VocabularyWordFragment : BaseFragment<VocabularyWordComponent, VocabularyWordView, VocabularyWordView.State, VocabularyWordPresenter>(), VocabularyWordView {

    @Bind(R.id.vocabulary_word_title)
    lateinit var titleView: TextView
    @Bind(R.id.vocabulary_word_translation_entry)
    lateinit var translationEntry: View
    @Bind(R.id.vocabulary_word_translation_value)
    lateinit var translationView: TextView
    @Bind(R.id.vocabulary_word_strength_value)
    lateinit var strengthValue: VocabularyStrengthView
    @Bind(R.id.vocabulary_word_gender_entry)
    lateinit var genderEntry: View
    @Bind(R.id.vocabulary_word_gender_value)
    lateinit var genderView: TextView
    @Bind(R.id.vocabulary_word_pos_entry)
    lateinit var posEntry: View
    @Bind(R.id.vocabulary_word_pos_value)
    lateinit var posView: TextView
    @Bind(R.id.vocabulary_word_flipper)
    lateinit var flipper: ViewFlipper

    private val voiceOverPlayer = VoiceOverPlayer()
    private var word: VocabularyWord? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vocabular_word, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun inject() {
        component.inject(this)
    }

    override fun buildComponent(): VocabularyWordComponent {
        logger.debug("buildComponent() called")
        return DaggerVocabularyWordComponent.builder()
                .mainPageComponent((activity as MainActivity).component)
                .build()
    }

    override fun onPause() {
        super.onPause()
        voiceOverPlayer.onPause()
    }

    override fun onResume() {
        super.onResume()
        voiceOverPlayer.onResume()
    }

    override val mvpView: VocabularyWordView
        get() = this

    @OnClick(R.id.vocabular_word_voice)
    fun onVoiceClick() {
        voiceOverPlayer.voice(word!!)
    }

    override fun render(viewState: VocabularyWordView.State) {
        val word = viewState.word
        if (word != null) {
            flipper.displayedChild = CONTENT
            this.word = word
            titleView.text = word.word
            if (!word.translations.isEmpty()) {
                translationView.text = word.translations[0]
            } else {
                translationView.setText(R.string.vocabulary_word_not_available)
            }
            strengthValue.setStrength(word.strength)
            genderView.text = word.gender.orElse(getString(R.string.vocabulary_word_not_available))
            posView.text = word.pos.orElse(getString(R.string.vocabulary_word_not_available))
        } else {
            flipper.displayedChild = EMPTY
        }
    }

    override fun events(): Observable<ViewEvent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private class VoiceOverPlayer {
        private var mediaPlayer: MediaPlayer? = null
        private var currentWord: VocabularyWord? = null
        private var hasCompleted: Boolean = false

        fun onPause() {
            mediaPlayer!!.release()
        }

        fun onResume() {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer!!.setOnPreparedListener({ it.start() })
            mediaPlayer!!.setOnCompletionListener { hasCompleted = true }
        }

        fun voice(word: VocabularyWord) {
            // Start playing only if we want to voice some other word, or if we have completed playing
            if (word != currentWord || hasCompleted) {
                hasCompleted = false
                try {
                    mediaPlayer!!.reset()
                    mediaPlayer!!.setDataSource(String.format(URL, word.learningLanguage, word.normalizedWord))
                } catch (e: IOException) {
                    // Do nothing
                }

                mediaPlayer!!.prepareAsync()
                currentWord = word
            }
        }

        companion object {
            // TODO: move to core module
            private val URL = "https://d7mj4aqfscim2.cloudfront.net/tts/%s/token/%s"
        }
    }

    companion object {

        val TAG = VocabularyWordFragment::class.java.simpleName
        private val logger = LoggerFactory.getLogger(VocabularyWordFragment::class.java)
        private val CONTENT = 0
        private val EMPTY = 1
    }
}

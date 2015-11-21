package com.triangleleft.flashcards.vocab;

import com.triangleleft.flashcards.MainActivity;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.IVocabularWord;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VocabularWordFragment extends Fragment implements MainActivity.IBackPressable {

    public static final String ARGUMENT_WORD = "argumentWord";
    public static final String TAG = VocabularWordFragment.class.getSimpleName();
    public static final String KEY_LOCATION = "keyLocation";
    public static final String KEY_TOP = "keyTop";
    public static final String KEY_TARGET_HEIGHT = "keyHeight";
    public static final String KEY_START_HEIGHT = "keyStartHeight";

    @Bind(R.id.background)
    View backgroundView;
    @Bind(R.id.list_item)
    View listItem;

    private ValueAnimator animator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_vocabular_word, container, false);
        ButterKnife.bind(this, view);

        Bundle arguments = getArguments();
        IVocabularWord word = arguments.getParcelable(ARGUMENT_WORD);
        int[] position = arguments.getIntArray(KEY_LOCATION);
        final float top = arguments.getInt(KEY_TOP);
        final int startHeight = arguments.getInt(KEY_START_HEIGHT);
        final int targetHeight = arguments.getInt(KEY_TARGET_HEIGHT);
        final float startScale = startHeight / (float) targetHeight;

        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        // AssertDialog.assertNotNull("No word was passed!", word);
        //layoutParams.topMargin = top;

        final IntEvaluator intEvaluator = new IntEvaluator();
        final FloatEvaluator floatEvaluator = new FloatEvaluator();
        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setDuration(getResources().getInteger(R.integer.animation_duration));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (Float) animation.getAnimatedValue();
                float scale = floatEvaluator.evaluate(fraction, startScale, 1.0);
                backgroundView.setScaleY(scale);
                backgroundView.setPivotY(0.0f);
                float translation = floatEvaluator.evaluate(fraction, top, 0);
                backgroundView.setTranslationY(translation);

                listItem.setTranslationY(translation);
            }
        });


        VocabularViewHolder vocabularViewHolder = new VocabularViewHolder(listItem, null);
        vocabularViewHolder.show(word);

        show(word);

        return view;
    }

    private void show(IVocabularWord word) {
        // wordView.setText(word.word);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Log.d(TAG, "onCreateAnimation()");
        if (enter) {
            animator.start();
        } else {
            animator.reverse();
        }
        Animation anim = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f);
        anim.setDuration(getResources().getInteger(R.integer.animation_duration));
        return anim;
    }


}

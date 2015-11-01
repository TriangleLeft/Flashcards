package com.triangleleft.flashcards.vocab;

import com.triangleleft.flashcards.R;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VocabularInfoDialog extends AppCompatDialogFragment {

    public static final String TAG = VocabularInfoDialog.class.getSimpleName();
    @Bind(R.id.vocabular_info_text)
    TextView infoView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.vocabular_info);
        ButterKnife.bind(this, dialog);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
    }
}

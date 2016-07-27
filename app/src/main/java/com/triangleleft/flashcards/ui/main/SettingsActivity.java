package com.triangleleft.flashcards.ui.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.ui.common.DrawableUtils;
import com.triangleleft.flashcards.ui.common.FlashcardsApplication;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        Drawable drawable = DrawableUtils
            .getTintedDrawable(this, R.drawable.ic_arrow_back_black_24dp, R.color.textColorPrimary);
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.content, new GeneralPreferenceFragment())
            .commit();
    }

    public static class GeneralPreferenceFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String string) {
            addPreferencesFromResource(R.xml.pref_general);
            Preference preference = findPreference(getString(R.string.logout));
            preference.setOnPreferenceClickListener(something -> {
                ((FlashcardsApplication) getActivity().getApplication()).navigateToLogin();
                return false;
            });

        }
    }
}

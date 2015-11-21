package com.triangleleft.flashcards.vocab;

import com.triangleleft.flashcards.Injector;
import com.triangleleft.flashcards.MainActivity;
import com.triangleleft.flashcards.R;
import com.triangleleft.flashcards.service.IVocabularModule;
import com.triangleleft.flashcards.service.IVocabularWord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class VocabularListFragment extends Fragment {

    public static final String TAG = VocabularListFragment.class.getSimpleName();
    @Inject
    IVocabularModule vocabularModule;
    @Bind(R.id.vocab_list)
    RecyclerView vocabList;
    private Observable<List<IVocabularWord>>
            listObservable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabular_list, container, false);
        ButterKnife.bind(this, view);
        Injector.INSTANCE.getComponent().inject(this);


        listObservable = vocabularModule.loadWords();
        listObservable.subscribeOn(AndroidSchedulers.mainThread()).subscribe().
        final VocabularAdapter adapter = new VocabularAdapter();
        adapter.setItemClickListener((viewHolder, position) -> {
            IVocabularWord word = adapter.getItem(position);
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                activity.onWordSelected(viewHolder.itemView, word);
            }

        });
        vocabList.setAdapter(adapter);
        vocabList.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return view;
    }

//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        if (!enter) {
//            return AnimationUtils.loadAnimation(getContext(), R.anim.list_fade_out);
//        } else {
//            return AnimationUtils.loadAnimation(getContext(), R.anim.list_fade_in);
//        }
//    }
}

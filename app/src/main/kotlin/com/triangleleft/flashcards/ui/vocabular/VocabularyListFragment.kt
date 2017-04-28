package com.triangleleft.flashcards.ui.vocabular

import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ViewFlipper
import butterknife.Bind
import butterknife.ButterKnife
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.support.v7.widget.scrollStateChanges
import com.jakewharton.rxbinding2.view.clicks
import com.triangleleft.flashcards.R
import com.triangleleft.flashcards.di.vocabular.DaggerVocabularyListComponent
import com.triangleleft.flashcards.di.vocabular.VocabularyListComponent
import com.triangleleft.flashcards.ui.common.BaseFragment
import com.triangleleft.flashcards.ui.common.OnItemClickListener
import com.triangleleft.flashcards.ui.main.MainActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory

class VocabularyListFragment : BaseFragment<VocabularyListComponent, IVocabularyListView, VocabularyListViewState, VocabularyListPresenter>(), IVocabularyListView {

    companion object {

        private val logger = LoggerFactory.getLogger(VocabularyListFragment::class.java)
        val TAG: String = VocabularyListFragment::class.java.simpleName

        private val PROGRESS = 0
        private val CONTENT = 1
        private val ERROR = 2
        private val EMPTY = 3
    }

    @Bind(R.id.vocab_list)
    lateinit var vocabList: RecyclerView
    @Bind(R.id.view_flipper)
    lateinit var viewFlipper: ViewFlipper
    @Bind(R.id.swipeRefreshLayout)
    lateinit var swipeRefresh: SwipeRefreshLayout
    @Bind(R.id.vocabulary_list_button_retry)
    lateinit var retryLoadButton: Button
    lateinit var vocabularyAdapter: VocabularyAdapter
    private var itemClickListener: OnItemClickListener<VocabularyViewHolder>? = null
    private var twoPane: Boolean = false

    private val wordSelections = PublishSubject.create<VocabularyWordSelectEvent>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        logger.debug("onCreateView() called with: inflater = [{}], container = [{}], savedInstanceState = [{}]",
                inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_vocabular_list, container, false)
        ButterKnife.bind(this, view)

        twoPane = resources.getBoolean(R.bool.two_panes)
        vocabularyAdapter = VocabularyAdapter(twoPane, wordSelections)
        vocabList.adapter = vocabularyAdapter
        vocabList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        swipeRefresh.setOnRefreshListener { presenter.onRefreshList() }
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.green)

        return view
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ButterKnife.unbind(this)
    }

    override fun inject() {
        component.inject(this)
    }

    override fun buildComponent(): VocabularyListComponent {
        return DaggerVocabularyListComponent.builder().mainPageComponent((activity as MainActivity).component).build()
    }

    override fun getMvpView(): IVocabularyListView {
        return this
    }

    override fun render(state: VocabularyListViewState) {
        val page = state.page
        when (page) {
            is VocabularyListViewState.Page.Progress -> viewFlipper.displayedChild = PROGRESS
            is VocabularyListViewState.Page.Error -> viewFlipper.displayedChild = ERROR
            is VocabularyListViewState.Page.Content -> {
                // On one hand this looks like a business logic
                // On the other, it's definitely a "list render"
                if (page.list.isNotEmpty()) {
                    viewFlipper.displayedChild = CONTENT
                    vocabularyAdapter.setData(page.list)
                    vocabularyAdapter.setSelectedPosition(page.selectedPosition)
                    // If we are in two pane view, it's first time we show data, and we want to select some valid position
                    // simulate click, in order for second panel to display something
                    // TODO: ??
                } else {
                    viewFlipper.displayedChild = EMPTY
                }
            }
        }
        // FIXME: always snaps =(
        //vocabList.scrollToPosition(state.scrollPosition)
        swipeRefresh.isRefreshing = state.isRefreshing
        if (state.hasRefreshError) {
            Snackbar.make(vocabList, R.string.vocabulary_list_refresh_error, Snackbar.LENGTH_LONG)
                    .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onShown(transientBottomBar: Snackbar?) {
                            super.onShown(transientBottomBar)
                        }

                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)
                            // TODO: reset has refresh error state?
                        }
                    })
                    .setAction(R.string.button_retry) {
                        // I hope it invokes listener TODO: check
                        swipeRefresh.isRefreshing = true
                    }.show()
        }
    }

    override fun refreshes(): Observable<Unit> {
        return swipeRefresh.refreshes()
    }

    override fun wordSelections(): Observable<VocabularyWordSelectEvent> {
        return wordSelections
    }

    override fun loadListRetires(): Observable<Unit> {
        return retryLoadButton.clicks()
    }

    override fun listScrolls(): Observable<Int> {
        return vocabList.scrollStateChanges().filter { it == RecyclerView.SCROLL_STATE_IDLE }.map { (vocabList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() }
    }
}

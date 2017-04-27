package com.triangleleft.flashcards.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.OnClick
import com.android.debug.hv.ViewServer
import com.annimon.stream.Optional
import com.triangleleft.flashcards.R
import com.triangleleft.flashcards.di.main.DaggerMainPageComponent
import com.triangleleft.flashcards.di.main.MainPageComponent
import com.triangleleft.flashcards.di.main.MainPageModule
import com.triangleleft.flashcards.service.vocabular.VocabularyWord
import com.triangleleft.flashcards.ui.cards.FlashcardsActivity
import com.triangleleft.flashcards.ui.common.BaseActivity
import com.triangleleft.flashcards.ui.main.drawer.NavigationView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory

class MainActivity : BaseActivity<MainPageComponent, IMainView, MainViewState, MainPresenter>(), IMainView {

    @Bind(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @Bind(R.id.navigation_view)
    lateinit var navigationView: NavigationView
    @Bind(R.id.main_container)
    lateinit var mainContainer: View

    lateinit var delegate: IMainActivityDelegate
    val backPresses: PublishSubject<Any> = PublishSubject.create<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.putParcelable(FRAGMENTS_TAG, null)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)

        val fixedDrawer = resources.getBoolean(R.bool.fixed_drawer)
        val twoPanes = resources.getBoolean(R.bool.two_panes)

        val configuration = resources.configuration
        val screenWidthDp = configuration.screenWidthDp
        val screenHeightDp = configuration.screenHeightDp
        logger.debug("onCreate() width: {}, height: {}", screenWidthDp, screenHeightDp)

        // TODO: Factory.buildViewDelegate(fixedDrawer, twoPanes);
        if (fixedDrawer && twoPanes) {
            delegate = TabletWideDelegate(this)
        } else if (fixedDrawer) {
            delegate = PhoneDelegate(this)
        } else {
            delegate = PhoneDelegate(this)
        }

        navigationView.init(component)
        ViewServer.get(this).addWindow(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewServer.get(this).removeWindow(this)
    }

    override fun inject() {
        component.inject(this)
    }

    override fun buildComponent(): MainPageComponent {
        return DaggerMainPageComponent.builder()
                .applicationComponent(applicationComponent)
                .mainPageModule(MainPageModule())
                .build()
    }

    override fun getMvpView(): IMainView {
        return this
    }

    override fun onBackPressed() {
        if (delegate.isDrawerOpen) {
            delegate.closeDrawer()
        } else {
            backPresses.onNext(Any())
        }
    }

    @OnClick(R.id.button_flashcards)
    fun onFlashcardsClick() {
        // Create custom dialog class with presenter?
        val settingsDialog = FlashcardSettingsDialog(this) { dialog, which ->
            when (which) {
                FlashcardSettingsDialog.BUTTON_POSITIVE -> {
                    val intent = Intent(this, FlashcardsActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    dialog.dismiss()
                    //presenter.setSettingsDialogShown(false)
                }
            }
        }
        settingsDialog.show()
        //presenter.setSettingsDialogShown(true)
        //        Intent intent = new Intent(this, FlashcardsActivity.class);
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //            int[] location = new int[2];
        //            view.getLocationOnScreen(location);
        //            int cx = location[0] + view.getWidth() / 2;
        //            int cy = location[1] + view.getHeight() / 2;
        //            intent.putExtra(FlashcardsActivity.REVEAL_X, cx);
        //            intent.putExtra(FlashcardsActivity.REVEAL_Y, cy);
        //            startActivity(intent);
        //            overridePendingTransition(-1, -1);
        //        } else {
        //            startActivity(intent);
        //        }
    }

    override fun render(it: MainViewState) {
        toolbar.title = it.title
        val page = it.page
        when (page) {
            is MainViewState.Page.List -> delegate.showList()
            is MainViewState.Page.Exit -> finish()
            is MainViewState.Page.Word -> {
                val word = Optional.ofNullable(page.word)
                delegate.showWord(word)
            }
        }
    }

    override fun backPresses(): Observable<Any> {
        return backPresses
    }

    override fun wordClicks(): Observable<VocabularyWord> {
        return Observable.empty()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MainActivity::class.java)
        private val FRAGMENTS_TAG = "android:support:fragments"
    }
}

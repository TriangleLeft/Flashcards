package com.triangleleft.flashcards.ui.main

import android.animation.FloatEvaluator
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.Bind
import butterknife.BindDimen
import butterknife.ButterKnife
import butterknife.OnClick
import com.pnikosis.materialishprogress.ProgressWheel
import com.squareup.picasso.Picasso
import com.triangleleft.flashcards.R
import com.triangleleft.flashcards.di.main.MainPageComponent
import com.triangleleft.flashcards.service.settings.Language
import com.triangleleft.flashcards.ui.common.FlagImagesProvider
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory
import javax.inject.Inject

class NavigationView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs), IDrawerView {

    @Bind(R.id.drawer_list)
    lateinit var recyclerView: RecyclerView
    @Bind(R.id.progress_wheel)
    lateinit var progressWheel: ProgressWheel
    @Bind(R.id.drawer_user_name)
    lateinit var drawerUserName: TextView
    @Bind(R.id.drawer_user_avatar)
    lateinit var drawerUserAvatar: ImageView
    @Bind(R.id.drawer_divider)
    lateinit var divider: View
    @Bind(R.id.drawer_overlay)
    lateinit var overlay: View
    @Bind(R.id.drawer_content)
    lateinit var content: View
    @JvmField @BindDimen(R.dimen.avatar_size)
    var avatarSize: Float = 0f
    @JvmField @BindDimen(R.dimen.drawer_item_language_icon_size)
    var drawerIconSize: Float = 0f
    @JvmField @BindDimen(R.dimen.drawer_list_slide_offset)
    var drawerListSlideOffset: Float = 0f
    @JvmField @BindDimen(R.dimen.drawer_header_avatar_margin_top)
    var drawerHeaderAvatarMarginTop: Float = 0f
    @JvmField @BindDimen(R.dimen.activity_vertical_margin)
    var activityVerticalMargin: Float = 0f

    @Inject
    lateinit var flagImagesProvider: FlagImagesProvider
    @Inject
    lateinit var presenter: DrawerPresenter

    private val evaluator = FloatEvaluator()
    private var adapter: DrawerLanguagesAdapter? = null
    private val layoutManager: LinearLayoutManager
    private val languageClicks = PublishSubject.create<Language>()
    private val errorShowCompletes = PublishSubject.create<Any>()
    private val states = PublishSubject.create<DrawerViewState>()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_navigation, this, true)
        ButterKnife.bind(this)

        drawerUserAvatar.pivotX = (-1 * drawerUserAvatar.measuredWidth / 2).toFloat()
        drawerUserAvatar.pivotY = (-1 * drawerUserAvatar.measuredHeight / 2).toFloat()

        layoutManager = object : LinearLayoutManager(context) {
            override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
                if (!isSmoothScrolling) {
                    val linearSmoothScroller = object : LinearSmoothScroller(recyclerView.context) {

                        private val SPEED = 150f

                        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                            return SPEED / displayMetrics.densityDpi
                        }
                    }
                    linearSmoothScroller.targetPosition = position
                    startSmoothScroll(linearSmoothScroller)
                }
            }
        }
        recyclerView.layoutManager = layoutManager

        states.map { it.page }.distinctUntilChanged().subscribe {
            when (it) {
                IDrawerView.Page.PROGRESS -> {
                    recyclerView.visibility = View.INVISIBLE
                    progressWheel.visibility = View.VISIBLE
                }
                IDrawerView.Page.CONTENT -> {
                    recyclerView.visibility = View.VISIBLE
                    progressWheel.visibility = View.INVISIBLE
                }
            }
        }
        states.map { it.avatar }.distinctUntilChanged().subscribe { Picasso.with(context).load(it).into(drawerUserAvatar) }
        states.map { it.username }.distinctUntilChanged().subscribe { drawerUserName.text = it }
        states.map { it.hasError }.distinctUntilChanged().subscribe {
            Toast.makeText(context, R.string.drawer_language_switch_error, Toast.LENGTH_SHORT).show()
            errorShowCompletes.onNext(Any())
        }
        states.map { it.languages }.distinctUntilChanged().subscribe {
            recyclerView.scrollToPosition(0)
            adapter?.setData(it)
        }
    }

    fun init(component: MainPageComponent) {
        component.inject(this)
        adapter = DrawerLanguagesAdapter(flagImagesProvider, languageClicks)
        recyclerView.adapter = adapter
        presenter.onCreate()
        presenter.onBind(this)
    }

    override fun languageClicks(): Observable<Language> {
        return languageClicks
    }

    override fun errorShowCompletes(): Observable<Any> {
        return errorShowCompletes
    }

    override fun render(state: DrawerViewState) {
        states.onNext(state)
    }

    fun setAnimationProgress(value: Float) {
        // 1f - expanded, 0f - collapsed
        val avatarScale = evaluator.evaluate(value, drawerIconSize / avatarSize, 1)
        drawerUserAvatar.scaleX = avatarScale!!
        drawerUserAvatar.scaleY = avatarScale

        val contentOffset = evaluator.evaluate(value, activityVerticalMargin * 2 - drawerHeaderAvatarMarginTop, 0)
        content.translationY = contentOffset!!

        val alpha = evaluator.evaluate(value, -1f, 1f)
        drawerUserName.alpha = alpha!!
        recyclerView.alpha = alpha
        divider.alpha = alpha

        val listOffset = evaluator.evaluate(value, -drawerListSlideOffset, 0)
        recyclerView.translationY = listOffset!!
        drawerUserName.translationY = listOffset
        divider.translationY = listOffset

        if (value == 1f) {
            overlay.visibility = View.GONE
        } else {
            overlay.visibility = View.VISIBLE
        }
    }

    fun setOnOverlayClickListener(listener: OnClickListener) {
        overlay.setOnClickListener(listener)
    }

    fun showListProgress() {
        recyclerView.visibility = View.INVISIBLE
        progressWheel.visibility = View.VISIBLE
    }

    fun resetScroll() {
        recyclerView.smoothScrollToPosition(0)
    }

    @OnClick(R.id.drawer_button_settings)
    fun onSettingsClick() {
        context.startActivity(Intent(context, SettingsActivity::class.java))
    }

    companion object {

        private val logger = LoggerFactory.getLogger(NavigationView::class.java)
    }
}

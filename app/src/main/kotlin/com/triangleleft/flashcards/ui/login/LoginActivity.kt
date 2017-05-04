package com.triangleleft.flashcards.ui.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.CheckableImageButton
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ViewFlipper
import butterknife.Bind
import butterknife.ButterKnife
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import com.triangleleft.flashcards.R
import com.triangleleft.flashcards.di.login.DaggerLoginActivityComponent
import com.triangleleft.flashcards.di.login.LoginActivityComponent
import com.triangleleft.flashcards.ui.common.BaseActivity
import com.triangleleft.flashcards.ui.main.MainActivity
import com.triangleleft.flashcards.ui.view.setTextIfChanged
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity<LoginActivityComponent, LoginView, LoginViewState, LoginPresenter>(), LoginView {

    @Bind(R.id.login_email)
    lateinit var loginView: EditText
    @Bind(R.id.login_email_layout)
    lateinit var loginLayoutView: TextInputLayout
    @Bind(R.id.login_password)
    lateinit var passwordView: EditText
    @Bind(R.id.login_password_layout)
    lateinit var passwordLayoutView: TextInputLayout
    @Bind(R.id.view_flipper)
    lateinit var flipperView: ViewFlipper
    @Bind(R.id.login_button)
    lateinit var loginButton: Button
    @Bind(R.id.login_switch)
    lateinit var rememberSwitch: SwitchCompat
    @Bind(R.id.login_container)
    lateinit var container: View

    private val states = PublishSubject.create<LoginViewState>()
    private val genericErrorShows = PublishSubject.create<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        logger.debug("onCreate() called with: savedInstanceState = [{}]", savedInstanceState)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        setupUI(container)

        states.map { it.login }.distinctUntilChanged().subscribe { setLogin(it) }
        states.map { it.password }.distinctUntilChanged().subscribe { setPassword(it) }
        states.map { it.loginButtonEnabled }.distinctUntilChanged().subscribe { setLoginButtonEnabled(it) }
        states.map { it.page }.distinctUntilChanged().subscribe { showPage(it) }
        states.map { it.hasLoginError }.distinctUntilChanged().subscribe { setLoginErrorVisible(it) }
        states.map { it.hasPasswordError }.distinctUntilChanged().subscribe { setPasswordErrorVisible(it) }
        states.map { it.shouldRememberUser }.distinctUntilChanged().subscribe { setRememberUser(it) }
        states.map { it.hasGenericError }.filter { it == true }.subscribe { showGenericError() }
    }

    override fun inject() {
        component.inject(this)
    }

    override fun buildComponent(): LoginActivityComponent {
        logger.debug("buildComponent() called")
        return DaggerLoginActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
    }

    override fun getMvpView(): LoginView {
        return this
    }

    override fun render(viewState: LoginViewState) {
        logger.debug("render() called with {}", viewState)
        states.onNext(viewState)
    }

    private fun showGenericError() {
        Toast.makeText(this, R.string.login_generic_error, Toast.LENGTH_SHORT).show()
        genericErrorShows.onNext(Any())
    }

    private fun showPage(page: LoginViewState.Page) {
        when (page) {
            LoginViewState.Page.PROGRESS -> flipperView.displayedChild = PROGRESS
            LoginViewState.Page.CONTENT -> {
                flipperView.displayedChild = CONTENT
                if (loginView.error != null) {
                    loginView.requestFocus()
                } else if (passwordView.error != null) {
                    passwordView.requestFocus()
                }
            }
            LoginViewState.Page.SUCCESS -> {
                finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
    }


    private fun setLoginButtonEnabled(enabled: Boolean) {
        loginButton.isEnabled = enabled
        if (enabled) {
            loginButton.background.colorFilter = null
        } else {
            loginButton.background.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun setLogin(login: String?) {
        loginView.setTextIfChanged(login)
    }

    private fun setPassword(password: String?) {
        passwordView.setTextIfChanged(password)
    }

    private fun setLoginErrorVisible(visible: Boolean) {
        loginLayoutView.error = if (visible) getString(R.string.wrong_login) else null
    }

    private fun setPasswordErrorVisible(visible: Boolean) {
        passwordLayoutView.error = if (visible) getString(R.string.wrong_password) else null
    }

    private fun setRememberUser(rememberUser: Boolean) {
        rememberSwitch.isChecked = rememberUser
    }

    override fun events(): Observable<LoginViewEvent> {
        return Observable.mergeArray(
                RxTextView.textChanges(loginView).map { SetLoginEvent(it.toString()) },
                RxTextView.textChanges(passwordView).map { SetPasswordEvent(it.toString()) },
                RxView.clicks(loginButton)
                        .map { LoginClickEvent(loginView.text.toString(), passwordView.text.toString()) },
                RxCompoundButton.checkedChanges(rememberSwitch).map { SetRememberUserEvent(it) },
                genericErrorShows.map { GenericErrorShownEvent }
        )
    }

    fun hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    fun setupUI(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        // We should also ignore "show password" icon
        if (view !is EditText && view !is CheckableImageButton) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            (0..view.childCount - 1)
                    .map { view.getChildAt(it) }
                    .forEach { setupUI(it) }
        }
    }

    companion object {

        private val logger = LoggerFactory.getLogger(LoginActivity::class.java)
        private val CONTENT = 0
        private val PROGRESS = 1
    }

}


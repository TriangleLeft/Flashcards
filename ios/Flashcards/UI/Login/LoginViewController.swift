//
//  LoginViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 22.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import FlashcardsCore
import Material

class LoginViewController: UIViewController  {
    
    @IBOutlet weak var contentView: UIStackView!
    
    @IBOutlet weak var activityIndicatorView: UIActivityIndicatorView!
    
    @IBOutlet weak var loginTextField: ErrorTextField!
    
    @IBOutlet weak var passwordTextField: ErrorTextField!
    
    @IBOutlet weak var rememberMeSwich: MaterialSwitch!
    
    @IBOutlet weak var loginButton: UIButton!
    
    var presenter:LoginPresenter;
    
    init(presenter:LoginPresenter) {
        self.presenter = presenter;
        super.init(nibName: nil, bundle: nil)
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        presenter.onBindWithIView(self);
        presenter.onCreate();
        
        
        // Close keyboard if we tap on background
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(LoginViewController.tap(_:)))
        view.addGestureRecognizer(tapGesture)
        
        activityIndicatorView.hidesWhenStopped = true;
    }
    
    override func viewDidAppear(animated: Bool) {
        presenter.onRebindWithIView(self);
    }
    
    func tap(gesture: UITapGestureRecognizer) {
        loginTextField.endEditing(true);
        passwordTextField.endEditing(true);
    }
    
    @IBAction func onLoginClick(sender: AnyObject) {
        presenter.onLoginClick();
    }
    
    @IBAction func onRememberMeChanged(sender: AnyObject) {
        presenter.onRememberCheckWithBoolean(rememberMeSwich.on);
    }
    
    @IBAction func onPasswordChanged(sender: AnyObject) {
        presenter.onPasswordChangedWithNSString(passwordTextField.text ?? "");
    }
    
    @IBAction func onLoginChanged(sender: AnyObject) {
        presenter.onLoginChangedWithNSString(loginTextField.text ?? "");
    }
    
}

extension LoginViewController: ILoginView {
    
    func setLoginButtonEnabledWithBoolean(enabled: jboolean) {
        loginButton.enabled = enabled;
    }
    
    func setLoginWithNSString(login: String?) {
        loginTextField.text = login;
    }
    
    func setPasswordWithNSString(password: String?) {
        passwordTextField.text = password;
    }
    
    func setLoginErrorVisibleWithBoolean(visible: jboolean) {
        loginTextField.revealError = visible;
        loginTextField.detail = "Wrong login";
    }
    
    func setPasswordErrorVisibleWithBoolean(visible: jboolean) {
        passwordTextField.revealError = visible;
        passwordTextField.detail = "Wrong password";
    }
    
    func showProgress() {
        activityIndicatorView.startAnimating();
        contentView.hidden = true;
    }
    
    func showContent() {
        contentView.hidden = false;
        activityIndicatorView.stopAnimating();
    }
    
    func advance() {
        let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        appDelegate.setMainViewController();
    }
    
    func setRememberUserWithBoolean(rememberUser: jboolean) {
        rememberMeSwich.on = rememberUser;
    }
    
    func notifyGenericError() {
        
    }
    
    func notifyNetworkError() {
        
    }
}

protocol LalalaDelegate:class {
    func lala(value:String)
}
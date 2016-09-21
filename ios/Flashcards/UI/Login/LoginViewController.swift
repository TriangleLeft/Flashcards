//
//  LoginViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 22.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import FlashcardsCore

class LoginViewController: UIViewController  {
    
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var activityIndicatorView: UIActivityIndicatorView!
    @IBOutlet weak var loginTextField: UITextField!
    @IBOutlet weak var loginErrorLabel: UILabel!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var passwordErrorLabel: UILabel!
    @IBOutlet weak var rememberMeSwitch: UISwitch!
    @IBOutlet weak var loginButton: UIButton!

    var presenter:LoginPresenter;
    
    init(presenter:LoginPresenter) {
        self.presenter = presenter;
        super.init(nibName: "LoginView", bundle: nil)
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        UITextField.connectFields([loginTextField, passwordTextField])
        
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
        presenter.onRememberCheckWithBoolean(rememberMeSwitch.on);
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
        loginErrorLabel.hidden = !visible
    }
    
    func setPasswordErrorVisibleWithBoolean(visible: jboolean) {
        passwordErrorLabel.hidden = !visible
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
        appDelegate.setMainViewController(true);
    }
    
    func setRememberUserWithBoolean(rememberUser: jboolean) {
        rememberMeSwitch.on = rememberUser;
    }
    
    func notifyGenericError() {
        
    }
    
    func notifyNetworkError() {
        
    }
}
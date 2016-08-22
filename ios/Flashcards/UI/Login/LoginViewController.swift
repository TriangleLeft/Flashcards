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
    
    @IBOutlet weak var contentView: UIStackView!

    @IBOutlet weak var activityIndicatorView: UIActivityIndicatorView!
    
    @IBOutlet weak var loginTextField: UITextField!
    
    @IBOutlet weak var passwordTextField: UITextField!
    
    @IBOutlet weak var rememberMeSwich: UISwitch!
    
    @IBOutlet weak var loginButton: UIButton!
    
    var presenter:LoginPresenter?;
    
    override func viewDidLoad() {
        super.viewDidLoad();
        presenter!.onBindWithIView(self);
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(LoginViewController.tap(_:)))
        view.addGestureRecognizer(tapGesture)
    }
    
    func tap(gesture: UITapGestureRecognizer) {
        loginTextField.endEditing(true);
        passwordTextField.endEditing(true);
    }
    
    @IBAction func onLoginClick(sender: AnyObject) {
        presenter!.onLoginClick();
    }
    
    @IBAction func onRememberMeChanged(sender: AnyObject) {
        presenter!.onRememberCheckWithBoolean(rememberMeSwich.on);
    }
    
    @IBAction func onPasswordChanged(sender: AnyObject) {
        presenter!.onPasswordChangedWithNSString(passwordTextField.text ?? "");
    }
    
    @IBAction func onLoginChanged(sender: AnyObject) {
        presenter!.onLoginChangedWithNSString(loginTextField.text ?? "");
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
        
    }
    
    func setPasswordErrorVisibleWithBoolean(visible: jboolean) {
        
    }
    
    func showProgress() {
        
    }
    
    func showContent() {
        
    }
    
    func advance() {
        
    }
    
    func setRememberUserWithBoolean(rememberUser: jboolean) {
        rememberMeSwich.on = rememberUser;
    }
    
    func showGenericError() {
        
    }
    
    func showNetworkError() {
        
    }
}
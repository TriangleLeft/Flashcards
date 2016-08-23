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
    
    var presenter:LoginPresenter?;
    
    override func viewDidLoad() {
        super.viewDidLoad();
        presenter!.onCreate();
        presenter!.onBindWithIView(self);
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(LoginViewController.tap(_:)))
        view.addGestureRecognizer(tapGesture)
        
        activityIndicatorView.hidesWhenStopped = true;
    }
    
    override func viewDidAppear(animated: Bool) {
        presenter!.onRebindWithIView(self);
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
        let controller = MainViewController();
        let toolbar = ToolbarController(rootViewController: controller);
        toolbar.toolbar.title = "Main controller"
        toolbar.toolbar.backgroundColor = MaterialColor.lightBlue.base
        toolbar.toolbar.titleLabel.textColor = MaterialColor.white
        toolbar.toolbar.titleLabel.textAlignment = .Center
        toolbar.statusBarStyle = .LightContent
        
        let img2: UIImage? = MaterialIcon.menu;
        let btn2: IconButton = IconButton()
        btn2.pulseColor = MaterialColor.white
        btn2.tintColor = MaterialColor.white
        btn2.setImage(img2, forState: .Normal)
        btn2.setImage(img2, forState: .Highlighted)
        
        let imgSettings: UIImage? = MaterialIcon.settings;
        let btnSettings: IconButton = IconButton()
        btnSettings.pulseColor = MaterialColor.white
        btnSettings.tintColor = MaterialColor.white
        btnSettings.setImage(imgSettings, forState: .Normal)
        btnSettings.setImage(imgSettings, forState: .Highlighted)
        
        toolbar.toolbar.leftControls = [btn2]
        toolbar.toolbar.rightControls = [btnSettings];
        
        let mainVC = VocabularyListTableViewController();
        let secondaryVC = VocabularyWordViewController();
        mainVC.delegate = secondaryVC;
        controller.viewControllers = [mainVC, secondaryVC];
        let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        appDelegate.window!.rootViewController = toolbar;
        //presentViewController(controller, animated: true, completion: nil);
        
        
    }
    
    func setRememberUserWithBoolean(rememberUser: jboolean) {
        rememberMeSwich.on = rememberUser;
    }
    
    func showGenericError() {
        
    }
    
    func showNetworkError() {
        
    }
}

protocol LalalaDelegate:class {
    func lala(value:String)
}
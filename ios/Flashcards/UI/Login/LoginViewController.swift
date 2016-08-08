//
//  LoginViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 08.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class LoginViewController: UIViewController {
    
    @IBOutlet weak var loginField: UITextField!
    @IBOutlet weak var passwordView: UITextField!
    @IBOutlet weak var rememberView: UISwitch!
    @IBOutlet weak var loginButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    @IBAction func onRememberChanged(sender: UISwitch) {

    }
    
    @IBAction func onLoginClick(sender: AnyObject) {
        loginField.text = "login"
        passwordView.text = "password"
        rememberView.setOn(!rememberView.on, animated: true)
    }
    
    
}
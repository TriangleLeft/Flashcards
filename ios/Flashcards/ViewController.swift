//
//  ViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 15.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import FlashcardsCore

class ViewController: UIViewController, FlashcardsNavigator {
    
    @objc
    override func viewDidLoad() {
        super.viewDidLoad()
//        JavaUtilConcurrentExecutor_class_();
//        JavaUtilConcurrentThreadPoolExecutor_class_();
//        JavaUtilConcurrentExecutorService_class_();
//        JavaUtilConcurrentScheduledExecutorService_class_();
        let gson = ComGoogleGsonGson();
        let storage = IOSPersistentStorage(gson);
        
        let restService = ObjRestService(gson: gson);
        
        let accountModule = SimpleAccountModule(persistentStorage: storage);
        
        let settingsModule = RestSettingsModule(restService: restService, withAccountModule: accountModule);
        
        let loginModule = RestLoginModule(restService: restService, withSettingsModule: settingsModule, withAccountModule: accountModule);
        
        let presenter = LoginPresenter(accountModule: accountModule, withLoginModule: loginModule, withRxScheduler: RxSchedulersSchedulers_immediate())
        
        presenter.onLoginChangedWithNSString("lekz112");
        presenter.onPasswordChangedWithNSString("samsung112");
        presenter.onLoginClick();
        
        //loginModule.loginWithNSString("lekz112", withNSString: "samsung112").subscribeWithRxObserver(MyObserver());
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func navigateToLogin() {
        
    }
    
}


//
//  ViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 15.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import FlashcardsCore

class ViewController: UIViewController, ComTriangleleftFlashcardsUiFlashcardsNavigator {

    @objc
    override func viewDidLoad() {
        super.viewDidLoad()
        JavaUtilConcurrentExecutor_class_();
        JavaUtilConcurrentThreadPoolExecutor_class_();
        JavaUtilConcurrentExecutorService_class_();
        JavaUtilConcurrentScheduledExecutorService_class_();
        let storage = PersistentStorage();
        let gson = ComGoogleGsonGson();
        let restService = ObjRestService(gson: gson);
        let accountModule = ComTriangleleftFlashcardsServiceAccountSimpleAccountModule(comTriangleleftFlashcardsUtilPersistentStorage: storage);
        
        let settingsModule = ComTriangleleftFlashcardsServiceSettingsRestRestSettingsModule(comTriangleleftFlashcardsServiceRestService: restService, withComTriangleleftFlashcardsServiceAccountAccountModule: accountModule);
        
        let loginModule = ComTriangleleftFlashcardsServiceLoginRestRestLoginModule(comTriangleleftFlashcardsServiceRestService: restService, withComTriangleleftFlashcardsServiceSettingsSettingsModule: settingsModule, withComTriangleleftFlashcardsServiceAccountAccountModule: accountModule);
        
        let presenter = ComTriangleleftFlashcardsUiLoginLoginPresenter(comTriangleleftFlashcardsServiceAccountAccountModule: accountModule, withComTriangleleftFlashcardsServiceLoginLoginModule: loginModule, withRxScheduler: RxSchedulersSchedulers_immediate())
        
        presenter.onLoginChangedWithNSString("lekz112");
        presenter.onPasswordChangedWithNSString("samsung112");
        presenter.onLoginClick();

        //loginModule.loginWithNSString("lekz112", withNSString: "samsung112").subscribeWithRxObserver(MyObserver());
    }
    class MyObserver : NSObject, RxObserver {

        func onCompleted() {
            print("Compl");
        }
        

        func onErrorWithNSException(e: NSException!) {
            print("Ex!", e);
        }
        

        func onNextWithId(t: AnyObject!) {
            print("Next!", t);
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func navigateToLogin() {
        
    }
    
    class Callback : NSObject, RxFunctionsAction1 {
        func callWithId(t: AnyObject!) {
            print(t);
        }
    }


}


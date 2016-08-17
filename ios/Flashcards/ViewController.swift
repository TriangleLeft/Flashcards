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

    override func viewDidLoad() {
        super.viewDidLoad()
        let component = ComTriangleleftFlashcardsDiDaggerApplicationComponent.builder()
            .applicationModuleWithComTriangleleftFlashcardsDiApplicationModule(ComTriangleleftFlashcardsDiApplicationModule())
            .netModuleWithComTriangleleftFlashcardsDiNetModule(ComTriangleleftFlashcardsDiNetModule())
            .persistenceModuleWithComTriangleleftFlashcardsDiPersistenceModule(ComTriangleleftFlashcardsDiPersistenceModule(comTriangleleftFlashcardsUtilPersistentStorage: PersistentStorage(), withOkhttp3CookieJar: CookieJar(), withComTriangleleftFlashcardsServiceVocabularVocabularyWordsRepository: VocabularyWordsRepository()))
            .build();
        
        ComTriangleleftFlashcardsServiceRestService
        
        
        let algorithm = JavaxNetSslTrustManagerFactory_getDefaultAlgorithm();
        let factory = JavaxNetSslTrustManagerFactory_getInstanceWithNSString_(algorithm);
        
        component.loginModule().loginWithNSString("login", withNSString: "password").subscribeWithRxFunctionsAction1(Callback(), withRxFunctionsAction1: Callback());
        
        print("whoa");
        
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


//
//  RestService.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 17.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import Foundation
import Alamofire
import FlashcardsCore

class RestService : NSObject, ComTriangleleftFlashcardsServiceRestServiceProtocol {
    
    
    func loginWithComTriangleleftFlashcardsServiceLoginRestLoginRequestController(model: ComTriangleleftFlashcardsServiceLoginRestLoginRequestController!) -> RxObservable {
        let observable = RxObservable.justWithId("JAJAJ");//(NSException(NSString: "llalal"));//(MyClosure())
        
        return observable;
    }
    
    class MyClosure : NSObject, RxObservable_OnSubscribe {
        
        func callWithId(t: AnyObject!) {
            let urlComponent = NSURLComponents();
            urlComponent.scheme = ComTriangleleftFlashcardsServiceRestService.BASE_SCHEME();
            urlComponent.host = ComTriangleleftFlashcardsServiceRestService.BASE_URL();
            urlComponent.path = ComTriangleleftFlashcardsServiceRestService.POST_LOGIN();
            
            let parameters = [
                "login":"login",
                "password":"password"
            ]

            let subscriber = t;
//            let requestReference = Alamofire.request(.POST, urlComponent, parameters: parameters, encoding: .JSON)
//                .validate()
//                .responseJSON(completionHandler: { (response) in
//                    print(response.request)  // original URL request
//                    print(response.response) // URL response
//                    print(response.data)     // server data
//                    print(response.result)   // result of response serialization
//                    
//                    if let JSON = response.result.value {
//                        print("JSON: \(JSON)")
//                    }
//                    subscriber.onErrorWithNSException(NSException(NSString: "jajaja"))
//                })
        }
    }

    
    func getVocabularyListWithLong(timestamp: jlong) -> RxObservable! {
        return nil;
    }
    
    func getFlashcardDataWithInt(count: jint, withBoolean allowPartialDeck: jboolean, withLong timestamp: jlong) -> RxObservable! {
        return nil;
    }
    
    func postFlashcardResultsWithComTriangleleftFlashcardsServiceCardsRestFlashcardResultsController(model: ComTriangleleftFlashcardsServiceCardsRestFlashcardResultsController!) -> RxObservable! {
        return nil;
    }
    
    func switchLanguageWithNSString(languageId: String!) -> RxObservable! {
        return nil;
    }
    
    func getUserDataWithNSString(userId: String!) -> RxObservable! {
        return nil;
    }
    
    func getTranslationWithNSString(languageIdFrom: String!, withNSString languageIdTo: String!, withNSString tokens: String!) -> RxObservable! {
        return nil;
    }
    
}
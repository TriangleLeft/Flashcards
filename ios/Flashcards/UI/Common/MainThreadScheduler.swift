//
//  MainThreadScheduler.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 25.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class MainThreadScheduler: RxScheduler {
    override func createWorker() -> RxScheduler_Worker! {
        return Worker();
    }
    
    class Worker: RxScheduler_Worker {
        
        // So apparently this should be thread safe
        var unsubscribed:Bool = false;
        
        override func scheduleWithRxFunctionsAction0(action: RxFunctionsAction0!) -> RxSubscription! {
            dispatch_async(dispatch_get_main_queue(), {
                action.call()
            })
            return RxSubscriptionsSubscriptions_unsubscribed()
        }
        
        override func scheduleWithRxFunctionsAction0(action: RxFunctionsAction0!, withLong delayTime: jlong, withJavaUtilConcurrentTimeUnit unit: JavaUtilConcurrentTimeUnit!) -> RxSubscription! {
            fatalError("Delayed action are not supported")
        //    return scheduleWithRxFunctionsAction0(action)
        }
        
        override func unsubscribe() {
            unsubscribed = true
        }
        
        override func isUnsubscribed() -> jboolean {
            return unsubscribed
        }
    }
}

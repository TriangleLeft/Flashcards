//
//  MainThreadExecutor.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 04.10.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class MainThreadExecutor: NSObject, JavaUtilConcurrentExecutor {

    func executeWithJavaLangRunnable(runnable:JavaLangRunnable) {
        if (NSThread.isMainThread()) {
            runnable.run()
        } else {
            dispatch_async(dispatch_get_main_queue(),{
                runnable.run()
                
            })
        }
    }
}

//
//  PersistentStorage.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 15.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import Foundation
import FlashcardsCore

class PersistentStorage : NSObject, ComTriangleleftFlashcardsUtilPersistentStorage {
    
    func putWithNSString(key: String!, withId value: AnyObject?) {
        
    }
    
    func getWithNSString(key: String!, withIOSClass clazz: IOSClass!) -> AnyObject? {
        return nil;
    }
    
    func getWithNSString(key: String!, withIOSClass clazz: IOSClass!, withId defaultValue: AnyObject?) -> AnyObject? {
        return nil;
    }

}
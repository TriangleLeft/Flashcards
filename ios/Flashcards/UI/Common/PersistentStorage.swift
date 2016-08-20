//
//  PersistentStorage.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 15.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import Foundation
import FlashcardsCore

class IOSPersistentStorage : NSObject,PersistentStorage {
    
    let gson:ComGoogleGsonGson;
    let userDefaults = NSUserDefaults.standardUserDefaults();
    
    init(_ gson: ComGoogleGsonGson) {
        self.gson = gson;
    }
    
    func putWithNSString(key: String!, withId value: AnyObject?) {
        var json:String? = nil;
        if (value != nil) {
            json = gson.toJsonWithId(value);
        }
        userDefaults.setObject(json, forKey: key);
    }
    
    func getWithNSString(key: String!, withIOSClass clazz: IOSClass!) -> AnyObject? {
        let json:String? = userDefaults.stringForKey(key);
        if (json != nil) {
            return gson.fromJsonWithNSString(json, withIOSClass: clazz);
        } else {
            return nil;
        }
    }
    
    func getWithNSString(key: String!, withIOSClass clazz: IOSClass!, withId defaultValue: AnyObject?) -> AnyObject? {
        let json:String? = userDefaults.stringForKey(key);
        if (json != nil) {
            return gson.fromJsonWithNSString(json, withIOSClass: clazz);
        } else {
            return defaultValue;
        }
    }
    
}
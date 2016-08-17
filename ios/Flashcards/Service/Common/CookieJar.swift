//
//  CookieJar.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 15.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import Foundation
import FlashcardsCore

class CookieJar: NSObject, Okhttp3CookieJarProtocol  {
    /*!
     @brief Saves <code>cookies</code> from an HTTP response to this store according to this jar's policy.
     <p>Note that this method may be called a second time for a single HTTP response if the response
     includes a trailer. For this obscure HTTP feature, <code>cookies</code> contains only the trailer's
     cookies.
     */
    func saveFromResponseWithOkhttp3HttpUrl(url: Okhttp3HttpUrl!, withJavaUtilList cookies: JavaUtilList!) {
        
    }
    
    /*!
     @brief Load cookies from the jar for an HTTP request to <code>url</code>.
     This method returns a possibly
     empty list of cookies for the network request.
     <p>Simple implementations will return the accepted cookies that have not yet expired and that
     match <code>url</code>.
     */
    func loadForRequestWithOkhttp3HttpUrl(url: Okhttp3HttpUrl!) -> JavaUtilList! {
        return nil;
    }
}
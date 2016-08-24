//
//  VocabularyWordsRepository.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 15.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import Foundation
import FlashcardsCore

class IOSVocabularyWordsRepository: NSObject, VocabularyWordsRepository {
    
    func getWordsWithNSString(uiLanguageId: String!, withNSString learningLanguageId: String!) -> JavaUtilList! {
        return JavaUtilArrayList();
    }
    
    func putWordsWithJavaUtilList(words: JavaUtilList!) {
        
    }
}
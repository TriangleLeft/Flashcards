//
//  VocabularyWordDao.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 28.09.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import Foundation
import CoreData

@objc(VocabularyWordDao)
class VocabularyWordDao: NSManagedObject {
    
    func toVocabularyWord() -> VocabularyWord {
        let list = JavaUtilArrayList()
        if translations != nil {
            for translationDao in translations! {
                list.addWithId(translationDao.translation)
            }
        }
        return VocabularyWord.createWithNSString(
            wordString,
            withNSString: normalizedString,
            withComAnnimonStreamOptional: ComAnnimonStreamOptional.ofNullableWithId(pos),
            withComAnnimonStreamOptional: ComAnnimonStreamOptional.ofNullableWithId(gender),
            withInt: strength,
            withJavaUtilList: list,
            withNSString: uiLanguage,
            withNSString: learningLanguage)
    }
}

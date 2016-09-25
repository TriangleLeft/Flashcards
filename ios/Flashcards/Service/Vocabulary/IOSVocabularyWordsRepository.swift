//
//  VocabularyWordsRepository.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 15.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import Foundation
import FlashcardsCore
import CoreData

class IOSVocabularyWordsRepository: NSObject, VocabularyWordsRepository {
    
    func getWordsWithNSString(uiLanguageId: String!, withNSString learningLanguageId: String!) -> JavaUtilList! {
        //var words = [NSManagedObject]()
        
        
        return JavaUtilArrayList();
    }
    
    func putWordsWithJavaUtilList(words: JavaUtilList!) {
        let iterator = words.iterator()
        let managedContext = AppDelegate.sharedAppDelegate().managedObjectContext
        managedContext.performBlock {
            // TODO: Need to delete old one first
            while iterator.hasNext() {
                let word:VocabularyWord = iterator.next() as! VocabularyWord
                let entityWord = NSEntityDescription.entityForName("VocabularyWordDao", inManagedObjectContext: managedContext)
                let newWord = NSManagedObject(entity: entityWord!, insertIntoManagedObjectContext: managedContext)
                
                if (word.getGender().isPresent()) {
                    newWord.setValue(word.getGender().get(), forKey: "gender")
                }
                if (word.getPos().isPresent()) {
                    newWord.setValue(word.getPos().get(), forKey: "pos")
                }
                newWord.setValue(word.getWord(), forKey: "wordString")
                newWord.setValue(word.getNormalizedWord(), forKey: "normalizedString")
                newWord.setValue(Int(word.getStrength()), forKey: "strength")
                newWord.setValue(word.getUiLanguage(), forKey: "uiLanguage")
                newWord.setValue(word.getLearningLanguage(), forKey: "learningLanguage")
                
                do {
                    try managedContext.save()
                } catch {
                    NSLog("Failed to save words into DB")
                }
            }
        }
    }
}
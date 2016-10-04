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
        let managedContext = AppDelegate.sharedAppDelegate().managedObjectContext
        let fetchRequest = NSFetchRequest(entityName: "VocabularyWordDao")
        let words = JavaUtilArrayList()
        fetchRequest.predicate = NSPredicate(format: "(uiLanguage == %@) AND (learningLanguage = %@)", uiLanguageId, learningLanguageId)
        
        let sortDescriptor = NSSortDescriptor(key: "normalizedString", ascending: true)
        let sortDescriptors = [sortDescriptor]
        fetchRequest.sortDescriptors = sortDescriptors
        
        do {
            if let results = try managedContext.executeFetchRequest(fetchRequest) as? [VocabularyWordDao] {
                for result in results {
                    words.addWithId(result.toVocabularyWord())
                }
            }
        } catch {
            NSLog("Failed to retrieve words")
        }
        
        return words
    }
    
    func putWordsWithJavaUtilList(words: JavaUtilList!) {
        if words.isEmpty() {
            return;
        }
        let managedContext = AppDelegate.sharedAppDelegate().managedObjectContext
        
        managedContext.performBlock {
            // Need to delete old one first
            let word:VocabularyWord = words.getWithInt(0) as! VocabularyWord
            let uiLanguageId = word.getUiLanguage()
            let learningLanguageId = word.getLearningLanguage()
            
            
            let fetchRequest = NSFetchRequest(entityName: "VocabularyWordDao")
            fetchRequest.predicate = NSPredicate(format: "(uiLanguage == %@) AND (learningLanguage = %@)", uiLanguageId, learningLanguageId)
            fetchRequest.includesPropertyValues = false
            
            do {
                if let results = try managedContext.executeFetchRequest(fetchRequest) as? [NSManagedObject] {
                    for result in results {
                        managedContext.deleteObject(result)
                    }
                    // No need to save it, as it would be done after we add items
                }
            } catch {
                NSLog("Failed to clear previous data")
            }
            let iterator = words.iterator()
            while iterator.hasNext() {
                let word:VocabularyWord = iterator.next() as! VocabularyWord
                let entityWord = NSEntityDescription.entityForName("VocabularyWordDao", inManagedObjectContext: managedContext)
                let newWord = VocabularyWordDao(entity: entityWord!, insertIntoManagedObjectContext: managedContext)
                
                if (word.getGender().isPresent()) {
                    newWord.gender = word.getGender().get() as? String
                }
                if (word.getPos().isPresent()) {
                    newWord.pos = word.getPos().get() as? String
                }
                newWord.wordString = word.getWord()
                newWord.normalizedString = word.getNormalizedWord()
                newWord.strength = word.getStrength()
                newWord.uiLanguage = word.getUiLanguage()
                newWord.learningLanguage = word.getLearningLanguage()
                
                newWord.translations = Set<VocabularyWordTranslationDao>()
                let translationIterator = word.getTranslations().iterator()
                while translationIterator.hasNext() {
                    let translation = translationIterator.next() as! String
                    let entityTranslation = NSEntityDescription.entityForName("VocabularyWordTranslationDao", inManagedObjectContext: managedContext)
                    let newTranslation = VocabularyWordTranslationDao(entity: entityTranslation!, insertIntoManagedObjectContext: managedContext)
                    
                    newTranslation.translation = translation
                    newTranslation.translates = newWord
                    
                    newWord.translations!.insert(newTranslation)
                }
                
            }
            
            AppDelegate.sharedAppDelegate().saveContext()
        }
    }
}

//
//  VocabularyWordDao+CoreDataProperties.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 28.09.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//
//  Choose "Create NSManagedObject Subclass…" from the Core Data editor menu
//  to delete and recreate this implementation file for your updated model.
//

import Foundation
import CoreData

extension VocabularyWordDao {

    @NSManaged var gender: String?
    @NSManaged var learningLanguage: String?
    @NSManaged var normalizedString: String?
    @NSManaged var pos: String?
    @NSManaged var strength: Int32
    @NSManaged var uiLanguage: String?
    @NSManaged var wordString: String?
    @NSManaged var translations: Set<VocabularyWordTranslationDao>?

}

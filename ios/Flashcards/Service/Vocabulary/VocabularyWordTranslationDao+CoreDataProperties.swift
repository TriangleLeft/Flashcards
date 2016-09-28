//
//  VocabularyWordTranslationDao+CoreDataProperties.swift
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

extension VocabularyWordTranslationDao {

    @NSManaged var translation: String?
    @NSManaged var translates: VocabularyWordDao?

}

//
//  ErrorWordCell.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 02.09.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import FlashcardsCore

class ErrorWordCell: UITableViewCell {

    @IBOutlet weak var translationLabel: UILabel!
    @IBOutlet weak var wordLabel: UILabel!
    
    func showWord(word: FlashcardWord) {
        wordLabel.text = word.getWord()
        translationLabel.text = word.getTranslation()
    }
    
}

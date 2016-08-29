//
//  VocabularyListTableViewCell.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class VocabularyListTableViewCell: UITableViewCell {
    
    @IBOutlet weak var iconView: VocabularyStrengthView!
    @IBOutlet weak var label: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    func showWord(word: VocabularyWord) {
        label.text = word.getWord()
        iconView.setStrength(Int(word.getStrength()))
    }
    
}

//
//  VocabularyWordViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class VocabularyWordViewController: UIViewController {
    
    @IBOutlet weak var wordLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        wordLabel.text = "lalala";
    }
}

extension VocabularyWordViewController : LalalaDelegate {
    func lala(value:String) {
        wordLabel.text = value
    }
}
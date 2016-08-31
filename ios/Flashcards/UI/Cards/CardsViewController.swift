//
//  CardsViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 30.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class CardsViewController: UIViewController {
    
    @IBOutlet weak var cardView: FlashcardView!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Need this because otherwise content would be placed below navigation bar
        self.edgesForExtendedLayout = .None
        
        
      //  cardView.textLabel.text = "Hey!"
        
    }

}

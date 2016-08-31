//
//  FlashcardView.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 31.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

@IBDesignable class FlashcardView: NibLoadingView {
    
    @IBOutlet weak var divider: UIView!
    @IBOutlet weak var wordLabelCenterY: NSLayoutConstraint!
    @IBOutlet weak var IWasWrongButton: UIButton!
    @IBOutlet weak var IWasRightButton: UIButton!
    @IBOutlet weak var showAnswerButton: UIButton!
    @IBOutlet weak var fakeWordLabel: UILabel!
    @IBOutlet weak var answerGroup: UIView!
    @IBOutlet weak var wordLabel: UILabel!
    @IBOutlet weak var translationLabel: UILabel!
    
    @IBAction func onButtonRight(sender: AnyObject) {
        NSLog("Right")
    }
    
    @IBAction func onButtonWrong(sender: AnyObject) {
        NSLog("Wrong")
    }
    
    @IBAction func onShowAnswer(sender: AnyObject) {
        UIView.animateWithDuration(0.3, delay: 0, options: [.CurveEaseInOut], animations: {
            // word label should move where fake when is placed
            self.wordLabel.center.y = self.fakeWordLabel.center.y
            }, completion: {
                finished in
                // Disable constraint that pinned word label to center of container
                self.wordLabelCenterY.active = false
                // Add new constraint to place word label above divider
                self.divider.topAnchor.constraintEqualToAnchor(self.wordLabel.bottomAnchor, constant: 16).active = true
        })
        
        UIView.animateWithDuration(0.3, delay: 0.1, options: [.CurveEaseInOut], animations: {
            // Show answer with buttons, hide original one
            self.answerGroup.alpha = 1
            self.showAnswerButton.alpha = 0
            }, completion: nil)
        
    }
}
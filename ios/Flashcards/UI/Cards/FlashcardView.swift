//
//  FlashcardView.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 31.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import FlashcardsCore

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
    
    var revealed: Bool = false
    var delegate: FlashcardViewDelegate?
    var word: FlashcardWord?
    
    @IBAction func onButtonRight(sender: AnyObject) {
        delegate!.onRightClicked(word)
    }
    
    @IBAction func onButtonWrong(sender: AnyObject) {
        delegate!.onWrongClicked(word)
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
            }, completion: {
                finished in
                self.revealed = true
        })
    }
    
    @IBInspectable var cornerRadius: CGFloat = 10
    
    @IBInspectable var shadowOffsetWidth: Int = 0
    @IBInspectable var shadowOffsetHeight: Int = 3
    @IBInspectable var shadowColor: UIColor? = UIColor.blackColor()
    @IBInspectable var shadowOpacity: Float = 0.5
    
    func showWord(word: FlashcardWord) {
        self.word = word
        wordLabel.text = word.getWord()
        translationLabel.text = word.getTranslation()
    }
    
    override func didLoad() {
        super.didLoad()
        view.layer.cornerRadius = cornerRadius
        view.layer.masksToBounds = true
        
    }
    
    override func layoutSubviews() {
        let shadowPath = UIBezierPath(roundedRect: bounds, cornerRadius: cornerRadius)
        
        layer.shadowColor = shadowColor?.CGColor
        layer.shadowOffset = CGSize(width: shadowOffsetWidth, height: shadowOffsetHeight);
        layer.shadowOpacity = shadowOpacity
        layer.shadowPath = shadowPath.CGPath
        layer.shouldRasterize = true
        layer.rasterizationScale = UIScreen.mainScreen().scale
        
    }
}

protocol FlashcardViewDelegate: class {
    
    func onRightClicked(word: FlashcardWord?)
    
    func onWrongClicked(word: FlashcardWord?)
    
}
//
//  FlashcardView.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 31.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

@IBDesignable
class FlashcardView: NibLoadingView {

    @IBOutlet weak var showAnswerButton: UIButton!
    @IBOutlet weak var answerGroup: UIView!
    @IBOutlet weak var wordLabel: UILabel!
    @IBOutlet weak var translationLabel: UILabel!
    @IBAction func onButtonRight(sender: AnyObject) {
    }
    @IBAction func onButtonWrong(sender: AnyObject) {
    }
    @IBAction func onShowAnswer(sender: AnyObject) {
        showAnswerButton.hidden = true
        answerGroup.hidden = false
    }
}

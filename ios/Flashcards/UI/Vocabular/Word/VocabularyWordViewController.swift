//
//  VocabularyWordViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class VocabularyWordViewController: UIViewController {
    
    @IBOutlet weak var infoEntry: UIStackView!
    @IBOutlet weak var posEntry: UIStackView!
    @IBOutlet weak var genderEntry: UIStackView!
    @IBOutlet weak var translationEntry: UIStackView!
    
    @IBOutlet weak var strengthView: VocabularyStrengthView!
    @IBOutlet weak var wordLabel: UILabel!
    @IBOutlet weak var posLabel: UILabel!
    @IBOutlet weak var genderLabel: UILabel!
    @IBOutlet weak var translationLabel: UILabel!
    let presenter:VocabularyWordPresenter;
    
    init(_ presenter:VocabularyWordPresenter) {
        self.presenter = presenter;
        super.init(nibName: nil, bundle: nil);
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Hidden by default
        infoEntry.hidden = true
        presenter.onCreate();
        presenter.onBindWithIView(self);
    }
    
    override func viewDidAppear(animated: Bool) {
        presenter.onRebindWithIView(self);
    }
}

extension VocabularyWordViewController: IVocabularyWordView {
    func showWordWithVocabularyWord(word: VocabularyWord!) {
        infoEntry.hidden = false
        strengthView.setStrength(Int(word.getStrength()))
        wordLabel.text = word.getWord()
        if (word.getTranslations().isEmpty()) {
            translationEntry.hidden = true;
        } else {
            translationEntry.hidden = false;
            translationLabel.text = word.getTranslations().getWithInt(0) as? String;
        }
        if (word.getPos().isPresent()) {
            posEntry.hidden = false
            posLabel.text = word.getPos().get() as? String
        } else {
            posEntry.hidden = true
        }
        if (word.getGender().isPresent()) {
            genderEntry.hidden = false
            genderLabel.text = word.getGender().get() as? String
        } else {
            genderEntry.hidden = true
        }
    }
    
    func showEmpty() {
        infoEntry.hidden = true;
    }
}

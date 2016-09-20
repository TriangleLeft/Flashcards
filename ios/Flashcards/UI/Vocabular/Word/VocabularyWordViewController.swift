//
//  VocabularyWordViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class VocabularyWordViewController: UIViewController {
    
    @IBOutlet weak var wordGroup: UIView!
    @IBOutlet weak var strengthGroup: UIView!
    @IBOutlet weak var translationGroup: UIView!
    @IBOutlet weak var genderGroup: UIView!
    @IBOutlet weak var posGroup: UIView!

    
    @IBOutlet weak var strengthView: VocabularyStrengthView!
    @IBOutlet weak var wordLabel: UILabel!
    @IBOutlet weak var posLabel: UILabel!
    @IBOutlet weak var genderLabel: UILabel!
    @IBOutlet weak var translationLabel: UILabel!
    let presenter:VocabularyWordPresenter;
    
    init(_ presenter:VocabularyWordPresenter) {
        self.presenter = presenter;
        super.init(nibName: "VocabularyWordView", bundle: nil);
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Hidden by default
        showEmpty()
        presenter.onCreate();
        presenter.onBindWithIView(self);
    }
    
    override func viewDidAppear(animated: Bool) {
        presenter.onRebindWithIView(self);
    }
}

extension VocabularyWordViewController: IVocabularyWordView {
    func showWordWithVocabularyWord(word: VocabularyWord!) {
        wordGroup.hidden = false
        strengthGroup.hidden = false
        translationGroup.hidden = false
        genderGroup.hidden = false
        posGroup.hidden = false
        
        // We expected that there is always word to show
        wordLabel.text = word.getWord()
        // We expect that every word has strength level
        strengthView.setStrength(Int(word.getStrength()))
        // Some words doesn't have tranlsations
        translationLabel.text = word.getTranslations().isEmpty() ? "N/A" : word.getTranslations().getWithInt(0) as! String;
        posLabel.text = (word.getPos().orElseWithId("N/A") as! String)
        genderLabel.text = (word.getGender().orElseWithId("N/A") as! String)
    }
    
    func showEmpty() {
        wordGroup.hidden = true
        strengthGroup.hidden = true
        translationGroup.hidden = true
        genderGroup.hidden = true
        posGroup.hidden = true
    }
}

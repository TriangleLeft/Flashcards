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
        wordLabel.text = "lalala";
        presenter.onCreate();
        presenter.onBindWithIView(self);
    }
    
    override func viewDidAppear(animated: Bool) {
        presenter.onRebindWithIView(self);
    }
}

extension VocabularyWordViewController: IVocabularyWordView {
    func showWordWithVocabularyWord(word: VocabularyWord!) {
        wordLabel.text = word.getWord();
    }
    
    func showEmpty() {
        wordLabel.text = "";
    }
}

extension VocabularyWordViewController : LalalaDelegate {
    func lala(value:String) {
        wordLabel.text = value
    }
}
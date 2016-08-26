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
        
//        navigationItem.leftBarButtonItem = splitViewController?.displayModeButtonItem()
//        navigationItem.leftItemsSupplementBackButton = true
        
        wordLabel.text = "lalala";
        presenter.onCreate();
        presenter.onBindWithIView(self);
    }
    
    override func viewDidAppear(animated: Bool) {
        presenter.onRebindWithIView(self);
    }
    
    func onBackPressed(sender: UIButton!) {
        //presenter.
    }
    
    class func build(presenter:VocabularyWordPresenter) -> VocabularyWordViewController {
        let controller = VocabularyWordViewController(presenter)
//        let toolbarController = ToolbarController(rootViewController: controller)
//        
//        toolbarController.toolbar.backgroundColor = MaterialColor.lightBlue.base
//        toolbarController.toolbar.titleLabel.textColor = MaterialColor.white
//        toolbarController.toolbar.titleLabel.textAlignment = .Center
//        toolbarController.statusBarStyle = .LightContent
//        toolbarController.toolbar.title = "Detail"
//        
//        let arrowBackImage: UIImage? = MaterialIcon.cm.arrowBack;
//        let backButton: IconButton = IconButton()
//        backButton.pulseColor = MaterialColor.white
//        backButton.tintColor = MaterialColor.white
//        backButton.setImage(arrowBackImage, forState: .Normal)
//        backButton.setImage(arrowBackImage, forState: .Highlighted)
//        backButton.addTarget(controller, action: #selector(VocabularyWordViewController.onBackPressed(_:)), forControlEvents: .TouchUpInside)
//        
//     //   let arrowBackImage: UIImage? = MaterialIcon.cm.arrowBack;
//        let fakeButton: IconButton = IconButton()
//        //backButton.pulseColor = MaterialColor.white
//        //backButton.tintColor = MaterialColor.white
//        //fakeButton.setImage(arrowBackImage, forState: .Normal)
//        //fakeButton.setImage(arrowBackImage, forState: .Highlighted)
//        fakeButton.hidden = true;
//        
//        toolbarController.toolbar.leftControls = [backButton]
//        toolbarController.toolbar.rightControls = [fakeButton]
//        
        
        return controller
    }
}

extension VocabularyWordViewController: IVocabularyWordView {
    func showWordWithVocabularyWord(word: VocabularyWord!) {
        infoEntry.hidden = false
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

extension VocabularyWordViewController : LalalaDelegate {
    func lala(value:String) {
        wordLabel.text = value
    }
}

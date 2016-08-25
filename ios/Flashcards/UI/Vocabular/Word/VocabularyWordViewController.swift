//
//  VocabularyWordViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import Material

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
    
    func onBackPressed(sender: UIButton!) {
        //presenter.
    }
    
    class func build(presenter:VocabularyWordPresenter) -> ToolbarController {
        let controller = VocabularyWordViewController(presenter)
        let toolbarController = ToolbarController(rootViewController: controller)
        
        toolbarController.toolbar.backgroundColor = MaterialColor.lightBlue.base
        toolbarController.toolbar.titleLabel.textColor = MaterialColor.white
        toolbarController.toolbar.titleLabel.textAlignment = .Center
        toolbarController.statusBarStyle = .LightContent
        toolbarController.toolbar.title = "Detail"
        
        let arrowBackImage: UIImage? = MaterialIcon.cm.arrowBack;
        let backButton: IconButton = IconButton()
        backButton.pulseColor = MaterialColor.white
        backButton.tintColor = MaterialColor.white
        backButton.setImage(arrowBackImage, forState: .Normal)
        backButton.setImage(arrowBackImage, forState: .Highlighted)
        backButton.addTarget(controller, action: #selector(VocabularyWordViewController.onBackPressed(_:)), forControlEvents: .TouchUpInside)
        
     //   let arrowBackImage: UIImage? = MaterialIcon.cm.arrowBack;
        let fakeButton: IconButton = IconButton()
        //backButton.pulseColor = MaterialColor.white
        //backButton.tintColor = MaterialColor.white
        //fakeButton.setImage(arrowBackImage, forState: .Normal)
        //fakeButton.setImage(arrowBackImage, forState: .Highlighted)
        fakeButton.hidden = true;
        
        toolbarController.toolbar.leftControls = [backButton]
        toolbarController.toolbar.rightControls = [fakeButton]
        
        
        return toolbarController;
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

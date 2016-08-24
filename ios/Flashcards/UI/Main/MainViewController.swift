//
//  MainViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import Material

class MainViewController: UISplitViewController, UISplitViewControllerDelegate {

    class func buildToolbarController(mainController:MainViewController) -> ToolbarController {
        let toolbarController = ToolbarController(rootViewController: mainController);
        mainController.toolbar = toolbarController.toolbar;
        
        toolbarController.toolbar.backgroundColor = MaterialColor.lightBlue.base
        toolbarController.toolbar.titleLabel.textColor = MaterialColor.white
        toolbarController.toolbar.titleLabel.textAlignment = .Center
        toolbarController.statusBarStyle = .LightContent
        
        let menuImage: UIImage? = MaterialIcon.menu;
        let menuButton: IconButton = IconButton()
        menuButton.pulseColor = MaterialColor.white
        menuButton.tintColor = MaterialColor.white
        menuButton.setImage(menuImage, forState: .Normal)
        menuButton.setImage(menuImage, forState: .Highlighted)
        
        let settingsImage: UIImage? = MaterialIcon.settings;
        let settingsButton: IconButton = IconButton()
        settingsButton.pulseColor = MaterialColor.white
        settingsButton.tintColor = MaterialColor.white
        settingsButton.setImage(settingsImage, forState: .Normal)
        settingsButton.setImage(settingsImage, forState: .Highlighted)
        
        toolbarController.toolbar.leftControls = [menuButton]
        toolbarController.toolbar.rightControls = [settingsButton];
  
        return toolbarController;
    }
    
    let presenter:MainPresenter
    var toolbar:Toolbar?
    let listVC:VocabularyListTableViewController;
    let wordVC:VocabularyWordViewController;
    
    init(_ presenter:MainPresenter, listPrensenter:VocabularyListPresenter, wordPresenter:VocabularyWordPresenter) {
        self.presenter = presenter;
        self.listVC = VocabularyListTableViewController(listPrensenter);
        self.wordVC = VocabularyWordViewController(wordPresenter);
        
        super.init(nibName: nil, bundle: nil);
    
        viewControllers = [listVC, wordVC];
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        presenter.onCreate();
        presenter.onBindWithIView(self);
    }
    
    override func viewDidAppear(animated: Bool) {
        presenter.onRebindWithIView(self);
    }
    
    func splitViewController(splitViewController: UISplitViewController, collapseSecondaryViewController secondaryViewController: UIViewController, ontoPrimaryViewController primaryViewController: UIViewController) -> Bool{
        return true
    }

}

extension MainViewController: IMainView {
    
    func setTitleWithNSString(title: String!) {
        toolbar!.title = title;
    }
    
    func showWordWithComAnnimonStreamOptional(word: ComAnnimonStreamOptional!) {
        wordVC.presenter.showWordWithComAnnimonStreamOptional(word);
        if (collapsed) {
            showDetailViewController(wordVC, sender: nil)
        }
    }
    
    func showList() {
        if (collapsed) {
            showViewController(listVC, sender: nil);
        }
    }
    
    func finish() {
        
    }
    
    func reloadList() {
        
    }
    
    func showDrawerProgress() {
        
    }
    
    func showUserDataWithNSString(username: String!, withNSString avatar: String!, withJavaUtilList languages: JavaUtilList!) {
        
    }
    
    func showDrawerError() {
        
    }
}

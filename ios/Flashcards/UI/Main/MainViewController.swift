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
    
    let presenter:MainPresenter
    let listVC:ToolbarController;
    let wordVC:ToolbarController;
    
    init(_ presenter:MainPresenter, listPrensenter:VocabularyListPresenter, wordPresenter:VocabularyWordPresenter) {
        self.presenter = presenter;
        self.listVC = VocabularyListTableViewController.build(listPrensenter);
        self.wordVC = VocabularyWordViewController.build(wordPresenter);
        
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
        listVC.toolbar.title = title;
    }
    
    func showWordWithComAnnimonStreamOptional(word: ComAnnimonStreamOptional!) {
        (wordVC.rootViewController as! VocabularyWordViewController).presenter.showWordWithComAnnimonStreamOptional(word);
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

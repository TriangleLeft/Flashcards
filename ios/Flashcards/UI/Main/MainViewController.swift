//
//  MainViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import DrawerController

class MainViewController: UISplitViewController, UISplitViewControllerDelegate {
    
    let presenter:MainPresenter
    let listVC:VocabularyListViewController
    let wordVC:VocabularyWordViewController
    let navListVC:UINavigationController
    let navWordVC:UINavigationController
    let defaultWidthFraction:CGFloat = 0.4
    
    var collapseDetailViewController: Bool = true
    
    init(_ presenter:MainPresenter, listPrensenter:VocabularyListPresenter, wordPresenter:VocabularyWordPresenter) {
        self.presenter = presenter;
        self.listVC = VocabularyListViewController(listPrensenter)
        self.wordVC = VocabularyWordViewController(wordPresenter);
        self.navListVC = UINavigationController(rootViewController: listVC)
        self.navWordVC = UINavigationController(rootViewController: wordVC)
        
        super.init(nibName: nil, bundle: nil);
        
        self.listVC.mainDelegate = self
        
        // Set navigation bar colors
        navListVC.navigationBar.barTintColor = UIColor.flashcardsPrimary()
        navListVC.navigationBar.tintColor = UIColor.whiteColor()
        navListVC.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.whiteColor()]
        navWordVC.navigationBar.barTintColor = UIColor.flashcardsPrimary()
        navWordVC.navigationBar.tintColor = UIColor.whiteColor()
        
        delegate = self
        viewControllers = [navListVC, navWordVC];
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        maximumPrimaryColumnWidth = view.bounds.size.width;
        preferredPrimaryColumnWidthFraction = defaultWidthFraction
        preferredDisplayMode = .AllVisible
        
        presenter.onCreate();
        presenter.onBindWithIView(self);
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        presenter.onRebindWithIView(self);
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    func splitViewController(splitViewController: UISplitViewController, collapseSecondaryViewController secondaryViewController: UIViewController, ontoPrimaryViewController primaryViewController: UIViewController) -> Bool {
        return collapseDetailViewController
    }
}

extension MainViewController: MainViewControllerDelegate {
    func onMenuClicked() {
        let drawer: DrawerController = self.parentViewController as! DrawerController
        if (drawer.openSide == .None) {
            drawer.openDrawerSide(.Left, animated: true, completion: nil)
            
        } else {
            drawer.closeDrawerAnimated(true, completion: nil)
        }
    }
}

extension MainViewController: IMainView {
    
    func setTitleWithNSString(title: String!) {
        listVC.title = title
    }
    
    func showWordWithComAnnimonStreamOptional(word: ComAnnimonStreamOptional!) {
        wordVC.presenter.showWordWithComAnnimonStreamOptional(word);
        collapseDetailViewController = false
        if (collapsed && word.isPresent()) {
            showDetailViewController(wordVC, sender: self)
        }
    }
    
    func showList() {
        if (collapsed) {
            showViewController(navListVC, sender: nil);
        }
    }
    
    func finish() {
        
    }
    
    func reloadList() {
        listVC.presenter.onLoadList()
    }
}

protocol MainViewControllerDelegate: class {
    func onMenuClicked()
}

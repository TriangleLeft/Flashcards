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
    let listVC:VocabularyListTableViewController
    let wordVC:VocabularyWordViewController
    let navListVC:UINavigationController
    let navWordVC:UINavigationController
    var collapseDetailViewController = true
    
    init(_ presenter:MainPresenter, listPrensenter:VocabularyListPresenter, wordPresenter:VocabularyWordPresenter) {
        self.presenter = presenter;
        self.listVC = VocabularyListTableViewController(listPrensenter)
        self.wordVC = VocabularyWordViewController(wordPresenter);
        self.navListVC = UINavigationController(rootViewController: listVC)
        self.navWordVC = UINavigationController(rootViewController: wordVC)
        
        super.init(nibName: nil, bundle: nil);
        
        preferredDisplayMode = .AllVisible
        delegate = self
        
        viewControllers = [navListVC, navWordVC];
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        presenter.onCreate();
        presenter.onBindWithIView(self);
        
        wordVC.navigationItem.title = "Detail"
        listVC.navigationItem.title = "Main"
    }
    
    override func viewDidAppear(animated: Bool) {
        presenter.onRebindWithIView(self);
    }
    
    func splitViewController(splitViewController: UISplitViewController, collapseSecondaryViewController secondaryViewController: UIViewController, ontoPrimaryViewController primaryViewController: UIViewController) -> Bool {
        return collapseDetailViewController
    }
    
    class func wrapWithDrawer(vc: MainViewController, drawerPresenter:DrawerPresenter) -> UIViewController {
        let drawerVC = DrawerViewController(presenter: drawerPresenter)
        let drawer = DrawerController(centerViewController: vc, leftDrawerViewController: drawerVC)
        drawer.openDrawerGestureModeMask = .BezelPanningCenterView
        drawer.closeDrawerGestureModeMask = .PanningCenterView
        drawer.centerHiddenInteractionMode = .CloseDrawer
        drawer.showsShadows = false
        drawer.shouldStretchDrawer = false
        return drawer
    }
    
}

extension MainViewController: IMainView {
    
    func setTitleWithNSString(title: String!) {
        listVC.navigationItem.title = title
    }
    
    func showWordWithComAnnimonStreamOptional(word: ComAnnimonStreamOptional!) {
        wordVC.presenter.showWordWithComAnnimonStreamOptional(word);
        collapseDetailViewController = false
        if (collapsed) {
          //  evo_drawerController?.openDrawerGestureModeMask = []
        
            showDetailViewController(wordVC, sender: self)
        }
    }
    
    func showList() {
        if (collapsed) {
           // evo_drawerController?.openDrawerGestureModeMask = .BezelPanningCenterView
            showViewController(navListVC, sender: nil);
        }
    }
    
    func finish() {
        
    }
    
    func reloadList() {
        listVC.presenter.onLoadList()
    }
}

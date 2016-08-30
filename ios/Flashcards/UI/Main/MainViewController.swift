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
    let defaultWidthFraction:CGFloat = 0.4
    
    var collapseDetailViewController: Bool = true
    var splitVCGestureRecognizer: UIGestureRecognizer? = nil
    
    init(_ presenter:MainPresenter, listPrensenter:VocabularyListPresenter, wordPresenter:VocabularyWordPresenter) {
        self.presenter = presenter;
        self.listVC = VocabularyListTableViewController(listPrensenter)
        self.wordVC = VocabularyWordViewController(wordPresenter);
        self.navListVC = UINavigationController(rootViewController: listVC)
        self.navWordVC = UINavigationController(rootViewController: wordVC)
        
        super.init(nibName: nil, bundle: nil);
        
        self.listVC.mainDelegate = self
        
        // Set navigation bar colors
        navListVC.navigationBar.barTintColor = UIColor.flashcardsPrimary()
        navListVC.navigationBar.tintColor = UIColor.whiteColor()
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
     
        splitVCGestureRecognizer = view.gestureRecognizers![0]
        
        presenter.onCreate();
        presenter.onBindWithIView(self);
        wordVC.navigationItem.title = "Detail"
        listVC.navigationItem.title = "Main"
        
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
    
    class func wrapWithDrawer(vc: MainViewController, drawerPresenter:DrawerPresenter) -> UIViewController {
        let drawerVC = DrawerViewController(presenter: drawerPresenter)
        let drawer = DrawerController(centerViewController: vc, leftDrawerViewController: drawerVC)
        drawer.openDrawerGestureModeMask = .BezelPanningCenterView
        drawer.closeDrawerGestureModeMask = .PanningCenterView
        drawer.centerHiddenInteractionMode = .CloseDrawer
        drawer.showsShadows = true
        drawer.shouldStretchDrawer = false
        return drawer
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
    
    func setMasterCollapsed(collapsed: Bool) {
        if (collapsed) {
            preferredPrimaryColumnWidthFraction = defaultWidthFraction
        } else {
            preferredPrimaryColumnWidthFraction = 1.0
        }
        NSLog("Collaped %@", collapsed)
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
            
            let arr = view.gestureRecognizers
            //let recog:UIGestureRecognizer = view.gestureRecognizers![0]
            //recog.enabled = false
            //recog.delegate = nil
            //recog.
            splitVCGestureRecognizer?.enabled = false
            
            if let drawer: DrawerController = self.parentViewController as? DrawerController {
                // Disable drawer
                if let array = drawer.view.gestureRecognizers {
                    for recognizer in array {
                        recognizer.enabled = false
                    }
                }
            }
            //  evo_drawerController?.openDrawerGestureModeMask = []
            
            showDetailViewController(wordVC, sender: self)
        }
    }
    
    func showList() {
        if (collapsed) {
            // Enable drawer
            if let drawer: DrawerController = self.parentViewController as? DrawerController {
                if let array = drawer.view.gestureRecognizers {
                    for recognizer in array {
                        recognizer.enabled = true
                    }
                }
            }
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

//
//  VocabularListTableViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class VocabularyListTableViewController: UITableViewController {
    
    let nibStringName = "VocabularyListTableViewCell"
    let presenter:VocabularyListPresenter
    let activityIndicatorView:UIActivityIndicatorView = UIActivityIndicatorView(activityIndicatorStyle: UIActivityIndicatorViewStyle.Gray)
    
    var items:JavaUtilList = JavaUtilArrayList()
    var showContent = false
    weak var mainDelegate:MainViewControllerDelegate?
    
    init(_ presenter:VocabularyListPresenter) {
        self.presenter = presenter;
        super.init(nibName: nil, bundle: nil)
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Add flashcards button
        let flashcardsImage = UIImage(named: "ic_flashcards")!.imageWithRenderingMode(.AlwaysTemplate)
        let flashcardsButton = UIButton(type: .InfoDark)
        flashcardsButton.setImage(flashcardsImage, forState: .Normal)
        flashcardsButton.frame = CGRect(x: 0, y: 0, width: flashcardsImage.size.width, height: flashcardsImage.size.height)
        flashcardsButton.addTarget(self, action: #selector(VocabularyListTableViewController.onFlashcardsClick), forControlEvents: .TouchUpInside)
        navigationItem.rightBarButtonItem = UIBarButtonItem(customView: flashcardsButton)
        
        // Add drawer button
        let menuImage = UIImage(named: "ic_menu_black_24dp")!.imageWithRenderingMode(.AlwaysTemplate)
        let menuButton = UIButton(type: .InfoDark)
        menuButton.setImage(menuImage, forState: .Normal)
        menuButton.frame = CGRect(x: 0, y: 0, width: menuImage.size.width, height: menuImage.size.height)
        menuButton.addTarget(self, action: #selector(VocabularyListTableViewController.onMenuClick), forControlEvents: .TouchUpInside)
        navigationItem.leftBarButtonItem = UIBarButtonItem(customView: menuButton)
        
        tableView.registerNib(UINib(nibName: nibStringName, bundle: nil), forCellReuseIdentifier: nibStringName)
        tableView.backgroundView = activityIndicatorView
        tableView.separatorStyle = .None
        // Fake footer to hide empty cells and last divider
        tableView.tableFooterView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 1))
        
        presenter.onBindWithIView(self)
        presenter.onCreate()
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        NSLog("appear")
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        presenter.onRebindWithIView(self)
    }
    
    func onFlashcardsClick() {
        let cardsController: CardsViewController = AppDelegate.sharedAppDelegate().buildCardsViewController()
        self.navigationController?.delegate = self
        mainDelegate?.setMasterCollapsed(false)
        self.navigationController?.pushViewController(cardsController, animated: false)
    }
    
    func onMenuClick() {
        mainDelegate?.onMenuClicked()
    }
}

extension VocabularyListTableViewController: UINavigationControllerDelegate {
    func navigationController(navigationController: UINavigationController, willShowViewController viewController: UIViewController, animated: Bool) {
        if (viewController == self) {
            mainDelegate?.setMasterCollapsed(true)
        }
        NSLog("Will show %@", viewController)
    }
}

extension VocabularyListTableViewController {
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (showContent) {
            return Int(items.size())
        } else {
            return 0;
        }
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(nibStringName, forIndexPath: indexPath) as! VocabularyListTableViewCell
        
        let item = items.getWithInt(Int32(indexPath.row)) as! VocabularyWord
        cell.showWord(item)
        
        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        presenter.onWordSelectedWithInt(Int32(indexPath.row))
    }
}

extension VocabularyListTableViewController: IVocabularyListView {
    
    func showWordsWithJavaUtilList(words: JavaUtilList, withInt selectedPosition: jint) {
        activityIndicatorView.stopAnimating()
        items = words
        showContent = true
        tableView.reloadData()
        tableView.separatorStyle = .SingleLine
        // on Ipads, simulate click so the second controller shows selected word
        if (self.traitCollection.horizontalSizeClass == .Regular) {
            let indexPath:NSIndexPath = NSIndexPath(forRow: Int(selectedPosition), inSection: 0)
            tableView.selectRowAtIndexPath(indexPath, animated: false, scrollPosition: .Middle)
            presenter.onWordSelectedWithInt(0)
        }
    }
    
    func showProgress() {
        activityIndicatorView.startAnimating()
        showContent = false
        tableView.reloadData()
        tableView.separatorStyle = .None
    }
    
    func showRefreshError() {
        
    }
    
    func showLoadError() {
        
    }
    
    func showEmpty() {
        
    }
}

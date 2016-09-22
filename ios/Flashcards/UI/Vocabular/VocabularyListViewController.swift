//
//  VocabularListTableViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class VocabularyListViewController: UIViewController {
    
    @IBOutlet weak var loadErrorGroup: UIView!
    @IBOutlet weak var noKnownWordsLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    let nibStringName = "VocabularyListTableViewCell"
    let presenter:VocabularyListPresenter
    let refreshControl = UIRefreshControl()
    
    var items:JavaUtilList = JavaUtilArrayList()
    weak var mainDelegate:MainViewControllerDelegate?
    
    init(_ presenter:VocabularyListPresenter) {
        self.presenter = presenter;
        super.init(nibName: "VocabularyListView", bundle: nil)
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationController?.navigationBar.translucent = false
        
        // Add flashcards button
        let flashcardsImage = UIImage(named: "ic_flashcards")!.imageWithRenderingMode(.AlwaysTemplate)
        let flashcardsButton = UIButton(type: .InfoDark)
        flashcardsButton.setImage(flashcardsImage, forState: .Normal)
        flashcardsButton.frame = CGRect(x: 0, y: 0, width: flashcardsImage.size.width, height: flashcardsImage.size.height)
        flashcardsButton.addTarget(self, action: #selector(VocabularyListViewController.onFlashcardsClick), forControlEvents: .TouchUpInside)
        navigationItem.rightBarButtonItem = UIBarButtonItem(customView: flashcardsButton)
        
        // Add drawer button
        let menuImage = UIImage(named: "ic_menu_black_24dp")!.imageWithRenderingMode(.AlwaysTemplate)
        let menuButton = UIButton(type: .InfoDark)
        menuButton.setImage(menuImage, forState: .Normal)
        menuButton.frame = CGRect(x: 0, y: 0, width: menuImage.size.width, height: menuImage.size.height)
        menuButton.addTarget(self, action: #selector(VocabularyListViewController.onMenuClick), forControlEvents: .TouchUpInside)
        navigationItem.leftBarButtonItem = UIBarButtonItem(customView: menuButton)
        
        tableView.registerNib(UINib(nibName: nibStringName, bundle: nil), forCellReuseIdentifier: nibStringName)
        tableView.separatorStyle = .SingleLine
        tableView.delegate = self
        tableView.dataSource = self
        
        // Fake footer to hide empty cells and last divider
        //     tableView.tableFooterView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.frame.size.width, height: 1))
        
        refreshControl.addTarget(self, action: #selector(VocabularyListViewController.refresh(_:)), forControlEvents: UIControlEvents.ValueChanged)
        tableView.addSubview(refreshControl)
        
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
    
    func refresh(sender:AnyObject) {
        presenter.onRefreshList()
    }
    
    func onFlashcardsClick() {
        let cardsController: UIViewController = AppDelegate.sharedAppDelegate().buildCardsViewController()
        presentViewController(cardsController, animated: true, completion: nil)
    }
    
    func onMenuClick() {
        mainDelegate?.onMenuClicked()
    }
    
    private func setPage(page: Page) {
        tableView.hidden = true
        loadErrorGroup.hidden = true
        noKnownWordsLabel.hidden = true
        activityIndicator.stopAnimating()
        switch page {
        case .Progress:
            activityIndicator.startAnimating()
            break
        case .Content:
            tableView.hidden = false
            break
        case .LoadError:
            loadErrorGroup.hidden = false
            break
        case .NoContent:
            noKnownWordsLabel.hidden = false
            break
        }
    }
    
    @IBAction func onRetryButtonClick(sender: AnyObject) {
        presenter.onLoadList()
    }
    
    private enum Page {
        case Progress
        case Content
        case NoContent
        case LoadError
    }
}

extension VocabularyListViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return Int(items.size())
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(nibStringName, forIndexPath: indexPath) as! VocabularyListTableViewCell
        
        let item = items.getWithInt(Int32(indexPath.row)) as! VocabularyWord
        cell.showWord(item)
        
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        presenter.onWordSelectedWithInt(Int32(indexPath.row))
    }
}

extension VocabularyListViewController: IVocabularyListView {
    
    func showWordsWithJavaUtilList(words: JavaUtilList, withInt selectedPosition: jint) {
        setPage(.Content)
        refreshControl.endRefreshing()
        items = words
        tableView.reloadData()
        // on Ipads, simulate click so the second controller shows selected word
        if (self.traitCollection.horizontalSizeClass == .Regular) {
            let indexPath:NSIndexPath = NSIndexPath(forRow: Int(selectedPosition), inSection: 0)
            tableView.selectRowAtIndexPath(indexPath, animated: false, scrollPosition: .Middle)
            presenter.onWordSelectedWithInt(0)
        }
    }
    
    func showProgress() {
        setPage(.Progress)
    }
    
    func showRefreshError() {
        
    }
    
    func showLoadError() {
        setPage(.LoadError)
    }
    
    func showEmpty() {
        setPage(.NoContent)
    }
}

//
//  MainDrawerViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 26.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class DrawerViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var userNameLabel: UILabel!
    @IBOutlet weak var avatarView: UIImageView!
    
    let presenter:DrawerPresenter
    let nibStringName = "VocabularyListTableViewCell"
    let cellIdentifier = "languageCell"
    let activityIndicatorView:UIActivityIndicatorView = UIActivityIndicatorView(activityIndicatorStyle: .White)
    
    var items:JavaUtilList = JavaUtilArrayList();
    var showContent:Bool = false
    
    init(presenter:DrawerPresenter) {
        self.presenter = presenter;
        super.init(nibName: "DrawerViewController", bundle: nil)
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.registerNib(UINib(nibName: "DrawerViewCell", bundle: nil), forCellReuseIdentifier: cellIdentifier)
        tableView.dataSource = self
        tableView.delegate = self
        tableView.backgroundColor = .clearColor()
        tableView.backgroundView = activityIndicatorView
        tableView.separatorStyle = .None
        
        presenter.onBindWithIView(self)
        presenter.onCreate()
    }
}

extension DrawerViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (showContent) {
            return Int(items.size())
        } else {
            return 0
        }
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier, forIndexPath: indexPath) as! DrawerViewCell
        
        let item = items.getWithInt(Int32(indexPath.row)) as! Language
        
        cell.titleLabel.text = item.getName()
        cell.levelLabel.text = String(item.getLevel())
        
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        presenter.onLanguageSelectedWithLanguage(items.getWithInt(Int32(indexPath.row)) as! Language)
    }
    
    func tableView(tableView: UITableView, willDisplayCell cell: UITableViewCell, forRowAtIndexPath indexPath: NSIndexPath) {
        cell.backgroundColor = .clearColor()
    }
}

extension DrawerViewController: IDrawerView {
    func showUserDataWithNSString(username: String!, withNSString avatar: String!, withJavaUtilList languages: JavaUtilList!) {
        userNameLabel.text = username
        items = languages
        
        activityIndicatorView.stopAnimating()
        showContent = true
        tableView.separatorStyle = .SingleLine
        tableView.reloadData()
    }
    
    func notifyLanguageSwitchError() {
        
    }
    
    
    func notifyUserDataUpdateError() {
        
    }
    
    func showListProgress() {
        activityIndicatorView.startAnimating()
        showContent = false;
        tableView.separatorStyle = .None
        tableView.reloadData()
    }

}

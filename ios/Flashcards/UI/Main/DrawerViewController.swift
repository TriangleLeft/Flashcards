//
//  MainDrawerViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 26.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import Haneke
import JLToast

class DrawerViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var userNameLabel: UILabel!
    @IBOutlet weak var avatarView: UIImageView!
    @IBOutlet weak var settingsButton: UIButton!
    
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
        
        let settingsImage = UIImage(named: "ic_settings")!.imageWithRenderingMode(.AlwaysTemplate)
        settingsButton.tintColor = UIColor.whiteColor()
        settingsButton.setImage(settingsImage, forState: .Normal)
        
        avatarView.layer.cornerRadius = avatarView.frame.size.width / 2
        avatarView.clipsToBounds = true
        
        tableView.registerNib(UINib(nibName: "DrawerViewCell", bundle: nil), forCellReuseIdentifier: cellIdentifier)
        tableView.dataSource = self
        tableView.delegate = self
        tableView.backgroundColor = .clearColor()
        tableView.backgroundView = activityIndicatorView
        tableView.separatorStyle = .None
        
        presenter.onBindWithIView(self)
        presenter.onCreate()
    }
    
    @IBAction func onSettingsButtonClick(sender: AnyObject) {
        let settingController: UIViewController = AppDelegate.sharedAppDelegate().buildSettingsViewController()
        presentViewController(settingController, animated: true, completion: nil)
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
        
        cell.showLanguage(item)
        
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
        avatarView.hnk_setImageFromURL(NSURL(string: avatar)!)
        activityIndicatorView.stopAnimating()
        showContent = true
        tableView.separatorStyle = .SingleLine
        tableView.reloadData()
    }
    
    func notifyLanguageSwitchError() {
        JLToast.makeText("Failed to switch language", duration: JLToastDelay.ShortDelay).show()
    }
    
    
    func notifyUserDataUpdateError() {
        JLToast.makeText("Failed to update user info", duration: JLToastDelay.ShortDelay).show()
    }
    
    func showListProgress() {
        activityIndicatorView.startAnimating()
        showContent = false;
        tableView.separatorStyle = .None
        tableView.reloadData()
    }

}

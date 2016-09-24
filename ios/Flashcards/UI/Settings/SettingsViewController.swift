//
//  SettingsViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.09.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class SettingsViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet var tableViewCell: UITableViewCell!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationController?.navigationBar.translucent = false
        navigationItem.leftBarButtonItem = UIBarButtonItem(title: "Done", style: .Done, target: self, action: #selector(SettingsViewController.onDoneClick))
        
        tableView.dataSource = self
        tableView.delegate = self
    }
    
    func onDoneClick() {
        dismissViewControllerAnimated(true, completion: nil)
    }
}

extension SettingsViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        return tableViewCell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        AppDelegate.sharedAppDelegate().navigateToLogin()
    }
}

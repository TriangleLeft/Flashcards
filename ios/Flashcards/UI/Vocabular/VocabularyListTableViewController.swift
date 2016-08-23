//
//  VocabularListTableViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class VocabularyListTableViewController: UITableViewController {
    
    let items = ["Item 1", "Item2", "Item3", "Item4"]
    
    weak var delegate: LalalaDelegate?
    
    let nibStringName = "VocabularyListTableViewCell";
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.registerNib(UINib(nibName: nibStringName, bundle: nil), forCellReuseIdentifier: nibStringName)
    }

    // MARK: - Table view data source

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return items.count
    }

    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(nibStringName, forIndexPath: indexPath) as! VocabularyListTableViewCell

        let item = items[indexPath.row]
        
        cell.label.text = item
        

        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        delegate?.lala(items[indexPath.row])
        if let detailViewController = self.delegate as? VocabularyWordViewController {
            splitViewController?.showDetailViewController(detailViewController, sender: nil)
        }
    }
}

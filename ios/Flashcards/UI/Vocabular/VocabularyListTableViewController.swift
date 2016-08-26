//
//  VocabularListTableViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import Material

class VocabularyListTableViewController: UITableViewController {
    
    weak var delegate: LalalaDelegate?
    var items:JavaUtilList = JavaUtilArrayList();
    
    let nibStringName = "VocabularyListTableViewCell";
    
    let presenter:VocabularyListPresenter;
    
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
        
        tableView.registerNib(UINib(nibName: nibStringName, bundle: nil), forCellReuseIdentifier: nibStringName)
        
        presenter.onCreate()
        presenter.onBindWithIView(self)
    }
    
    override func viewDidAppear(animated: Bool) {
        presenter.onRebindWithIView(self)
    }
    
    class func build(presenter:VocabularyListPresenter) -> VocabularyListTableViewController {
        let controller = VocabularyListTableViewController(presenter)
        
        return controller;
    }
}

extension VocabularyListTableViewController {
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return Int(items.size())
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(nibStringName, forIndexPath: indexPath) as! VocabularyListTableViewCell
        
        let item = items.getWithInt(Int32(indexPath.row)) as! VocabularyWord;
        
        cell.label.text = item.getWord();
        
        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        presenter.onWordSelectedWithInt(Int32(indexPath.row))
    }
}

extension VocabularyListTableViewController: IVocabularyListView {
    
    func showWordsWithJavaUtilList(words: JavaUtilList, withInt selectedPosition: jint) {
        self.items = words
        tableView.reloadData()
    }
    
    func showProgress() {
        
    }
    
    func showRefreshError() {
        
    }
    
    func showLoadError() {
        
    }
    
    func showEmpty() {
        
    }
}

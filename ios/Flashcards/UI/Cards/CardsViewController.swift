//
//  CardsViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 30.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import Koloda

class CardsViewController: UIViewController {
    
    @IBOutlet weak var kolodaView: KolodaView!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    @IBOutlet weak var errorsView: UIView!
    @IBOutlet weak var noErrorsLabel: UILabel!
    @IBOutlet weak var errorsTableView: UITableView!
    
    let presenter: FlashcardsPresenter
    let nibStringName: String = "ErrorWordCell"
    
    var wordList:JavaUtilList = JavaUtilArrayList()
    var errorWordList:JavaUtilList = JavaUtilArrayList()
    
    init(_ presenter:FlashcardsPresenter) {
        self.presenter = presenter;
        super.init(nibName: "CardsView", bundle: nil)
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Need this because otherwise content would be placed below navigation bar
        self.edgesForExtendedLayout = .None
        
        kolodaView.dataSource = self
        kolodaView.delegate = self
        
        errorsTableView.dataSource = self
        errorsTableView.delegate = self
        errorsTableView.registerNib(UINib(nibName: nibStringName, bundle: nil), forCellReuseIdentifier: nibStringName)
        // Fake footer to hide empty cells and last divider
       // errorsTableView.tableFooterView = UIView(frame: CGRect(x: 0, y: 0, width: errorsTableView.frame.size.width, height: 1))
        
        navigationItem.leftBarButtonItem = UIBarButtonItem(title: "Back", style: .Plain, target: self, action: #selector(CardsViewController.onDoneClick))
        navigationItem.rightBarButtonItem = UIBarButtonItem(title: "Restart", style: .Plain, target: self, action: #selector(CardsViewController.onRestartClick))
        
        presenter.onBindWithIView(self)
        presenter.onCreate()
        
    }
    
    func onRestartClick() {
        presenter.onLoadFlashcards()
    }
    
    func onDoneClick() {
        dismissViewControllerAnimated(true, completion: nil)
    }
}

extension CardsViewController: IFlashcardsView {
    
    func showWordsWithJavaUtilList(wordList: JavaUtilList!) {
        setPage(.Content)
        self.wordList = wordList
        kolodaView.resetCurrentCardIndex()
    }
    
    func showProgress() {
        setPage(.Progress)
    }
    
    func showError() {
        
    }
    
    func showResultsNoErrors() {
        setPage(.NoErrors)
    }
    
    func showResultErrorsWithJavaUtilList(wordList: JavaUtilList!) {
        setPage(.Errors)
        errorWordList = wordList
        errorsTableView.reloadData()
    }
    
    private func setPage(page: Page) {
        kolodaView.hidden = true
        errorsView.hidden = true
        noErrorsLabel.hidden = true
        activityIndicator.stopAnimating()
        switch page {
        case .Progress:
            activityIndicator.startAnimating()
            break
        case .Content:
            kolodaView.hidden = false
            break
        case .Errors:
            errorsView.hidden = false
            break
        case .NoErrors:
            noErrorsLabel.hidden = false
            break
        }
    }
    
    private enum Page {
        case Progress
        case Content
        case NoErrors
        case Errors
    }
}

extension CardsViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return Int(errorWordList.size())
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(nibStringName, forIndexPath: indexPath) as! ErrorWordCell
        let item = errorWordList.getWithInt(Int32(indexPath.row)) as! FlashcardWord
        
        cell.showWord(item)
        
        return cell
    }
}


extension CardsViewController: FlashcardViewDelegate {
    
    func onRightClicked(word: FlashcardWord?) {
        presenter.onWordRightWithFlashcardWord(word)
        kolodaView.swipe(.Right)
    }
    
    func onWrongClicked(word: FlashcardWord?) {
        presenter.onWordWrongWithFlashcardWord(word)
        kolodaView.swipe(.Left)
        
    }
}

extension CardsViewController: KolodaViewDelegate {
    
    func kolodaDidRunOutOfCards(koloda: KolodaView) {
        presenter.onCardsDepleted()
    }
    
    func koloda(koloda: KolodaView, shouldDragCardAtIndex index: UInt ) -> Bool {
        let view = kolodaView.viewForCardAtIndex(Int(index)) as! FlashcardView
        return view.revealed
    }
}

extension CardsViewController: KolodaViewDataSource {
    
    func kolodaNumberOfCards(koloda:KolodaView) -> UInt {
        return UInt(wordList.size())
    }
    
    func koloda(koloda: KolodaView, viewForCardAtIndex index: UInt) -> UIView {
        let card = FlashcardView(frame: kolodaView.frame)
        card.delegate = self
        let word = wordList.getWithInt(Int32(index)) as! FlashcardWord
        card.showWord(word)
        return card
    }
    
    func koloda(koloda: KolodaView, viewForCardOverlayAtIndex index: UInt) -> OverlayView? {
        return nil
    }
}

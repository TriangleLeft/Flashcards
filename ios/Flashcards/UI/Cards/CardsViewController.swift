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
    
    let presenter: FlashcardsPresenter
    
    var wordList:JavaUtilList = JavaUtilArrayList()
    
    init(_ presenter:FlashcardsPresenter) {
        self.presenter = presenter;
        super.init(nibName: nil, bundle: nil)
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
        
        presenter.onBindWithIView(self)
        presenter.onCreate()
        
    }
}

extension CardsViewController: IFlashcardsView {
    
    func showWordsWithJavaUtilList(wordList: JavaUtilList!) {
        activityIndicator.stopAnimating()
        self.wordList = wordList
        kolodaView.reloadData()
    }
    
    func showProgress() {
        activityIndicator.startAnimating()
    }
    
    func showError() {
        
    }
    
    func showResultsNoErrors() {
        
    }
    
    func showResultErrorsWithJavaUtilList(wordList: JavaUtilList!) {
        
    }
    
}

extension CardsViewController: FlashcardViewDelegate {
    
    func onRightClicked(word: FlashcardWord?) {
        kolodaView.swipe(.Right)
        presenter.onWordRightWithFlashcardWord(word)
    }
    
    func onWrongClicked(word: FlashcardWord?) {
        kolodaView.swipe(.Left)
        presenter.onWordWrongWithFlashcardWord(word)
    }
}

extension CardsViewController: KolodaViewDelegate {
    
    func kolodaDidRunOutOfCards(koloda: KolodaView) {
        presenter.onCardsDepleted()
    }
    
    func koloda(koloda: KolodaView, didSelectCardAtIndex index: UInt) {
        //UIApplication.sharedApplication().openURL(NSURL(string: "http://yalantis.com/")!)
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
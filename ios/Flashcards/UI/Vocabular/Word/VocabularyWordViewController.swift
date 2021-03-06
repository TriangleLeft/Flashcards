//
//  VocabularyWordViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit
import AVFoundation

class VocabularyWordViewController: UIViewController {
    
    @IBOutlet weak var contentGroup: UIView!
    
    @IBOutlet weak var voiceButton: UIButton!
    @IBOutlet weak var strengthView: VocabularyStrengthView!
    @IBOutlet weak var wordLabel: UILabel!
    @IBOutlet weak var posLabel: UILabel!
    @IBOutlet weak var genderLabel: UILabel!
    @IBOutlet weak var translationLabel: UILabel!
    let presenter:VocabularyWordPresenter;
    let voiceUrl = "https://d7mj4aqfscim2.cloudfront.net/tts/%@/token/%@"
    var player:AVPlayer?
    
    init(_ presenter:VocabularyWordPresenter) {
        self.presenter = presenter;
        super.init(nibName: "VocabularyWordView", bundle: nil);
    }
    
    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Hidden by default
        showEmpty()
        presenter.onCreate();
        presenter.onBindWithIView(self);
    }
    
    override func viewDidAppear(animated: Bool) {
        presenter.onRebindWithIView(self);
    }

    @IBAction func onVoiceClick(sender: AnyObject) {
        player?.seekToTime(kCMTimeZero)
        player?.play()
    }
}

extension VocabularyWordViewController: IVocabularyWordView {
    func showWordWithVocabularyWord(word: VocabularyWord!) {
        contentGroup.hidden = false
        
        // We expected that there is always word to show
        wordLabel.text = word.getWord()
        // We expect that every word has strength level
        strengthView.setStrength(Int(word.getStrength()))
        // Some words doesn't have tranlsations
        translationLabel.text = word.getTranslations().isEmpty() ? "N/A" : word.getTranslations().getWithInt(0) as! String;
        posLabel.text = (word.getPos().orElseWithId("N/A") as! String)
        genderLabel.text = (word.getGender().orElseWithId("N/A") as! String)
        
        let urlString = String(format: voiceUrl, word.getLearningLanguage(), word.getNormalizedWord())
        if let url = NSURL(string: urlString) {
            let playerItem:AVPlayerItem = AVPlayerItem(URL: url)
            player = AVPlayer(playerItem: playerItem)
        }

    }
    
    func showEmpty() {
        contentGroup.hidden = true
    }
}

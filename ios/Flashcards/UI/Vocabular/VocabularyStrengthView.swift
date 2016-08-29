//
//  VocabularyStrengthView.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 29.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class VocabularyStrengthView: UIImageView {

    func setStrength(value:Int) {
        var color:UIColor?
        switch value {
        case 1:
            image = UIImage(named: "ic_signal_cellular_1_bar_black_24dp")
            color = UIColor.flashcardsRed()
        case 2:
            image = UIImage(named: "ic_signal_cellular_2_bar_black_24dp")
            color = UIColor.flashcardsOrange()
        case 3:
            image = UIImage(named: "ic_signal_cellular_3_bar_black_24dp")
            color = UIColor.flashcardsLime()
        case 4:
            image = UIImage(named: "ic_signal_cellular_4_bar_black_24dp")
            color = UIColor.flashcardsGreen()
        default:
            image = UIImage(named: "ic_signal_cellular_1_bar_black_24dp")
            color = UIColor.flashcardsRed()
        }
        image = image?.imageWithRenderingMode(.AlwaysTemplate)
        tintColor = color
    }

}

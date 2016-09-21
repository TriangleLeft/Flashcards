//
//  DrawerViewCellTableViewCell.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 28.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class DrawerViewCell: UITableViewCell {
    
    var flagImages = [
        "de":"germany",
        "fr":"france",
        "es":"spain",
        "dn":"netherlands",
        "ru":"russia",
        "uk":"ukraine",
        "pt":"brazil",
        "vi":"viewtnam",
        "nb":"norway",
        "tr":"turkey",
        "sv":"sweden",
        "da":"denmark",
        "cy":"wales",
        "ga":"ireland",
        "it":"italy",
        "pl":"polish",
        "he":"israel",
    ]
    @IBOutlet weak var levelLabel: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var iconView: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        
        selectionStyle = .None
    }
    
    func showLanguage(item: Language) {
        iconView.image = getFlagImage(item.getId())
        titleLabel.text = item.getName()
        levelLabel.text = String(item.getLevel())
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
    private func getFlagImage(id:String) -> UIImage? {
        if let imageName:String = flagImages[id] {
            return UIImage(named: imageName)
        } else {
            return UIImage(named: "unknown")
        }
    }
    
}

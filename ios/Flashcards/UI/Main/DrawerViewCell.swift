//
//  DrawerViewCellTableViewCell.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 28.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class DrawerViewCell: UITableViewCell {

    @IBOutlet weak var levelLabel: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var iconView: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        
        selectionStyle = .None
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
    
}

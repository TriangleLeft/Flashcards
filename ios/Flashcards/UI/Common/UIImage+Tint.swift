//
//  UIImageView+Tint.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 29.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

extension UIImage {
    convenience init?(named: String, withTint: UIColor) {
        self.init(named: named)
    }
}
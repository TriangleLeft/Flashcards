//
//  MainViewControllerDelegate.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 31.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

protocol MainViewControllerDelegate: class {
    func onMenuClicked()
    
    func setMasterCollapsed(collapsed: Bool)
}

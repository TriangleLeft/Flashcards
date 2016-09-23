//
//  SettingsViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.09.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class SettingsViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationItem.leftBarButtonItem = UIBarButtonItem(title: "Done", style: .Done, target: self, action: #selector(SettingsViewController.onDoneClick))
    }
    
    func onDoneClick() {
        dismissViewControllerAnimated(true, completion: nil)
    }

}

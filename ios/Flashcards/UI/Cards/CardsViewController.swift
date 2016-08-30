//
//  CardsViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 30.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class CardsViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Add flashcards button
        let back = UIBarButtonItem(title: "Back",style: .Plain, target: self,action: #selector(CardsViewController.onBackPressed))
        //self.navigationItem.leftBarButtonItem = back
    }
    
    func onBackPressed() {
        AppDelegate.sharedAppDelegate()?.setMainViewController(true)
    }

}

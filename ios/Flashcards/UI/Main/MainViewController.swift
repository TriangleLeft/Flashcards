//
//  MainViewController.swift
//  Flashcards
//
//  Created by Aleksey Kurnosenko on 23.08.16.
//  Copyright © 2016 TriangleLeft. All rights reserved.
//

import UIKit

class MainViewController: UISplitViewController, UISplitViewControllerDelegate {

    override func viewDidLoad() {
        super.viewDidLoad()
        
//        navigationController!.navigationBarHidden = false;
//        navigationController!.navigationBar.hidden = false;
//        navigationController!.title = "Title"
       
        //showViewController(mainVC, sender: nil);
        // Do any additional setup after loading the view.
    }
    
    func splitViewController(splitViewController: UISplitViewController, collapseSecondaryViewController secondaryViewController: UIViewController, ontoPrimaryViewController primaryViewController: UIViewController) -> Bool{
        return true
    }

}

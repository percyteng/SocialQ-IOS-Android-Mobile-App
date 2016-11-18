//
//  Messages.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-12.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class Message: UIViewController  {


    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var toolBar: UIToolbar!
    @IBOutlet weak var Open: UIBarButtonItem!
    
    @IBOutlet weak var section: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        Open.target = self.revealViewController()
        Open.action = #selector(SWRevealViewController.revealToggle(_:))
        
        self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        
        let inbox = UIViewController(nibName: "inbox", bundle: nil)
        self.addChildViewController(inbox)
        self.scrollView.addSubview(inbox.view)
        inbox.didMoveToParentViewController(self)
        
        let outbox = UIViewController(nibName: "outbox", bundle: nil)
        var outboxFrame = outbox.view.frame
        outboxFrame.origin.x = self.view.frame.size.width
        outbox.view.frame = outboxFrame
        self.addChildViewController(outbox)
        self.scrollView.addSubview(outbox.view)
        outbox.didMoveToParentViewController(self)
        
        self.scrollView.contentSize = CGSizeMake(self.view.frame.size.width*2, self.view.frame.size.height-64)
    }

//
//        if scrollView.subviews == inbox{
//            section.text = "OUTBOX"
//        }
//        else{
//            section.text = "INBOX"
//        }
//        var leftSwipe = UISwipeGestureRecognizer(target: self, action: Selector("handleSwipes:"))
//        var rightSwipe = UISwipeGestureRecognizer(target: self, action: Selector("handleSwipes:"))
//        
//        
//        leftSwipe.direction = .Left
//        rightSwipe.direction = .Right
//
//        
//        scrollView.addGestureRecognizer(leftSwipe)
//        scrollView.addGestureRecognizer(rightSwipe)
//
        

//    func handleSwipes(sender:UISwipeGestureRecognizer){
//        if sender.direction == .Left{
//            scrollView.backgroundColor = UIColor(white: 1, alpha: 1)
//        }
//        if sender.direction == .Right{
//            scrollView.backgroundColor = UIColor(white: 0.5, alpha: 0.5)
//        }
//
//    }
    
}

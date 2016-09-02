//
//  showMessage.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-25.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class showMessage: UIViewController {

    @IBOutlet weak var from: UILabel!
    @IBOutlet weak var profile: UIImageView!
    var username = String()
    @IBOutlet weak var whereFrom: UILabel!
    @IBOutlet weak var subjectLabel: UILabel!
    @IBOutlet weak var content: UILabel!
    @IBOutlet weak var timeLabel: UILabel!
    var target = String()
    var refresher: UIRefreshControl!
    @IBOutlet weak var replyButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        profile.layer.borderWidth = 1
        username = tempUser.username
        profile.layer.masksToBounds = false
        profile.layer.cornerRadius = profile.frame.size.width/2
        profile.clipsToBounds = true
        if tempUser.typeMessage == "Outbox"{
            replyButton.hidden = true
        }

        self.showAnimate()
        // Do any additional setup after loading the view.
    }


    @IBAction func back(sender: AnyObject) {
        self.removeAnimate()
    }

    @IBAction func replyBtn(sender: AnyObject) {
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("compose") as! composeMessage
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)

        if tempUser.typeMessage == "Inbox"{
            
            popOverVC.toField.text = "To: \(target)"
            popOverVC.target = target
            popOverVC.username = username
            popOverVC.toField.enabled = false
            popOverVC.cameFrom = "\(username) replied to your message. :)"
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func showAnimate(){
        self.view.transform = CGAffineTransformMakeScale(1.3, 1.3)
        self.view.alpha = 0.0;
        UIView.animateWithDuration(0.25, animations:{
            self.view.alpha = 1.0
            self.view.transform = CGAffineTransformMakeScale(1.0, 1.0)
        })
    }
    func removeAnimate(){
        
        UIView.animateWithDuration(0.25, animations:{
            self.view.alpha = 0.0
            self.view.transform = CGAffineTransformMakeScale(1.3, 1.3)
            }, completion: {(finished : Bool) in
                if (finished){
                    self.view.removeFromSuperview()
                }
        })
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

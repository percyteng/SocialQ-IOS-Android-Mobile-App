//
//  menuClass.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-17.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class menuClass: UIViewController {
    @IBOutlet weak var profile: UIImageView!
    var username = String()
    @IBOutlet weak var usernameLabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        usernameLabel.text = tempUser.username
        profile.layer.borderWidth = 1
        username = tempUser.username
        if let url = NSURL(string: "http://percyteng.com/orbit/pictures/\(username).JPG") {

            if let data = NSData(contentsOfURL: url) {
                profile.image = UIImage(data: data)
                print("diabolo")

            }
        }
        self.view.layoutIfNeeded()
        profile.layer.cornerRadius = profile.frame.size.width/2
        profile.layer.masksToBounds = false

        profile.clipsToBounds = true
        // Do any additional setup after loading the view, typically from a nib.
    }
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "mainPageSeg"{
            let vc = segue.destinationViewController as! mainPage
            vc.username = username
        }
        else if segue.identifier == "profileSeg"{
            let vc = segue.destinationViewController as! Profile
            vc.username = username
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

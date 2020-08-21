//
//  showPostF.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-25.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class showPostF: UIViewController {

    @IBOutlet weak var typeImg: UIImageView!
    @IBOutlet weak var type: UILabel!
    @IBOutlet weak var comments: UILabel!
    @IBOutlet weak var notes: UILabel!
    @IBOutlet weak var price: UILabel!
    @IBOutlet weak var location: UILabel!
    @IBOutlet weak var titles: UILabel!
    @IBOutlet weak var profile: UIImageView!
    @IBOutlet weak var scrollView: UIScrollView!
    var username = String()
    var category = String()
    @IBOutlet weak var subjectImg: UIImageView!
    @IBOutlet weak var postUser: UILabel!
    var pricesStr = String()
    var notesStr = String()
    var postUserStr = String()
    var isFrom = String()
    @IBOutlet weak var commentField: KMPlaceholderTextView!
    let DoneToolbar: UIToolbar = UIToolbar()

    @IBOutlet weak var commentBtn: UIButton!
    @IBOutlet weak var composeBtn: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        profile.layer.borderWidth = 1
        postUser.text = tempPost.postUser
        self.view.layoutIfNeeded()

        profile.layer.masksToBounds = false
        profile.layer.cornerRadius = profile.frame.size.width/2
        profile.clipsToBounds = true
        username = tempUser.username
        self.showAnimate()

        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(showPostF.keyboardWillShow(_:)), name:UIKeyboardWillShowNotification, object: nil);
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(showPostF.keyboardWillHide(_:)), name:UIKeyboardWillHideNotification, object: nil);
        // Do any additional setup after loading the view.
        DoneToolbar.barStyle = UIBarStyle.Default
        DoneToolbar.items=[
            UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.FlexibleSpace, target: self, action: nil),
            UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.Bordered, target: self, action: #selector(showPostF.boopla))
        ]
        
        DoneToolbar.sizeToFit()
        
        commentField.inputAccessoryView = DoneToolbar
    }
    override func viewDidAppear(animated: Bool) {
        if isFrom == "mainPage"{
            comments.hidden = true
            commentField.hidden = true
            commentBtn.hidden = true
        }
        postUser.text = postUserStr
        if let url = NSURL(string: "http://percyteng.com/orbit/pictures/\(postUserStr).JPG") {
            if let data = NSData(contentsOfURL: url) {
                profile.image = UIImage(data: data)
            }
        }
    }
    func boopla () {
        commentField.resignFirstResponder()
    }
    @IBAction func compose(sender: AnyObject) {
        if username == "Visitor"{
            let alert = UIAlertController(title: "Cannot compose message", message:"Please register your account to send messages. :)", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
        }
        else{
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("compose") as! composeMessage
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
        let target = postUser.text!
        popOverVC.toField.text = "To: \(target)"
        popOverVC.target = target
        popOverVC.toField.enabled = false
        popOverVC.cameFrom = "\(username) messaged you for your post on \(category) board :)"
        }

    }
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
        scrollView.endEditing(true)

    }
    @IBAction func postComments(sender: AnyObject) {
        if username == "Visitor"{
            commentField.resignFirstResponder()

            let alert = UIAlertController(title: "Cannot comment", message:"Please register your account to comment. :)", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
            commentField.text = ""

        }
        else{
        if commentField.text!.isEmpty{
            let alert = UIAlertController(title: "Invalid comment", message:"You cannot enter an empty comment.", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
            
        }
        else{

        var url = String()
        if type.text == "TUTORING"{
            url = "http://www.percyteng.com/orbit/updateTutors.php"
            
        }
        else{
            url = "http://www.percyteng.com/orbit/updateEvents.php"
        }
        let realComment = "\n\(username): \(commentField.text)"
        let postString = "username=\(postUser.text!)&notes=\(notesStr)&cost=\(pricesStr)&newComment=\(realComment)"
        let request = NSMutableURLRequest(URL: NSURL(string: url)!)
        request.HTTPMethod = "POST"
        request.HTTPBody = postString.dataUsingEncoding(NSUTF8StringEncoding)
        
        let task = NSURLSession.sharedSession().dataTaskWithRequest(request) {
            data, response, error in
            
            if error != nil {
                print("error=\(error)")
                return
            }
            
            print("response = \(response)")
            
            let responseString = NSString(data: data!, encoding: NSUTF8StringEncoding)
            print("responseString = \(responseString)")
        }
        task.resume()
            var temp = comments.text!
            comments.text = "\(temp)\(realComment)"
            commentField.resignFirstResponder()
            commentField.text = ""

        }
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func keyboardWillShow(sender: NSNotification) {
        self.view.frame.origin.y = -200
    }
    
    func keyboardWillHide(sender: NSNotification) {
        self.view.frame.origin.y = 0
    }
    @IBAction func back(sender: AnyObject) {
        NSNotificationCenter.defaultCenter().postNotificationName("backF", object: nil)
        NSNotificationCenter.defaultCenter().removeObserver(self)

        self.removeAnimate()
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

//
//  showPost.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-25.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class showPostT: UIViewController,UITextViewDelegate {

    @IBOutlet weak var comments: UILabel!
    @IBOutlet weak var notes: UILabel!
    @IBOutlet weak var item: UILabel!
    @IBOutlet weak var price: UILabel!
    @IBOutlet weak var typeImg: UIImageView!
    @IBOutlet weak var profile: UIImageView!
    @IBOutlet weak var type: UILabel!
    @IBOutlet weak var itemImg: UIImageView!
    @IBOutlet weak var priceImg: UIImageView!
    @IBOutlet weak var postUser: UILabel!
    @IBOutlet weak var commentField: KMPlaceholderTextView!
    var postUserStr = String()
    var category = String()
    var username = String()
    var isFrom = String()
    var pricesStr = String()
    var notesStr = String()
    let DoneToolbar: UIToolbar = UIToolbar()
    var refreshControl: UIRefreshControl!

    @IBOutlet weak var composeBtn: UIButton!
    @IBOutlet weak var commentBtn: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        notes.sizeToFit()
        profile.layer.borderWidth = 1
        username = tempUser.username
        profile.layer.masksToBounds = false
        profile.layer.cornerRadius = profile.frame.size.width/2
        profile.clipsToBounds = true

        self.showAnimate()
        self.commentField.delegate = self
//        if commentField.isFirstResponder(){
//        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(showPostT.keyboardWillShow(_:)), name:UIKeyboardWillShowNotification, object: nil);
//        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(showPostT.keyboardWillHide(_:)), name:UIKeyboardWillHideNotification, object: nil);
//        }
        // Do any additional setup after loading the view.
        DoneToolbar.barStyle = UIBarStyle.Default
        DoneToolbar.items=[
            UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.FlexibleSpace, target: self, action: nil),
            UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.Bordered, target: self, action: #selector(showPostT.boopla))
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
    func textViewDidBeginEditing(textView: UITextView) {
        self.view.frame.origin.y -= 200
    }
    
    
    func textViewDidEndEditing(textView: UITextView) {
        self.view.frame.origin.y += 200
    }
    @IBOutlet weak var scrollView: UIScrollView!
    @IBAction func compose(sender: AnyObject) {
        if username == "Visitor"{
            let alert = UIAlertController(title: "Cannot compose messages", message:"Please register your account to send messages. :)", preferredStyle: .Alert)
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
    @IBAction func back(sender: AnyObject) {
        NSNotificationCenter.defaultCenter().postNotificationName("backT", object: nil)
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
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
        scrollView.endEditing(true)
    }
    @IBAction func postComment(sender: AnyObject) {
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
        if type.text == "SERVICE"{
            url = "http://www.percyteng.com/orbit/updateServices.php"
            
        }
        else if type.text == "SUBLET"{
            url = "http://www.percyteng.com/orbit/updateSublet.php"
        }
        else if type.text == "TUTOR"{
            url = "http://www.percyteng.com/orbit/updateTutors.php"
        }
        else if type.text == "SPORTS"{
            url = "http://www.percyteng.com/orbit/updateSports.php"
        }
        else if type.text == "EXCHANGE"{
            url = "http://www.percyteng.com/orbit/updateExchange.php"
        }
        else{
            url = "http://www.percyteng.com/orbit/updateRideshare.php"
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
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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

//
//  composeMessage.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-25.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class composeMessage: UIViewController {

    @IBOutlet weak var toField: UITextField!
    @IBOutlet weak var subjectField: UITextField!
    var cameFrom = String()
    var target = String()
    var username = String()
    let DoneToolbar: UIToolbar = UIToolbar()
    @IBOutlet weak var messageField: KMPlaceholderTextView!
    override func viewDidLoad() {
        super.viewDidLoad()
        username = tempUser.username
        self.view.backgroundColor = UIColor.blackColor().colorWithAlphaComponent(0.8)
        self.showAnimate()
        DoneToolbar.barStyle = UIBarStyle.Default
        DoneToolbar.items=[
            UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.FlexibleSpace, target: self, action: nil),
            UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.Bordered, target: self, action: #selector(showPostT.boopla))
        ]
        
        DoneToolbar.sizeToFit()
        
        subjectField.inputAccessoryView = DoneToolbar
        messageField.inputAccessoryView = DoneToolbar
    }
    func boopla () {
        subjectField.resignFirstResponder()
        messageField.resignFirstResponder()
    }


    @IBAction func send(sender: AnyObject) {

        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/sendMessage.php")!)
        request.HTTPMethod = "POST"
        let postString = "from=\(username)&to=\(target)&subject=\(subjectField.text!)&content=\(messageField.text)&time=\(getTime().hour):\(getTime().min)&cameFrom=\(cameFrom)"
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
        self.removeAnimate()
    }
    func getTime() -> (hour:Int, min:Int, sec:Int) {
        let currentDateTime = NSDate()
        let calendar = NSCalendar.currentCalendar()
        let components = calendar.components([.Hour,.Minute,.Second], fromDate: currentDateTime)
        let hour = components.hour
        let min = components.minute
        let sec = components.second
        return (hour,min,sec)
    }
    @IBAction func back(sender: AnyObject) {
        self.removeAnimate()
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func keyboardWillShow(sender: NSNotification) {
        self.view.frame.origin.y = -150
    }
    
    func keyboardWillHide(sender: NSNotification) {
        self.view.frame.origin.y = 0
    }
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
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

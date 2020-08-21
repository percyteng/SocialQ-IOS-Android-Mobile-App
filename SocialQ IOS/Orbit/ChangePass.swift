//
//  ChangePass.swift
//  Orbit
//
//  Created by Percy teng on 2016-07-10.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class ChangePass: UIViewController {

    @IBOutlet weak var oldPass: UITextField!
    var username = String()
    @IBOutlet weak var newPass: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor.blackColor().colorWithAlphaComponent(0.8)
        self.showAnimate()
        username = tempUser.username
        // Do any additional setup after loading the view.
    }
    func changePass(){
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/changePass.php")!)
        request.HTTPMethod = "POST"
        let postString = "username=\(username)&newPass=\(newPass.text!)"
        
        request.HTTPBody = postString.dataUsingEncoding(NSUTF8StringEncoding)
        let task = NSURLSession.sharedSession().dataTaskWithRequest(request) {
            data, response, error in
            if error != nil {
                
                print("error=\(error)")
                return
            }
            print("response = \(response)")
            let responseString = try! NSJSONSerialization.JSONObjectWithData(data!, options: .MutableLeaves) as? NSDictionary
            print("responseString = \(responseString)")
            let result:NSNumber = (responseString!["success"] as? NSNumber)!
            
            
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                if result == 1{
                    self.removeAnimate()
                }
                else {
                    let alert = UIAlertController(title: "Error!", message:"Something went wrong!", preferredStyle: .Alert)
                    alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
                    self.presentViewController(alert, animated: true){}
                }
            }
        }
        task.resume()
        

    }
    @IBAction func confirm(sender: AnyObject) {
        
        if newPass.text?.characters.count > 20{
            let alert = UIAlertController(title: "Invalid Password", message:"Password cannot be more than 20 characters", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
            
        }
        else{
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/verifyPassword.php")!)
        request.HTTPMethod = "POST"
        let postString = "username=\(username)&password=\(oldPass.text!)"
        
        request.HTTPBody = postString.dataUsingEncoding(NSUTF8StringEncoding)
        let task = NSURLSession.sharedSession().dataTaskWithRequest(request) {
            data, response, error in
            if error != nil {
                
                print("error=\(error)")
                return
            }
            print("response = \(response)")
            let responseString = try! NSJSONSerialization.JSONObjectWithData(data!, options: .MutableLeaves) as? NSDictionary
            //            let responseString = NSString(data: data!, encoding: NSUTF8StringEncoding)
            //            let array = try! NSJSONSerialization.JSONObjectWithData(data!, options: NSJSONReadingOptions.MutableContainers) as! NSArray
            let result:NSNumber = (responseString!["success"] as? NSNumber)!
            
            
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                if result == 1{
                    self.changePass()
                }
                else {
                    let alert = UIAlertController(title: "Wrong Information!", message:"The old password you entered is incorrect!", preferredStyle: .Alert)
                    alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
                    self.presentViewController(alert, animated: true){}
                }
            }
        }
        task.resume()
        }

    }
    @IBAction func back(sender: AnyObject) {
        self.removeAnimate()
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

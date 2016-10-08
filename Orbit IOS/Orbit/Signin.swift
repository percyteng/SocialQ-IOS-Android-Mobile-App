//
//  ViewController.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-08.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class Signin: UIViewController {
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    @IBAction func loginButton(sender: AnyObject) {
        self.email.resignFirstResponder()
        self.password.resignFirstResponder()
        get{(value) in
        }
    }
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    func get(completion:(value: NSDictionary) -> Void){
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/login.php")!)
        request.HTTPMethod = "POST"
        let postString = "useremail=\(email.text!)&password=\(password.text!)"

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
                print("we in there")

                if result == 1{
                    let username: NSString = (responseString!["user"] as? NSString)!

                    self.getDes(username as String)
                    
                    tempUser.username = username as String
                    let next = self.storyboard?.instantiateViewControllerWithIdentifier("reveal") as! SWRevealViewController
                    next.loadView()


                    self.presentViewController(next, animated: true, completion: nil)
                    
                }
                else {
                    let alert = UIAlertController(title: "Wrong Information!", message:"Please enter the correct username and password", preferredStyle: .Alert)
                    alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
                    self.presentViewController(alert, animated: true){}
                }
            }
            completion(value: responseString!)
        }
        task.resume()

    }
    func getDes(username:String){
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/getDescription.php")!)
        request.HTTPMethod = "POST"
        let postString = "username=\(username)"
        request.HTTPBody = postString.dataUsingEncoding(NSUTF8StringEncoding)
        let task = NSURLSession.sharedSession().dataTaskWithRequest(request) {
            data, response, error in
            if error != nil {
                
                print("error=\(error)")
                return
            }
            
            print("response = \(response)")
            let responseString = try! NSJSONSerialization.JSONObjectWithData(data!, options: .MutableLeaves) as? NSDictionary
            var description:NSString? = responseString!["user"] as? NSString
            if description == nil{
                description = ""
            }
            //            let array = try! NSJSONSerialization.JSONObjectWithData(data!, options: NSJSONReadingOptions.MutableContainers) as! NSArray
            
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                tempUser.descript = description as! String
            }
        }
        task.resume()
    }
//    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject!) {
////        if (segue.identifier == "login") {
////            let revealViewCOntroller = segue.destinationViewController as! SWRevealViewController
////            revealViewCOntroller.loadView()
//        var secondVc:mainPage = segue.destinationViewController as! mainPage
////            let secondVc = revealViewCOntroller.frontViewController as! mainPage
////            let menu = revealViewCOntroller.rearViewController as! menuClass
////            menu.loadView()
////            secondVc.loadView()
//            secondVc.username = "Wrewerewrq"
////        }
//    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}


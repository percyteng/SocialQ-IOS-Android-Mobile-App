//
//  Profile.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-12.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit
class Profile: UIViewController, UITableViewDelegate, UITableViewDataSource  {
    var username = String()
    var values:NSArray = []
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var bio: UITextField!
    @IBOutlet weak var name: UILabel!
    @IBOutlet weak var profile: UIImageView!
    @IBOutlet weak var Open: UIBarButtonItem!
    @IBOutlet weak var toolBar: UIToolbar!
    var data: NSData?

    override func viewDidLoad() {
        
        super.viewDidLoad()

        name.text = tempUser.username
        username = tempUser.username

        bio.text = tempUser.descript
        self.view.layoutIfNeeded()

        profile.layer.borderWidth = 1
        profile.layer.masksToBounds = false
        profile.layer.cornerRadius = profile.frame.size.width/2
        profile.clipsToBounds = true
        get{(value) in
            self.values = value
        }
        Open.target = self.revealViewController()
        Open.action = #selector(SWRevealViewController.revealToggle(_:))
        print(username)
        let newString = username.stringByReplacingOccurrencesOfString(" ", withString: "%20")
        print(newString)
        let url = NSURL(string: "http://percyteng.com/orbit/pictures/\(newString).JPG")
        let data = NSData(contentsOfURL:url!)

        if data != nil {
            profile.image = UIImage(data:data!)
        } else {
            // In this when data is nil or empty then we can assign a placeholder image
            profile.image = UIImage(named: "ic_account_circle_white_3x.png")
        }
        
        self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
    }
    
    @IBAction func updateDes(sender: AnyObject) {
        if username == "Visitor"{
            bio.text = ""
            let alert = UIAlertController(title: "Can't update bio", message:"Please register an account to have your own profile. :)", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
        }
        else{
        if bio.text?.characters.count > 100{
            let alert = UIAlertController(title: "Invalid Bio", message:"Bio cannot be more than 100 characters", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
        }
        else{
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/updateDescription.php")!)
        request.HTTPMethod = "POST"
        let postString = "username=\(username)&userintro=\(bio.text!)"
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
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                tempUser.descript = self.bio.text!
            }
        }
        task.resume()
            let alert = UIAlertController(title: "Sweet!", message:"You have updated your bio! :)", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}

        }
        }
    }
    @IBAction func changePass(sender: AnyObject) {
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("changePass") as! ChangePass
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
    }
    func get(completion:(_: NSArray) -> Void){
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/getAllposts.php")!)
        request.HTTPMethod = "POST"
        let postString = "user=ios"
        request.HTTPBody = postString.dataUsingEncoding(NSUTF8StringEncoding)
        let task = NSURLSession.sharedSession().dataTaskWithRequest(request) {
            data, response, error in
            if error != nil {
                
                print("error=\(error)")
                return
            }
            
            print("response = \(response)")
            let responseString = try! NSJSONSerialization.JSONObjectWithData(data!, options: .MutableLeaves) as? NSDictionary
            if responseString!["success"] as! Int == 0{
                return
            }
            let array:NSArray = responseString!["all"] as! NSArray
            var temp = NSMutableArray()
            //            let array = try! NSJSONSerialization.JSONObjectWithData(data!, options: NSJSONReadingOptions.MutableContainers) as! NSArray
            
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                for ele in array{
                    if ele["username"] as! String == self.username{
                        temp.addObject(ele)
                    }
                }
                self.values = temp as NSArray
                self.tableView?.reloadData();
            }
            completion(array)
        }
        task.resume()
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return values.count
    }
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("cell", forIndexPath: indexPath) as!postCell
        let maindata = values[values.count-1-indexPath.row]
        if maindata["category"] as? String == "Services"{
            cell.postImg.image = UIImage(named: "tile_services")
        }
        else if maindata["category"] as? String == "exchange"{
            cell.postImg.image = UIImage(named: "tile_exchange")
        }
        else if maindata["category"] as? String == "Tutors"{
            cell.postImg.image = UIImage(named: "tile_tutoring")
        }
        else if maindata["category"] as? String == "Sports"{
            cell.postImg.image = UIImage(named: "tile_sports")
        }
        else if maindata["category"] as? String == "Sublet"{
            cell.postImg.image = UIImage(named: "tile_sublet")
        }
        else if maindata["category"] as? String == "Events"{
            cell.postImg.image = UIImage(named: "tile_events")
        }
        else{
            cell.postImg.image = UIImage(named: "tile_carpool")
        }
        
        
        if maindata["category"] as? String == "Services" || maindata["category"] as? String == "Tutors" || maindata["category"] as? String == "Events"{
            cell.title.text = maindata["title"] as? String
        }
        else if maindata["category"] as? String == "Sublet" || maindata["category"] as? String == "Rideshare" || maindata["category"] as? String == "Sports"{
            cell.title.text = maindata["location"] as? String
        }
        else{
            cell.title.text = maindata["item"] as? String
        }
        
        
        if maindata["category"] as? String == "Sublet" || maindata["category"] as? String == "Rideshare"{
            cell.location.text = ""
        }
        else if maindata["category"] as? String == "Sports"{
            cell.location.text = maindata["type"] as? String
        }
        else{
            cell.location.text = maindata["location"] as? String
        }
        cell.category.text = maindata["category"] as? String
        cell.price.text = maindata["price"] as? String
        
        if maindata["category"] as? String == "Sports"{
            cell.price.text = ""
            cell.location.text = maindata["price"] as? String
        }

            cell.categories = maindata["category"] as! String
            cell.username = maindata["username"] as! String
            if maindata["category"] as! String == "Services"{
                cell.locations = "do whatever shit i want"
            }
            else if maindata["category"] as! String == "exchange"{
                cell.locations = maindata["item"] as! String
            }
            else{
                cell.locations = maindata["location"] as! String
            }
            cell.prices = maindata["price"] as! String
            cell.notes = maindata["notes"] as! String
            cell.school = maindata["school"] as! String
        
        return cell
    }
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let maindata = values[values.count-1-indexPath.row]
        let category = maindata["category"] as? String;
        let price = maindata["price"] as! String;
        let notes = maindata["notes"] as! String;
        let comments = maindata["comments"] as! String;
        print("buyap\(comments)")
        if category == "Services" || category == "exchange" || category == "Rideshare" || category == "Sublet" || category == "Sports"{
            let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("showPostT") as! showPostT
            self.addChildViewController(popOverVC)
            popOverVC.view.frame = self.view.frame
            self.view.addSubview(popOverVC.view)
            popOverVC.didMoveToParentViewController(self)
            popOverVC.price.text = "Price: \(price)"
            popOverVC.postUserStr = maindata["username"] as! String
            popOverVC.notes.text = "Notes: \(notes)"
            popOverVC.comments.text = comments
            if category == "Services"{
                let title = maindata["title"] as! String;
                popOverVC.item.text = "service: \(title)"
                popOverVC.type.text = "SERVICE"
                popOverVC.typeImg.image = UIImage(named: "tile_services")
            }
            else if category == "Rideshare"{
                let location = maindata["location"] as! String;
                popOverVC.item.text = "Location: \(location)"
                popOverVC.type.text = "RIDESHARE"
                popOverVC.typeImg.image = UIImage(named: "tile_carpool")
            }
            else if category == "Sublet"{
                let location = maindata["location"] as! String;
                popOverVC.item.text = "Location: \(location)"
                popOverVC.type.text = "SUBLET"
                popOverVC.typeImg.image = UIImage(named: "tile_sublet")
            }
        }
        else{
            let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("showPostF") as! showPostF
            self.addChildViewController(popOverVC)
            popOverVC.view.frame = self.view.frame
            self.view.addSubview(popOverVC.view)
            popOverVC.didMoveToParentViewController(self)
            popOverVC.price.text = "Price: \(price)"
            popOverVC.postUserStr = maindata["username"] as! String
            popOverVC.notes.text = "Notes: \(notes)"
            popOverVC.comments.text = comments
            if category == "Sports"{
                let type = maindata["category"] as! String;
                let location = maindata["location"] as! String;
                popOverVC.titles.text = "Type: \(type)"
                popOverVC.location.text = "Location: \(location)"
                popOverVC.type.text = "SPORTS"
                popOverVC.typeImg.image = UIImage(named: "tile_sports")
            }
            else if category == "Tutors"{
                let subject = maindata["title"] as! String;
                let location = maindata["location"] as! String;
                popOverVC.titles.text = "Type: \(subject)"
                popOverVC.location.text = "Location: \(location)"
                popOverVC.type.text = "TUTORING"
                popOverVC.typeImg.image = UIImage(named: "tile_tutoring")
            }
            else if category == "Events"{
                let title = maindata["title"] as! String;
                let location = maindata["location"] as! String;
                popOverVC.titles.text = "Name: \(title)"
                popOverVC.location.text = "Location: \(location)"
                popOverVC.type.text = "EVENT"
                popOverVC.typeImg.image = UIImage(named: "tile_events")
            }
        }

    }
    

}

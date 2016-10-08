//
//  mainPage.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-12.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class mainPage: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var testLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var toolBar: UIToolbar!
    var values:NSArray = []
    var username = String()
    @IBOutlet weak var Open: UIBarButtonItem!
    var continued:Bool = true
    var commentArray:[String] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        username = tempUser.username
        Open.target = self.revealViewController()
        Open.action = #selector(SWRevealViewController.revealToggle(_:))
        self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())

        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(self.methodhandlingTheNotificationEvent), name:"NotificationIdentifie‌​r", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(mainPage.refreshVC), name:"refresh", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(mainPage.refreshVCyoo), name:"refreshyoo", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(mainPage.backFromBoard), name:"back", object: nil)
      
    }
     override func viewWillAppear(animated: Bool) {
        get()
    }
    func backFromBoard(){
        get()
    }
    func refreshVCyoo(notification:NSNotification){
        let userInfo:Dictionary<String,String!> = notification.userInfo as! Dictionary<String,String!>
        let first = userInfo["first"]
        let second = userInfo["second"]
        let third = userInfo["third"]
        let fourth = userInfo["fourth"]
        let school = userInfo["school"]
        let copy: [String: String] = ["location": first!, "price": second!, "notes": third!, "subject": fourth!, "school": school!, "username": username]
        var temp = NSMutableArray()
        temp.addObject(copy)
        for ele in values{
            temp.addObject(ele)
        }
        values = temp as NSArray
        tableView.reloadData()
        get()
        
    }

    func refreshVC(notification:NSNotification){
        let userInfo:Dictionary<String,String!> = notification.userInfo as! Dictionary<String,String!>
        let first = userInfo["first"]
        let second = userInfo["second"]
        let third = userInfo["third"]
        let school = userInfo["school"]
        let copy: [String: String] = ["title": first!, "price": second!, "notes": third!, "school": school!, "username": username]
        var temp = NSMutableArray()
        temp.addObject(copy)
        for ele in values{
            temp.addObject(ele)
        }
        values = temp as NSArray
        tableView.reloadData()
        get()
        //        self.presentingViewController!.dismissViewControllerAnimated(true, completion: {});
    }


    func methodhandlingTheNotificationEvent(notification:NSNotification){
        let userInfo:Dictionary<String,String!> = notification.userInfo as! Dictionary<String,String!>
        let price = userInfo["price"]
        let notes = userInfo["notes"]
        let school = userInfo["school"]
        
        var temp = NSMutableArray()
        for ele in values{
            if ele["price"] as! String != price! || ele["notes"] as! String != notes! || ele["school"] as! String != school!{
                temp.addObject(ele)
            }
        }
        values = temp as NSArray
        tableView.reloadData()
        
        get()

        tableView.reloadData()
    }
//    @IBAction func serviceAction(sender: AnyObject) {
//        let next = self.storyboard?.instantiateViewControllerWithIdentifier("service") as! serviceBoard
//        next.username = username
//        self.presentViewController(next, animated: true, completion: nil)
//    }
//    @IBAction func exchangeAction(sender: AnyObject) {
//        let next = self.storyboard?.instantiateViewControllerWithIdentifier("exchange") as! exchangeBoard
//        next.username = username
//        print ("hola \(username)")
//        self.presentViewController(next, animated: true, completion: nil)
//
//    }
    @IBAction func serviceBoar(sender: AnyObject) {
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("service") as! serviceBoard
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
    }
    @IBAction func exchangeBoar(sender: AnyObject) {
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("exchange") as! exchangeBoard
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
    }
    @IBAction func tutorBoar(sender: AnyObject) {
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("tutor") as! tutorBoard
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
    }
    @IBAction func eventBoar(sender: AnyObject) {
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("event") as! eventBoard
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
    }
    @IBAction func subletBoar(sender: AnyObject) {
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("sublet") as! subletBoard
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
    }
    @IBAction func sportBoar(sender: AnyObject) {
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("sport") as! sportBoard
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
    }
    @IBAction func carpoolBoar(sender: AnyObject) {
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("rideshare") as! carpoolBoard
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
    }
    func get(){
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
//            let array = try! NSJSONSerialization.JSONObjectWithData(data!, options: NSJSONReadingOptions.MutableContainers) as! NSArray
            
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                self.values = array
                self.tableView?.reloadData();

            }
        }
        task.resume()
        

//        let url = NSURL(string: "http://percyteng.com/orbit/getAllpostsTest.php")
//        let data = NSData(contentsOfURL: url!)
//
//    values = try! NSJSONSerialization.JSONObjectWithData(data!, options: NSJSONReadingOptions.MutableContainers) as! NSArray
    }
    func getComments(notes: String, price: String, category: String, user: String, index:Int){
        
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/getComments.php")!)
        request.HTTPMethod = "POST"
        let postString = "username=\(user)&cost=\(price)&category=\(category)&notes=\(notes)"
        request.HTTPBody = postString.dataUsingEncoding(NSUTF8StringEncoding)
        let task = NSURLSession.sharedSession().dataTaskWithRequest(request) {
            data, response, error in
            if error != nil {
                
                print("error=\(error)")
                return
            }
            
            print("response = \(response)")
            let responseString = try! NSJSONSerialization.JSONObjectWithData(data!, options: .MutableLeaves) as? NSDictionary
            let comment:NSString = responseString!["result"] as! NSString
            print("result is \(comment)&\(index)")
            //            let array = try! NSJSONSerialization.JSONObjectWithData(data!, options: NSJSONReadingOptions.MutableContainers) as! NSArray
            
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                self.commentArray.append(comment as String)
            }
        }
        task.resume()
    }

//        override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
//            if segue.identifier == "servicePoint"{
//                let vc = segue.destinationViewController as! serviceBoard
//                print("holla\(username)")
//                vc.username = username
//            }
//            else if segue.identifier == "exchangePoint"{
//                let vcs = segue.destinationViewController as! exchangeBoard
//                vcs.username = username
//                print ("exchange is \(vcs.username)")
//            }
//            else if segue.identifier == "tutorPoint"{
//                let vc = segue.destinationViewController as! tutorBoard
//                vc.username = username
//            }
//            else if segue.identifier == "eventPoint"{
//                let vc = segue.destinationViewController as! eventBoard
//                vc.username = username
//            }
//            else if segue.identifier == "subletPoint"{
//                let vc = segue.destinationViewController as! subletBoard
//                vc.username = username
//            }
//            else if segue.identifier == "sportsPoint"{
//                let vc = segue.destinationViewController as! sportBoard
//                vc.username = username
//            }
//            else if segue.identifier == "carPoint"{
//                let vc = segue.destinationViewController as! carpoolBoard
//                vc.username = username
//            }
//        }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if values.count > 20{
            return 20
        }
        else{
            return values.count
        }
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
        if maindata["username"] as! String != username && username != "admin"{
            cell.deleteBtn.hidden = true
        }
        else{
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
        }

        return cell
    }
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {

        let maindata = values[values.count-1-indexPath.row]
        let category = maindata["category"] as! String;
        let price = maindata["price"] as! String;
        let user = maindata["username"] as! String
        let notes = maindata["notes"] as! String;
        if category == "Services" || category == "exchange" || category == "Rideshare" || category == "Sublet" || category == "Sports"{
            let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("showPostT") as! showPostT
            self.addChildViewController(popOverVC)
            popOverVC.view.frame = self.view.frame
            self.view.addSubview(popOverVC.view)
            popOverVC.didMoveToParentViewController(self)
            popOverVC.price.text = "Price: \(price)"
            popOverVC.notes.text = "Notes: \(notes)"
            popOverVC.postUserStr = user
            popOverVC.comments.text = "Comments: "
            popOverVC.isFrom = "mainPage"
//            popOverVC.comments.text = commentArray[indexPath.row]
            popOverVC.notesStr = notes
            popOverVC.category = category
            popOverVC.pricesStr = price
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
            if category == "Sports"{
                let type = maindata["price"] as! String;
                let location = maindata["location"] as! String;
                popOverVC.item.text = "Type: \(type)"
                popOverVC.price.text = "Location: \(location)"
                popOverVC.type.text = "SPORTS"
                popOverVC.typeImg.image = UIImage(named: "tile_sports")
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
            popOverVC.notes.text = "Notes: \(notes)"
            popOverVC.postUserStr = user
            popOverVC.comments.text = "Comments: "
            popOverVC.isFrom = "mainPage"

//            popOverVC.comments.text = commentArray[indexPath.row]
            popOverVC.notesStr = notes
            popOverVC.category = category
            popOverVC.pricesStr = price
            if category == "Tutors"{
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
    override func viewWillDisappear(animated: Bool) {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }

}

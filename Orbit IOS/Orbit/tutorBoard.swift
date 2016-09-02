//
//  ServiceBoardViewController.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-22.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class tutorBoard: UIViewController, UITableViewDelegate, UITableViewDataSource, UISearchResultsUpdating,UIPopoverPresentationControllerDelegate {
    var values:NSArray = []
    var school = "QUEENS"
    var filteredTableData = [String]()
    //    var filteredPrice = [Any]()
    var username = String()
    var resultSearchController = UISearchController()
    @IBOutlet weak var tableView: UITableView!

    override func viewDidLoad() {
        super.viewDidLoad()
        username = tempUser.username
        self.resultSearchController = ({
            let controller = UISearchController(searchResultsController: nil)
            controller.searchResultsUpdater = self
            controller.dimsBackgroundDuringPresentation = false
            controller.searchBar.sizeToFit()
            
            self.tableView.tableHeaderView = controller.searchBar
            
            return controller
        })()
        get()
        self.showAnimate()
        self.tableView.reloadData()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(tutorBoard.refreshVCyoo), name:"refreshyoo", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(tutorBoard.methodhandlingTheNotificationEvent), name:"NotificationIdentifie‌​r", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(serviceBoard.backFrom), name:"backF", object: nil)
        
        
    }
    func backFrom(){
        get()
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
    @IBAction func back(sender: AnyObject) {
        NSNotificationCenter.defaultCenter().postNotificationName("back", object: nil)

        self.removeAnimate()
//        if (!self.resultSearchController.active && resultSearchController.searchBar.text == "") {
//            let next = self.storyboard?.instantiateViewControllerWithIdentifier("reveal") as! SWRevealViewController
//
//            self.presentViewController(next, animated: true, completion: nil)
//        }

    }

    func get(){
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/getTutors.php")!)
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
            let array:NSArray = responseString!["tutors"] as! NSArray
            var temp = NSMutableArray()
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                for ele in array{
                    if ele["school"] as! String == self.school{
                        temp.addObject(ele)
                    }
                }
                
                self.values = temp as NSArray
                self.tableView?.reloadData();            }
        }
        task.resume()
        
        
    }

    @IBAction func createPost(sender: AnyObject) {
        if username == "Visitor"{
            let alert = UIAlertController(title: "Can't create posts", message:"Please register an account to create posts. :)", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
            
        }
        else{
            
            

        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("tutorBoard") as! createTutor
        popOverVC.school = school
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
        }

    }
//    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
//        if segue.identifier == "createPost"{
//            let vc = segue.destinationViewController as! createTutor
//            let controller = vc.popoverPresentationController
//            
//            if controller != nil{
//                controller?.delegate = self
//            }
//        }
//    }
//    func adaptivePresentationStyleForPresentationController(controller: UIPresentationController) -> UIModalPresentationStyle {
//        return .None
//    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (self.resultSearchController.active && resultSearchController.searchBar.text != "") {
            return self.filteredTableData.count
        }
        else {
            return values.count
        }
    }
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("cell", forIndexPath: indexPath) as!postCell
        let maindata = values[values.count-1-indexPath.row]
        print (maindata["username"] as! String)
        if (self.resultSearchController.active && resultSearchController.searchBar.text != "") {
            //            if (filteredTableData[indexPath.row].rangeOfString("###") == nil){
            cell.postImg.image = UIImage(named:"tile_tutoring")
            cell.title.text = filteredTableData[indexPath.row]
            cell.category.text = "TUTORING"
            var location = String()
            var price = String()
            for i in values{
                if (i["subject"] as? String)! == filteredTableData[indexPath.row]{
                    price = (i["price"] as? String)!
                    location = (i["location"] as? String)!
                    cell.categories = "Tutors"
                    cell.username = i["username"] as! String
                    cell.locations = i["location"] as! String
                    cell.prices = i["price"] as! String
                    cell.notes = i["notes"] as! String
                    cell.school = i["school"] as! String

                }
            }
            cell.price.text = price
            cell.location.text = location
            return cell
        }
        else {
            if maindata["username"] as! String != username && username != "admin"{
                print("check")
                cell.deleteBtn.hidden = true
            }
            else{
                cell.categories = "Tutors"
                cell.username = maindata["username"] as! String
                cell.locations = maindata["location"] as! String
                cell.prices = maindata["price"] as! String
                cell.notes = maindata["notes"] as! String
                cell.school = maindata["school"] as! String
            }

            cell.postImg.image = UIImage(named:"tile_tutoring")
            cell.title.text = maindata["subject"] as? String
            cell.category.text = "TUTORING"
            cell.price.text = maindata["price"] as? String
            cell.location.text = maindata["location"] as? String
            return cell
        }
    }
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("showPostF") as! showPostF
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
        if (self.resultSearchController.active && resultSearchController.searchBar.text != "") {
            for i in values{
                if (i["subject"] as? String)! == filteredTableData[indexPath.row]{
                    popOverVC.type.text = "TUTORING"
                    popOverVC.typeImg.image = UIImage(named:"tile_tutoring")
                    popOverVC.titles.text = "Subject: \(i["subject"] as! String)"
                    popOverVC.location.text = "Location: \(i["location"] as! String)"
                    popOverVC.price.text = "Price: \(i["price"] as! String)"
                    popOverVC.notes.text = "Notes: \(i["notes"] as! String)"
                    popOverVC.comments.text = i["comments"] as? String
                    popOverVC.postUserStr = i["username"] as! String
                    popOverVC.notesStr = i["notes"] as! String
                    popOverVC.category = "tutor"
                    popOverVC.pricesStr = i["price"] as! String
                    if username == popOverVC.postUser.text!{
                        popOverVC.composeBtn.hidden = true
                    }
                }
            }
        }
        else{
            let maindata = values[values.count-1-indexPath.row]
            popOverVC.type.text = "TUTORING"
            popOverVC.typeImg.image = UIImage(named:"tile_tutoring")
            popOverVC.titles.text = "Subject: \(maindata["subject"] as! String)"
            popOverVC.location.text = "Location: \(maindata["location"] as! String)"
            popOverVC.price.text = "Price: \(maindata["price"] as! String)"
            popOverVC.notes.text = "Notes: \(maindata["notes"] as! String)"
            popOverVC.comments.text = maindata["comments"] as? String
            popOverVC.postUserStr = maindata["username"] as! String
            popOverVC.notesStr = maindata["notes"] as! String
            popOverVC.category = "tutor"
            popOverVC.pricesStr = maindata["price"] as! String
            if username == popOverVC.postUser.text!{
                popOverVC.composeBtn.hidden = true
            }
        }
    }
    func updateSearchResultsForSearchController(searchController: UISearchController)
    {
        var titles = [String]()
        filteredTableData.removeAll(keepCapacity: false)
        let searchPredicate = NSPredicate(format: "SELF CONTAINS[c] %@", searchController.searchBar.text!)
        var i = 0
        let count = values.count
        while i < count{
            titles.append(values[i]["subject"] as! String)
            i = i + 1
        }

        let array = (titles as NSArray).filteredArrayUsingPredicate(searchPredicate)
        //        let priceArray = (price as NSArray).filteredArrayUsingPredicate(searchPredicate)
        //        let numCount = priceArray.count
        //        array.append(String("###") + String(numCount))
        filteredTableData = array as! [String]
        self.tableView.reloadData()
    }

    
}

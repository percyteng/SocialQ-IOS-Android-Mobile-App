//
//  ServiceBoardViewController.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-22.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class subletBoard: UIViewController, UITableViewDelegate, UITableViewDataSource, UISearchResultsUpdating,UIPopoverPresentationControllerDelegate {
    var values:NSArray = []
    var filteredTableData = [String]()
    var username = String()
    var school = "QUEENS"
    //    var filteredPrice = [Any]()
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
            get()
            return controller
        })()
        self.showAnimate()
        self.tableView.reloadData()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(subletBoard.refreshVC), name:"refresh", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(subletBoard.methodhandlingTheNotificationEvent), name:"NotificationIdentifie‌​r", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(subletBoard.backFrom), name:"backT", object: nil)
        
        
    }
    func backFrom(){
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

    func refreshVC(notification:NSNotification){
        let userInfo:Dictionary<String,String!> = notification.userInfo as! Dictionary<String,String!>
        let first = userInfo["first"]
        let second = userInfo["second"]
        let third = userInfo["third"]
        let school = userInfo["school"]
        let copy: [String: String] = ["location": first!, "price": second!, "notes": third!, "school": school!, "username": username]
        var temp = NSMutableArray()
        temp.addObject(copy)
        for ele in values{
            temp.addObject(ele)
        }
        values = temp as NSArray
        tableView.reloadData()
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
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/getSublet.php")!)
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
            let array:NSArray = responseString!["subletting"] as! NSArray
            var temp = NSMutableArray()
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                for ele in array{
                    if ele["school"] as! String == self.school{
                        temp.addObject(ele)
                    }
                }
                
                self.values = temp as NSArray
                self.tableView?.reloadData();
            }
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
            
            

        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("subletBoard") as! createSublet
        popOverVC.school = school
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
        }
    }


    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
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
        if (self.resultSearchController.active && resultSearchController.searchBar.text != "") {
            //            if (filteredTableData[indexPath.row].rangeOfString("###") == nil){
            cell.postImg.image = UIImage(named:"tile_sublet")
            cell.title.text = filteredTableData[indexPath.row]
            cell.category.text = "SUBLET"
            var price = String()
            for i in values{
                if (i["location"] as? String)! == filteredTableData[indexPath.row]{
                    price = (i["price"] as? String)!
                    cell.categories = "Sublet"
                    cell.username = i["username"] as! String
                    cell.locations = i["location"] as! String
                    cell.prices = i["price"] as! String
                    cell.notes = i["notes"] as! String
                    cell.school = i["school"] as! String
                }
            }
            cell.price.text = price
            return cell
        }
        else {
            if maindata["username"] as! String != username && username != "admin"{
                cell.deleteBtn.hidden = true
            }
            else{
                cell.categories = "Sublet"
                cell.username = maindata["username"] as! String
                cell.locations = maindata["location"] as! String
                cell.prices = maindata["price"] as! String
                cell.notes = maindata["notes"] as! String
                cell.school = maindata["school"] as! String
            }
            cell.postImg.image = UIImage(named:"tile_sublet")
            cell.title.text = maindata["location"] as? String
            cell.category.text = "SUBLET"
            cell.price.text = maindata["price"] as? String
            
            return cell
        }
    }
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let popOverVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewControllerWithIdentifier("showPostT") as! showPostT
        self.addChildViewController(popOverVC)
        popOverVC.view.frame = self.view.frame
        self.view.addSubview(popOverVC.view)
        popOverVC.didMoveToParentViewController(self)
        if (self.resultSearchController.active && resultSearchController.searchBar.text != "") {
            for i in values{
                if (i["location"] as? String)! == filteredTableData[indexPath.row]{
                    popOverVC.type.text = "SUBLET"
                    popOverVC.typeImg.image = UIImage(named:"tile_sublet")
                    popOverVC.item.text = "Location: \(i["location"] as! String)"
                    popOverVC.price.text = "Price: \(i["price"] as! String)"
                    popOverVC.notes.text = "Notes: \(i["notes"] as! String)"
                    popOverVC.comments.text = i["comments"] as? String
                    popOverVC.itemImg.image = UIImage(named: "ic_location")
                    popOverVC.postUserStr = i["username"] as! String
                    popOverVC.notesStr = i["notes"] as! String
                    popOverVC.category = "sublet"
                    popOverVC.pricesStr = i["price"] as! String
                    
                    if username == popOverVC.postUser.text!{
                        popOverVC.composeBtn.hidden = true
                    }
                }
            }
        }
        else{
            let maindata = values[values.count-1-indexPath.row]
            popOverVC.type.text = "SUBLET"
            popOverVC.typeImg.image = UIImage(named:"tile_sublet")
            popOverVC.item.text = "Location: \(maindata["location"] as! String)"
            popOverVC.price.text = "Price: \(maindata["price"] as! String)"
            popOverVC.notes.text = "Notes: \(maindata["notes"] as! String)"
            popOverVC.comments.text = maindata["comments"] as? String
            popOverVC.itemImg.image = UIImage(named: "ic_location")
            popOverVC.postUserStr = maindata["username"] as! String
            popOverVC.notesStr = maindata["notes"] as! String
            popOverVC.category = "sublet"
            popOverVC.pricesStr = maindata["price"] as! String
            if username == popOverVC.postUser.text!{
                popOverVC.composeBtn.hidden = true
            }
        }
    }
    func updateSearchResultsForSearchController(searchController: UISearchController)
    {
        filteredTableData.removeAll(keepCapacity: false)
        var titles = [String]()

        let searchPredicate = NSPredicate(format: "SELF CONTAINS[c] %@", searchController.searchBar.text!)
        var i = 0
        let count = values.count
        while i < count{
            titles.append(values[i]["location"] as! String)
            i = i + 1
        }
        
        let array = (titles as NSArray).filteredArrayUsingPredicate(searchPredicate)
        //        let priceArray = (price as NSArray).filteredArrayUsingPredicate(searchPredicate)
        //        let numCount = priceArray.count
        //        array.append(String("###") + String(numCount))
        filteredTableData = array as! [String]
        self.tableView.reloadData()
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

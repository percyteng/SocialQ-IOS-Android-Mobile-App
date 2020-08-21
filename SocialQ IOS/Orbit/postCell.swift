//
//  postCell.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-16.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class postCell: UITableViewCell{
    
    @IBOutlet weak var deleteBtn: UIButton!
    @IBOutlet weak var postImg: UIImageView!
    @IBOutlet weak var category: UILabel!
    @IBOutlet weak var location: UILabel!
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var price: UILabel!
    var prices = String()
    var notes = String()
    var comments = String()
    var locations = String()
    var categories = String()
    var school = String()
    var username = String()
    var date = String()
    
    
    @IBAction func deleteAction(sender: AnyObject) {
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/deletePost.php")!)
        request.HTTPMethod = "POST"
        let postString = "name=\(username)&location=\(locations)&price=\(prices)&notes=\(notes)&school=\(school)&category=\(categories)"
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
        NSNotificationCenter.defaultCenter().postNotificationName("NotificationIdentifie‌​r", object: nil, userInfo:[
            "price":prices,
            "notes":notes,
            "school":school])
        NSNotificationCenter.defaultCenter().removeObserver(self)

    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated:animated)
    }
    
}
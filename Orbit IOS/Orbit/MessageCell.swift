//
//  MessageCell.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-21.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class MessageCell: UITableViewCell {



    @IBOutlet weak var messageImage: UIImageView!
    @IBOutlet weak var targetTXT: UILabel!
    @IBOutlet weak var titleTXT: UILabel!
    @IBOutlet weak var timeStamp: UILabel!
    var from = String()
    var to = String()
    var subject = String()
    var content = String()
    override func awakeFromNib() {
        super.awakeFromNib()
        self.layoutIfNeeded()

        messageImage.layer.borderWidth = 1

        messageImage.layer.masksToBounds = false
        messageImage.layer.cornerRadius = messageImage.frame.size.width/2
        messageImage.clipsToBounds = true

        // Initialization code
    }

    @IBAction func deleteAction(sender: AnyObject) {
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/deleteMessage.php")!)
        request.HTTPMethod = "POST"
        let postString = "from=\(from)&to=\(to)&subject=\(subject)&content=\(content)"
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
            
            }
        }
        task.resume()
        NSNotificationCenter.defaultCenter().postNotificationName("deleteMessage", object: nil, userInfo:[
            "to":to,
            "from":from,
            "subject":subject])    }
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}

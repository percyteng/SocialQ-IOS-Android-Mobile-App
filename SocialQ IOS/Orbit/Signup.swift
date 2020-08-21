//
//  Signup.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-11.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class Signup: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    var Array = ["Queen's University", "University of Waterloo", "University of Toronto", " McMaster University", "University of Ottawa", "Other University"]
    @IBOutlet weak var name: UITextField!
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    @IBOutlet weak var pickerView: UIPickerView!
    @IBOutlet weak var uploadBtn: UIButton!
    @IBOutlet weak var uploadLabel: UILabel!
    @IBOutlet weak var UploadImage: UIButton!
    @IBOutlet weak var realImage: UIImageView!
    var schoolName = String()
    @IBOutlet weak var school: UIPickerView!
    var myActivityIndicator:UIActivityIndicatorView!

    @IBAction func signup(sender: AnyObject) {
        if name.text!.isEmpty{
            let alert = UIAlertController(title: "Invalid Name", message:"Please enter a user name", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}

        }
        else if name.text?.characters.count > 10{
            let alert = UIAlertController(title: "Invalid Name", message:"User name cannot be more than 10 characters", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}

        }
        else if email.text!.isEmpty || email.text!.rangeOfString("@") == nil{
            let alert = UIAlertController(title: "Invalid Email", message:"Please enter a valid email", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
            
        }
        else if password.text!.isEmpty{
            let alert = UIAlertController(title: "Invalid Password", message:"Please enter a password", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
            
        }
        else if password.text?.characters.count > 20{
            let alert = UIAlertController(title: "Invalid Password", message:"Password cannot be more than 20 characters", preferredStyle: .Alert)
            alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
            self.presentViewController(alert, animated: true){}
            
        }

        else{
            process()
        }
        
        
//        myImageUploadRequest()
        self.email.resignFirstResponder()
        self.password.resignFirstResponder()
        self.name.resignFirstResponder()
    }
    func process(){
        myActivityIndicator = ActivityIndicator().StartActivityIndicator(self);
        myActivityIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.WhiteLarge
        let request = NSMutableURLRequest(URL: NSURL(string: "http://www.percyteng.com/orbit/newUserTest.php")!)
        request.HTTPMethod = "POST"
        let postString = "useremail=\(email.text!)&password=\(password.text!)&username=\(name.text!)&school=\(schoolName)"
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
            let resultMessage:NSString = (responseString!["message"] as? NSString)!
            dispatch_async(dispatch_get_main_queue()) { [unowned self] in
                if result == 1{

                    ActivityIndicator().StopActivityIndicator(self,indicator: self.myActivityIndicator);
                    self.myImageUploadRequest()
//                    tempUser.username = self.name.text!
//                    tempUser.descript = ""
//                    let next = self.storyboard?.instantiateViewControllerWithIdentifier("reveal") as! SWRevealViewController
//                    next.loadView()
//                    
//                    self.presentViewController(next, animated: true, completion: nil)
                    
                }
                else {
                    ActivityIndicator().StopActivityIndicator(self,indicator: self.myActivityIndicator);

                    let alert = UIAlertController(title: "Invalid Input", message:resultMessage as String, preferredStyle: .Alert)
                    alert.addAction(UIAlertAction(title: "OK", style: .Default) { _ in })
                    self.presentViewController(alert, animated: true){}
                }
            }

        }
        task.resume()
    }
    @IBAction func skip(sender: AnyObject) {
        tempUser.username = "Visitor"
        tempUser.descript = ""
        let next = self.storyboard?.instantiateViewControllerWithIdentifier("reveal") as! SWRevealViewController
        next.loadView()
        
        self.presentViewController(next, animated: true, completion: nil)
        
        return;

    }
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
    }
    override func viewDidLoad() {
        UploadImage.contentMode = UIViewContentMode.ScaleAspectFit
        super.viewDidLoad()
        pickerView.delegate = self
        pickerView.dataSource = self
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return Array[row]
    }
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return Array.count
    }
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int{
        return 1
    }
    func pickerView(pickerView: UIPickerView, viewForRow row: Int, forComponent component: Int, reusingView view: UIView?) -> UIView {
        let pickerLabel = UILabel()
        let titleData = Array[row]
        let myTitle = NSAttributedString(string: titleData, attributes: [NSFontAttributeName:UIFont(name: "HelveticaNeue-Medium", size: 15.0)!,NSForegroundColorAttributeName:UIColor.lightGrayColor()])
        pickerLabel.attributedText = myTitle
        schoolName = pickerLabel.text!
        return pickerLabel
    }
    @IBAction func selectPhotoButtonTapped(sender: AnyObject) {
        
        let myPickerController = UIImagePickerController()
        myPickerController.delegate = self;
        myPickerController.sourceType = UIImagePickerControllerSourceType.PhotoLibrary
        
        self.presentViewController(myPickerController, animated: true, completion: nil)
        
    }
    func imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : AnyObject]){
        let pickedImage = info[UIImagePickerControllerOriginalImage] as? UIImage
        realImage.image = pickedImage
        uploadLabel.text=""
        uploadBtn.hidden = true
        self.dismissViewControllerAnimated(true, completion: nil)
        
    }
    func myImageUploadRequest()
    {
        
        let myUrl = NSURL(string: "http://www.percyteng.com/orbit/uploadPicIOS.php");
        //let myUrl = NSURL(string: "http://www.boredwear.com/utils/postImage.php");
        
        let request = NSMutableURLRequest(URL:myUrl!);
        request.HTTPMethod = "POST";
        
        let param = [
            "firstName"  : name.text!,
            "lastName"    : "Teng",
            "userId"    : "0"
        ]
        
        let boundary = generateBoundaryString()
        
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        
        //        let compressedImg = image.image!.lowestQualityJPEGNSData
        //        let images : UIImage = UIImage(data: compressedImg)!
        let reducedImage = ResizeImage(realImage.image!, targetSize: CGSizeMake(200.0, 200.0))
        let imageData = UIImageJPEGRepresentation(reducedImage, 1)
        print("size is \(imageData?.length)")
        
        if(imageData==nil)  {
            tempUser.username = self.name.text!
            tempUser.descript = ""
            let next = self.storyboard?.instantiateViewControllerWithIdentifier("reveal") as! SWRevealViewController
            next.loadView()
            
            self.presentViewController(next, animated: true, completion: nil)

            return;
        
        }
        
        request.HTTPBody = createBodyWithParameters(param, filePathKey: "file", imageDataKey: imageData!, boundary: boundary)
        
        
        //        myActivityIndicator.startAnimating();
        
        let task = NSURLSession.sharedSession().dataTaskWithRequest(request) {
            data, response, error in
            
            if error != nil {
                print("error=\(error)")
                return
            }
            
            // You can print out response object
            print("******* response = \(response)")
            
            // Print out reponse body
            let responseString = NSString(data: data!, encoding: NSUTF8StringEncoding)
            print("****** response data = \(responseString!)")
            
            do {
                if let jsonResult = try NSJSONSerialization.JSONObjectWithData(data!, options: []) as? NSDictionary {
                    print(jsonResult)
                }
            } catch let error as NSError {
                print(error.localizedDescription)
            }
            
            
            dispatch_async(dispatch_get_main_queue(),{
                tempUser.username = self.name.text!
                tempUser.descript = ""
                let next = self.storyboard?.instantiateViewControllerWithIdentifier("reveal") as! SWRevealViewController
                next.loadView()
                
                self.presentViewController(next, animated: true, completion: nil)

                //                self.myActivityIndicator.stopAnimating()
//                self.realImage.image = nil;
            });
            
            /*
             if let parseJSON = json {
             var firstNameValue = parseJSON["firstName"] as? String
             println("firstNameValue: \(firstNameValue)")
             }
             */
            
        }
        
        task.resume()
        
    }
    
    func ResizeImage(image: UIImage, targetSize: CGSize) -> UIImage {
        let size = image.size
        
        let widthRatio  = targetSize.width  / image.size.width
        let heightRatio = targetSize.height / image.size.height
        
        // Figure out what our orientation is, and use that to form the rectangle
        var newSize: CGSize
        if(widthRatio > heightRatio) {
            newSize = CGSizeMake(size.width * heightRatio, size.height * heightRatio)
        } else {
            newSize = CGSizeMake(size.width * widthRatio,  size.height * widthRatio)
        }
        
        // This is the rect that we've calculated out and this is what is actually used below
        let rect = CGRectMake(0, 0, newSize.width, newSize.height)
        
        // Actually do the resizing to the rect using the ImageContext stuff
        UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        image.drawInRect(rect)
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage!
    }
    
    func createBodyWithParameters(parameters: [String: String]?, filePathKey: String?, imageDataKey: NSData, boundary: String) -> NSData {
        var body = NSMutableData();
        
        if parameters != nil {
            for (key, value) in parameters! {
                body.appendString("--\(boundary)\r\n")
                body.appendString("Content-Disposition: form-data; name=\"\(key)\"\r\n\r\n")
                body.appendString("\(value)\r\n")
            }
        }
        
        let filename = "\(name.text!).JPG"
        
        let mimetype = "image/jpg"
        
        body.appendString("--\(boundary)\r\n")
        body.appendString("Content-Disposition: form-data; name=\"\(filePathKey!)\"; filename=\"\(filename)\"\r\n")
        body.appendString("Content-Type: \(mimetype)\r\n\r\n")
        body.appendData(imageDataKey)
        body.appendString("\r\n")
        
        
        
        body.appendString("--\(boundary)--\r\n")
        
        return body
    }
    
    
    
    
    func generateBoundaryString() -> String {
        return "Boundary-\(NSUUID().UUIDString)"
    }
    
    
    
}


extension UIImage {
    var uncompressedPNGData: NSData      { return UIImagePNGRepresentation(self)!        }
    var highestQualityJPEGNSData: NSData { return UIImageJPEGRepresentation(self, 1.0)!  }
    var highQualityJPEGNSData: NSData    { return UIImageJPEGRepresentation(self, 0.75)! }
    var mediumQualityJPEGNSData: NSData  { return UIImageJPEGRepresentation(self, 0.5)!  }
    var lowQualityJPEGNSData: NSData     { return UIImageJPEGRepresentation(self, 0.25)! }
    var lowestQualityJPEGNSData:NSData   { return UIImageJPEGRepresentation(self, 0.0)!  }
}
extension NSMutableData {
    
    func appendString(string: String) {
        let data = string.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: true)
        appendData(data!)
    }
}


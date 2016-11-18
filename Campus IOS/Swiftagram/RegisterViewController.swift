//
//  ViewController.swift
//  Swiftagram
//
//  Created by Ronny Kibet on 4/22/15.
//  Copyright (c) 2015 TeamAppCreative. All rights reserved.
//

import UIKit
import Parse

class RegisterViewController: UIViewController {

    @IBOutlet weak var usernameTextField: UITextField!
    
    
    @IBOutlet weak var emailTextField: UITextField!
    
    
    @IBOutlet weak var passwordTextField: UITextField!
    
    
    @IBAction func createAccountbtn(sender: AnyObject) {
        
        var username =  usernameTextField.text
        var email = emailTextField.text
        var password = passwordTextField.text

        //check for empty fields and alert the user
        if username.isEmpty || email.isEmpty || password.isEmpty {
            //alert user of empty field
        }else {
        
        //proceed to sign up user.
            var user = PFUser()
            user.username = username
            user.email = email
            user.password = password
            user.signUpInBackgroundWithBlock({
                (isSuccesful: Bool, error: NSError?) -> Void in
                
                if error == nil {
                    //SUCCESSS user signed up.
                    println(isSuccesful)
                }else {
                    //there was an error.
                    println(error)
                }
                
            })
            
            
            
            
            
        }
        
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        
        PFUser.logOutInBackground()
   
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}


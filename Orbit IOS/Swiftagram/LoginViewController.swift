//
//  LoginViewController.swift
//  Swiftagram
//
//  Created by Ronny Kibet on 4/22/15.
//  Copyright (c) 2015 TeamAppCreative. All rights reserved.
//

import UIKit
import Parse

class LoginViewController: UIViewController {
    
    
    @IBOutlet weak var usernameTextField: UITextField!
    
    @IBOutlet weak var passwordTextField: UITextField!
    
    
    @IBAction func loginbutton(sender: AnyObject) {
        
        var username = usernameTextField.text
        var password = passwordTextField.text
        
        if username.isEmpty || password.isEmpty {
            //empty field
            println("empty field")
        }else {
            //proceed with loggin in the user.
            PFUser.logInWithUsernameInBackground(username, password: password, block: {
                (user: PFUser?, error: NSError?) -> Void in
                
                if error == nil {
                    //SUCCESS user logged in, take user home
                    println(user)
                    self.performSegueWithIdentifier("goHomeFromLogin", sender: self)
                
                }else{
                    println(error)
                }

            })
            
        }
        
        
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

 

}

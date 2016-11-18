//
//  Boards.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-12.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class Boards: UIViewController ,UITableViewDelegate, UITableViewDataSource {
    var boards:[String] = ["EVENTS", "SPORTS", "TUTORING", "RIDESHARE","EXCHANGE","SERVICE","SUBLET"]
    
    var images = [UIImage(named: "tile_events"),UIImage(named: "tile_sports"),UIImage(named: "tile_tutoring"),UIImage(named: "tile_carpool"),UIImage(named: "tile_exchange"),UIImage(named: "tile_services"),UIImage(named: "tile_sublet")]
    
    @IBOutlet weak var myTableView: UITableView!

    @IBOutlet weak var toolBar: UIToolbar!
    @IBOutlet weak var Open: UIBarButtonItem!

    override func viewDidLoad() {
        super.viewDidLoad()
        

        
        Open.target = self.revealViewController()
        Open.action = #selector(SWRevealViewController.revealToggle(_:))

        self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
    }
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return boards.count
    }
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("cell", forIndexPath: indexPath) as!customCellBoards
        cell.boardPic.image = images[indexPath.row]
        cell.boardTitle.text = boards[indexPath.row]
        return cell
    }
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        // cell selected code here
    }
}


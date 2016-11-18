//
//  customCellBoards.swift
//  Orbit
//
//  Created by Percy teng on 2016-06-16.
//  Copyright © 2016 Percy teng. All rights reserved.
//

import UIKit

class customCellBoards: UITableViewCell{
    

    @IBOutlet weak var boardPic: UIImageView!
    @IBOutlet weak var boardTitle: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated:animated)
    }
    
}
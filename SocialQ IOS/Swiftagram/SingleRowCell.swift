//
//  SingleRowCell.swift
//  Swiftagram
//
//  Created by Ronny Kibet on 4/22/15.
//  Copyright (c) 2015 TeamAppCreative. All rights reserved.
//

import UIKit

class SingleRowCell: UITableViewCell {
    
    @IBOutlet weak var swiftgramImageView: UIImageView!
    
    @IBOutlet weak var swiftagramIMageLabel: UILabel!
    

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}

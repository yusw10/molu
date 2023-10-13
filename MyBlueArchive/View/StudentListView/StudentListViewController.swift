//
//  ViewController.swift
//  MyBlueArchive
//
//  Created by 유한석 on 2023/08/16.
//

import UIKit
import Foundation

final class MainViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .red
        
        let targetURL = "https://api-blue-archive.vercel.app/api/characters"

        let req = URLRequest(url: URL(string: targetURL)!, method: HttpMethod<Students>.get)
        URLSession.shared.request(req) { (data: Students?, error) in
            print(data)
        }
    }
}


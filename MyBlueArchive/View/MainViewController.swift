//
//  ViewController.swift
//  MyBlueArchive
//
//  Created by 유한석 on 2023/08/16.
//

import UIKit

final class MainViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .red
        
        let targetURL = "https://api-blue-archive.vercel.app/api/characters"
        guard let url = URL(string: targetURL) else {
            return
        }
        let request = URLRequest(url: url)
        
        let urlSession = URLSession(configuration: .default)
        urlSession.dataTask(with: request) { data, response, error in
            
        }
    
    }
}


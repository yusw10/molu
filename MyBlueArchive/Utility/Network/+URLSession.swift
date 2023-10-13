//
//  Extension-URLRequest.swift
//  MyBlueArchive
//
//  Created by cyldev on 2023/09/27.
//

import Foundation

extension URLSession {
    func request<T: Decodable>(_ urlRequest: URLRequest, completion: @escaping(T?, Error?) -> Void) {
        dataTask(with: urlRequest) { data, response, error in
            if let error = error {
                print("error: \(error.localizedDescription)")
                completion(nil, error)
            }
            
            if let response = response as? HTTPURLResponse,
               (200..<300).contains(response.statusCode),
               let data = data {
                print("URLSession data: \(String(describing: data))")
                let decodedData = try? JSONDecoder().decode(T.self, from: data)
                completion(decodedData, nil)
            }
        }.resume()
    }
}

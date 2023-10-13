//
//  +URLRequest.swift
//  MyBlueArchive
//
//  Created by cyldev on 2023/09/27.
//

import Foundation

enum HttpMethod<Body> {
    case get
    case post(Body?)
    case put(Body)
    case patch(Body)
    case delete(Body?)
}

extension URLRequest {
    init<Body: Encodable> (url: URL, method: HttpMethod<Body>) {
        self.init(url: url)
        self.timeoutInterval = TimeInterval(10)
        
        switch method {
        case .get:
            self.httpMethod = "GET"
        case .post(let body):
            self.httpMethod = "POST"
            self.httpBody = try? JSONEncoder().encode(body)
        case .put(let body):
            self.httpMethod = "PUT"
            self.httpBody = try? JSONEncoder().encode(body)
        case .patch(let body):
            self.httpMethod = "PATCH"
            self.httpBody = try? JSONEncoder().encode(body)
        case .delete(let body):
            self.httpMethod = "DELETE"
            self.httpBody = try? JSONEncoder().encode(body)
        }
    }
}

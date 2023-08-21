//
//  Character.swift
//  MyBlueArchive
//
//  Created by 유한석 on 2023/08/16.
//

import Foundation

// MARK: - Charactesrs
struct Charactesrs: Codable {
    let message: String
    let dataAllPage: Int
    let characterDetail: [Character] //TODO: 원래는 data 로 오는데 이거 임의로 바꿔도 되는지 모르겠음.
    
    enum Codingkeys: String, CodingKey {
        case message 
        case dataAllPage
        case characterDetail = "data"
    }
}

// MARK: - Datum
struct Character: Codable {
    let id, name, school, birthday: String
    let photoURL: String
    let image: String
    let imageSchool: String
    let damageType: DamageType

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case name, school, birthday
        case photoURL = "photoUrl"
        case image, imageSchool, damageType
    }
}

enum DamageType: String, Codable {
    case explosive = "Explosive"
    case mystic = "Mystic"
    case penetration = "Penetration"
}

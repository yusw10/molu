//
//  Character.swift
//  MyBlueArchive
//
//  Created by 유한석 on 2023/08/16.
//

import Foundation

// MARK: - Characters
struct Students: Codable {
    let message: String
    let dataAllPage: Int
    let data: [StudentDetail]
}

// MARK: - Datum
struct StudentDetail: Codable {
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


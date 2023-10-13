//
//  ModelTest.swift
//  MyBlueArchiveTests
//
//  Created by 유한석 on 2023/08/18.
//

import XCTest

final class ModelTest: XCTestCase {

    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }
    
    //MARK: - Model Characters  test
    
    func test_Characters_JSON_to_Model() throws {
        guard let jsonData = load(fileName: "mock_Characters", extensionType: "json") else {
            return
        }
        let characters = try JSONDecoder().decode(Students.self, from: jsonData)
        XCTAssert(characters.dataAllPage == 119)
    }
    
    func test_Characters_Model_to_JSON() throws {
        
    }
    
    //MARK: - Utility
    func load(fileName: String, extensionType: String) -> Data? {
        // 3. 파일 위치
        guard let fileLocation = Bundle.main.url(
            forResource: fileName,
            withExtension: extensionType
        ) else { return nil }
        
        
        do {
            // 4. 해당 위치의 파일을 Data로 초기화하기
            let data = try Data(contentsOf: fileLocation)
            return data
        } catch {
            // 5. 잘못된 위치나 불가능한 파일 처리 (오늘은 따로 안하기)
            return nil
        }
    }
}



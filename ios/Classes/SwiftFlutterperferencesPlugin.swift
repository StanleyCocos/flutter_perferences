import Flutter
import UIKit
import KeychainSwift

public class SwiftFlutterperferencesPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutterperferences", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterperferencesPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    //result("iOS " + UIDevice.current.systemVersion)
    
    if("getCache" == call.method){
        let uuid: String? = KeychainSwift().get("designUUID")
        var data: [String: Any] = [:]
        let appdomain = Bundle.main.bundleIdentifier ?? ""
        let prefs = UserDefaults.standard.persistentDomain(forName: appdomain) ?? [:]
        for key in prefs.keys {
            if(key == "accessToken"){
                data["accessToken"] = prefs[key]
            } else if (key == "refreshToken"){
                data["refreshToken"] = prefs[key]
            } else if (key == "newVersionUser"){
                data["isNewUser"] = prefs[key]
            } else if (key == "SelectLabel"){
                data["isSelectLabel"] = prefs[key]
            }
        }
        
        if let uuid = uuid {
            if uuid.count > 0 {
                data["imei"] = uuid
            } else {
                data["imei"] = ""
            }
        } else {
             data["imei"] = ""
        }
    
        result(data)
    }
  }
}

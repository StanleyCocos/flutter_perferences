import Flutter
import UIKit

public class SwiftFlutterperferencesPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutterperferences", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterperferencesPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    //result("iOS " + UIDevice.current.systemVersion)
    
    if("getCache" == call.method){
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
            } else if (key == "newSelectLabel"){
                data["isSelectLabel"] = prefs[key]
            }
        }
        result(data)
    }
  }
}

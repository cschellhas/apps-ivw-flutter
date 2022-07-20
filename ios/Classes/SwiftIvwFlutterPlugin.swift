import Flutter
import UIKit
import INFOnlineLibrary
import IOMbLibrary

public class SwiftIvwFlutterPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "ivw_flutter", binaryMessenger: registrar.messenger())
    let instance = SwiftIvwFlutterPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
      //result("iOS " + UIDevice.current.systemVersion)
      switch call.method {
        case "initialize":
          callInitialize(call: call, result: result)
        case "initializeIOMb":
          callInitializeIOMb(call: call, result: result)
        case "logEvent":
          callLogEvent(call: call, result: result)
        case "logEventIOMb":
          callLogEventIOMb(call: call, result: result)
        default:
            result(FlutterMethodNotImplemented)
        }

    }
    
    func callInitialize(call: FlutterMethodCall, result: FlutterResult) {
        if let args = call.arguments as? Dictionary<String, Any>,
        let appId = args["appId"] as? String{
         let debug = args["debug"] as? Bool ?? true;
        if(debug){
            IOLLogging.setDebugLogLevel(.trace)
        }
        IOLSession.defaultSession(for: .SZM).start(withOfferIdentifier:appId,
          privacyType: .LIN)
            result(true)
            return;
        } else {
            result(FlutterError.init(code: "ivw_flutter", message: "Arguments missing", details: nil));
            return;
        }
    }
    
    func callInitializeIOMb(call: FlutterMethodCall, result: FlutterResult) {
        if let args = call.arguments as? Dictionary<String, Any>,
        let appId = args["appId"] as? String,
        let url = args["url"] as? String {
         let debug = args["debug"] as? Bool ?? true;
        if(debug){
            IOMBLogging.setDebugLogLevel(.info)
        }
            guard let url = URL(string: url) else { return }
            let configuration = IOMBSessionConfiguration(offerIdentifier: appId, baseURL: url)
            IOMBSession.defaultSession.start(with: configuration)
            result(true)
            return;
        } else {
            result(FlutterError.init(code: "ivw_flutter", message: "Arguments missing", details: nil));
            return;
        }
    }
    
    func callLogEvent(call: FlutterMethodCall, result: FlutterResult) {
        if let args = call.arguments as? Dictionary<String, Any>,
        let ivwPathName = args["ivwPath"] as? String{
            print("Log EVENT")
            let event = IOLViewEvent(type: .appeared, category: ivwPathName, comment: nil)
               IOLSession.defaultSession(for: .SZM).logEvent(event)
        } else {
            result(FlutterError.init(code: "ivw_flutter", message: "Arguments missing", details: nil));
            return;
        }
        
    }
    
    func callLogEventIOMb(call: FlutterMethodCall, result: FlutterResult) {
        if let args = call.arguments as? Dictionary<String, Any>,
        let ivwPathName = args["ivwPath"] as? String{
            print("Log EVENT IOMB")
            let event = IOMBViewEvent(type: .appeared, category: ivwPathName, comment: nil)
             IOMBSession.defaultSession.logEvent(event)
        } else {
            result(FlutterError.init(code: "ivw_flutter", message: "Arguments missing", details: nil));
            return;
        }
        
    }
}

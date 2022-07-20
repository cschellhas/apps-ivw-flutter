import Flutter
import UIKit


public class SwiftIvwFlutterPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
   // let channel = FlutterMethodChannel(name: "ivw_flutter", binaryMessenger: registrar.messenger())
    let instance = SwiftIvwFlutterPlugin()
    //registrar.addMethodCallDelegate(instance, channel: channel)
  }


}

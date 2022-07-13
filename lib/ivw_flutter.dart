
import 'dart:async';

import 'package:flutter/services.dart';

class IvwFlutter {
  static const MethodChannel _channel = const MethodChannel('ivw_flutter');

  static Future<bool> initialize(String appId, {bool debug = true}) async {
    final bool result = await _channel.invokeMethod('initialize', <String, dynamic>{
      'appId': appId,
      'debug': debug,
    });
    return result;
  }

  static Future<bool> initializeIOMb(String url, String appId, {bool debug = true}) async {
    final bool result = await _channel.invokeMethod('initializeIOMb', <String, dynamic>{
      'url' : url,
      'appId': appId,
      'debug': debug,
    });
    return result;
  }

  static Future<bool> logEvent(String ivwPath) async {
    final bool result = await _channel.invokeMethod('logEvent', <String, dynamic>{
      'ivwPath': ivwPath,
    });
    return result;
  }

  static Future<bool> logEventIOMb(String ivwPath) async {
    final bool result = await _channel.invokeMethod('logEventIOMb', <String, dynamic>{
      'ivwPath': ivwPath,
    });
    return result;
  }
}

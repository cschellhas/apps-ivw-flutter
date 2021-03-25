
import 'dart:async';

import 'package:flutter/services.dart';

class IvwFlutter {
  static const MethodChannel _channel = const MethodChannel('ivw_flutter');

  static Future<bool> initialize(String appId) async {
    final bool result = await _channel.invokeMethod('initialize', <String, dynamic>{
      'appId': appId,
    });
    return result;
  }

  static Future<bool> logEvent(String ivwPath) async {
    final bool result = await _channel.invokeMethod('logEvent', <String, dynamic>{
      'ivwPath': ivwPath,
    });
    return result;
  }
}

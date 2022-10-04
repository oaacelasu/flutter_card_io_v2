import 'dart:async';

import 'package:flutter/services.dart';

class FlutterCardIoV2 {
  static const MethodChannel _channel =
      const MethodChannel('flutter_card_io_v2');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<dynamic> scanCard(Map<String, dynamic> args) async {
    final result = await _channel.invokeMethod('scanCard', args);
    return result;
  }
}

import 'dart:async';

import 'package:flutter/services.dart';

class Flutterperferences {
  static const MethodChannel _channel = const MethodChannel(
      'flutterperferences');
  static Map<String, dynamic> _cache;

  static Future<void> get getCache async {
    var cache = await _channel.invokeMethod('getCache');
    if (cache != null) {
      _cache = new Map<String, dynamic>.from(cache);
    } else {
      _cache = {};
    }
  }

  static bool get isNewUser {
    return _cache["isNewUser"] ?? false;
  }

  static bool get isSelectLabel {
    return _cache["isSelectLabel"] ?? false;
  }

  static String get refreshToken {
    return _cache["refreshToken"] ?? "";
  }

  static String get accessToken {
    return _cache["accessToken"] ?? "";
  }

  static String get imei {
    return _cache["imei"] ?? "";
  }
}

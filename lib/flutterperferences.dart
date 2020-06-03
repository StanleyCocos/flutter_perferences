import 'dart:async';

import 'package:flutter/services.dart';

class Flutterperferences {
  static const MethodChannel _channel =
      const MethodChannel('flutterperferences');
  static Map<String, dynamic> _cache;

  static Future<void> get getCache async {
    try {
      var cache = await _channel.invokeMethod('getCache');
      _cache = cache != null ? Map<String, dynamic>.from(cache) : {};
    } on MissingPluginException catch (e) {
      print("$e");
    }
  }

  static bool get isNewUser {
    if (_cache == null || !_cache.containsKey("isNewUser")) return false;
    return _cache["isNewUser"] ?? false;
  }

  static bool get isSelectLabel {
    if (_cache == null || !_cache.containsKey("isSelectLabel")) return false;
    return _cache["isSelectLabel"] ?? false;
  }

  static String get refreshToken {
    if (_cache == null || !_cache.containsKey("refreshToken")) return "";
    return _cache["refreshToken"] ?? "";
  }

  static String get accessToken {
    if (_cache == null || !_cache.containsKey("accessToken")) return "";
    return _cache["accessToken"] ?? "";
  }

  static String get imei {
    if (_cache == null || !_cache.containsKey("imei")) return "";
    return _cache["imei"] ?? "";
  }
}

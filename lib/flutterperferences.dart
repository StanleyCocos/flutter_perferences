import 'dart:async';

import 'package:flutter/services.dart';

class Flutterperferences {
  Flutterperferences._();

  static Flutterperferences _instance;

  factory Flutterperferences() {
    if (_instance == null) {
      _instance = Flutterperferences._();
//      _instance._getCache;
    }
    return _instance;
  }

  MethodChannel _channel = const MethodChannel('flutterperferences');
  Map<String, dynamic> _cache;

  Future<void> get getCache async {
    var cache = await _channel.invokeMethod('getCache');
    if (cache != null) {
      _cache = new Map<String, dynamic>.from(cache);
    } else {
      _cache = {};
    }
  }

  bool get isNewUser {
    return _cache["isNewUser"] ?? false;
  }

  bool get isSelectLabel {
    return _cache["isSelectLabel"] ?? false;
  }

  String get refreshToken {
    return _cache["refreshToken"] ?? "";
  }

  String get accessToken {
    return _cache["accessToken"] ?? "";
  }

  String get imei {
    return _cache["imei"] ?? "";
  }
}

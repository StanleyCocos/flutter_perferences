import 'dart:async';

import 'package:flutter/services.dart';

class Flutterperferences {

  Flutterperferences._();
  static Flutterperferences _instance;
  factory Flutterperferences() {
    if (_instance == null) {
      _instance = Flutterperferences._();
      _instance._getCache;
    }
    return _instance;
  }

  MethodChannel _channel = const MethodChannel('flutterperferences');
  Map<String, dynamic> _cache;

   Future<void> get _getCache async {
    final Map<String, dynamic> cache = await _channel.invokeMethod('getCache');
    if(cache != null){
      _cache = cache;
    } else {
      _cache = {};
    }
  }

  bool get isNewUser{
     return _cache["isNewUser"] ?? true;
  }

  bool get isSelectLabel{
     return _cache["isSelectLabel"] ?? true;
  }

  String get refreshToken{
     return _cache["refreshToken"] ?? "";
  }

  String get accessToken{
     return _cache["accessToken"] ?? "";
  }
}

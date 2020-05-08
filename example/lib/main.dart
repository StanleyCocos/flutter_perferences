import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutterperferences/flutterperferences.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    print("123132");
    await Flutterperferences().getCache;
    print("123132111");
    print("isNewUser : ${Flutterperferences().isNewUser}");
    print("isSelectLabel : ${Flutterperferences().isSelectLabel}");
    print("accessToken: ${Flutterperferences().accessToken}");
    print("refreshToken: ${Flutterperferences().refreshToken}");
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: GestureDetector(
            onTap: initPlatformState,
            child: Container(
              width: 100,
              height: 40,
              child: Text("get cache"),
            ),
          ),
        ),
      ),
    );
  }
}

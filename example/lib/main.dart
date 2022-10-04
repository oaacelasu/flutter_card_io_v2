import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_card_io_v2/flutter_card_io_v2.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _scanResult = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String? platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FlutterCardIoV2.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion ?? 'Unknown';
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  _scanCard() async {
    Map<String, dynamic> details;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      details = Map<String, dynamic>.from(await FlutterCardIoV2.scanCard({
            "requireExpiry": true,
            "scanExpiry": true,
            "requireCVV": true,
            "requirePostalCode": true,
            "restrictPostalCodeToNumericOnly": false,
            "requireCardHolderName": true,
            "hideCardIOLogo": true,
            "useCardIOLogo": false,
            "usePayPalActionbarIcon": false,
            "suppressManualEntry": true,
            "suppressConfirmation": true,
            "scanInstructions": 'Escanea por favor la tarjeta',
          }) ??
          Map<String, dynamic>());
    } on PlatformException {
      print('PlatformException');
      return;
    }

    if (details == null) {
      print('details == null');

      return;
    } else {
      print(details.toString());
    }

    if (!mounted) return;
    setState(() {
      _scanResult = details.toString();
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              Text('Running on: $_platformVersion\n'),
              Text('Scan result on: $_scanResult\n'),
              ElevatedButton(
                child: Text('scan'),
                onPressed: () async => _scanCard(),
              )
            ],
          ),
        ),
      ),
    );
  }
}

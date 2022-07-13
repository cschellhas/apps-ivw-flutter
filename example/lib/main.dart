import 'package:flutter/material.dart';
import 'package:ivw_flutter/ivw_flutter.dart';

void main() async {

  var url = "https://data-ef4e2c0163.example.com";
  var appID = "iamtest";
  WidgetsFlutterBinding.ensureInitialized();
  await IvwFlutter.initialize(appID).then((value) {
    print("ivw_flutter: initialized = $value");
  }).catchError((e) {
    print("ivw_flutter: failed with $e");
  });
  await IvwFlutter.initializeIOMb(url, appID).then((value) {
    print("ivw_flutter: initialized = $value");
  }).catchError((e) {
    print("ivw_flutter: failed with $e");
  });
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('IVW (infonline) example app'),
        ),
        body: Center(
          child: TextButton(
            child: Text('Send test event'),
            onPressed: () {
              IvwFlutter.logEvent('ivwPath');
              IvwFlutter.logEventIOMb('ivwPath');
            },
          ),
        ),
      ),
    );
  }
}

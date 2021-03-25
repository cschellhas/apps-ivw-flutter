import 'package:flutter/material.dart';
import 'package:ivw_flutter/ivw_flutter.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await IvwFlutter.initialize('iamtest').then((value) {
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
            },
          ),
        ),
      ),
    );
  }
}

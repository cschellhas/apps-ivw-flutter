package eu.ivw.ivw_flutter

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import de.infonline.lib.IOLSession
import de.infonline.lib.IOLSessionPrivacySetting
import de.infonline.lib.IOLSessionType
import de.infonline.lib.IOLViewEvent
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** IvwFlutterPlugin */
class IvwFlutterPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {

  companion object {
    const val TAG = "ivw_flutter"

    var applicationContext: Context? = null
  }

  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  private var currentSessionType: IOLSessionType? = null
  private var currentUserInput: String? = null
  private var currentPrivacySetting: IOLSessionPrivacySetting? = null

  private fun getCurrentSessionType(): IOLSessionType? {
    return currentSessionType
  }

  private fun getCurrentSession(): IOLSession? {
    return IOLSession.getSessionForType(currentSessionType)
  }

  private fun getCurrentUserInput(): String? {
    return currentUserInput
  }

  private fun getCurrentPrivacySetting(): IOLSessionPrivacySetting? {
    return currentPrivacySetting
  }

  private fun clearPrivacySetting() {
    currentPrivacySetting = null
  }

  private fun initIOLSessionType(
          type: IOLSessionType,
          customerData: String,
          privacySetting: IOLSessionPrivacySetting
  ) {
    IOLSession.getSessionForType(type).initIOLSession("iamtest", null, customerData, BuildConfig.DEBUG, privacySetting)
    currentUserInput = customerData
    currentSessionType = type
    currentPrivacySetting = privacySetting
  }

  private fun callLogEvent(call: MethodCall, result: Result) {
    val ivwPathName: String? = call.argument("ivwPath")
    val event = IOLViewEvent(IOLViewEvent.IOLViewEventType.Appeared)
    event.category = ivwPathName
    IOLSession.getSessionForType(IOLSessionType.SZM).logEvent(event)

    result.success(true)
  }

  private fun callInitialize(call: MethodCall, result: Result) {
    val appId: String? = call.argument("appId") ?: "iamtest"
    if (appId == null || appId.isEmpty()) {
      result.error("no_app_id", "a null or empty appId was provided", null)
      return
    } else {
      // The IOLSession needs the application context to log enterForeground & enterBackground
      // events correctly.
      IOLSession.init(applicationContext)

      // Crash
      // TODO: java.lang.NoClassDefFoundError: Failed resolution of: Landroidx/localbroadcastmanager/content/LocalBroadcastManager;
      initIOLSessionType(
              IOLSessionType.SZM,
              appId,
              IOLSessionPrivacySetting.LIN
      )
      // @Nullable Context var1, @NonNull String var2, boolean var3, @NonNull IOLSessionPrivacySetting var4
      /*IOLSession.getSessionForType(IOLSessionType.SZM).initIOLSession(
              //applicationContext,
              "aadtonl", //"iamtest",
              BuildConfig.DEBUG,
              IOLSessionPrivacySetting.LIN
      )*/

      result.success(true)

      // Alternatively you can initialize the IOLSession directly, if you want to initialize the
      // session directly.
      //IOLSession.getSessionForType(IOLSessionType.SZM).initIOLSession(this, "iamtest", true, IOLSessionPrivacySetting.LIN)
      //IOLSession.getSessionForType(IOLSessionType.OEWA).initIOLSession(this, "iamtest", true, IOLSessionPrivacySetting.LIN)
    }
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    // setup channel
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "ivw_flutter")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "initialize" -> callInitialize(call, result)
      "logEvent" -> callLogEvent(call, result)
      else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    Log.v(TAG, "onAttachedToActivity")
    applicationContext = binding.activity.applicationContext

    // Your plugin is now associated with an Android Activity.
    //
    // If this method is invoked, it is always invoked after
    // onAttachedToFlutterEngine().
    //
    // You can obtain an Activity reference with
    // binding.getActivity()
    //
    // You can listen for Lifecycle changes with
    // binding.getLifecycle()
    //
    // You can listen for Activity results, new Intents, user
    // leave hints, and state saving callbacks by using the
    // appropriate methods on the binding.


  }

  override fun onDetachedFromActivityForConfigChanges() {
    Log.v(TAG, "onDetachedFromActivityForConfigChanges")

    // The Activity your plugin was associated with has been
    // destroyed due to config changes. It will be right back
    // but your plugin must clean up any references to that
    // Activity and associated resources.
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    Log.v(TAG, "onReattachedToActivityForConfigChanges")
    // Your plugin is now associated with a new Activity instance
    // after config changes took place. You may now re-establish
    // a reference to the Activity and associated resources.
  }

  override fun onDetachedFromActivity() {
    Log.v(TAG, "onDetachedFromActivity")
    // Your plugin is no longer associated with an Activity.
    // You must clean up all resources and references. Your
    // plugin may, or may not ever be associated with an Activity
    // again.
  }
}

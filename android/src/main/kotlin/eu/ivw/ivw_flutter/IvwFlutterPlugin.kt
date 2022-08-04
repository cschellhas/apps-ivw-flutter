package eu.ivw.ivw_flutter

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import de.infonline.lib.IOLSession
import de.infonline.lib.IOLSessionPrivacySetting
import de.infonline.lib.IOLSessionType
import de.infonline.lib.IOLViewEvent
import de.infonline.lib.iomb.IOLDebug
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import de.infonline.lib.iomb.IOMB
import de.infonline.lib.iomb.measurements.Measurement
import de.infonline.lib.iomb.measurements.iomb.IOMBSetup


/** IvwFlutterPlugin */
class IvwFlutterPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {

  companion object {
    const val TAG = "ivw_flutter"

    var applicationContext: Context? = null
    lateinit var IOMB_SESSION: Measurement
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


  private fun callLogEvent(call: MethodCall, result: Result) {
    val ivwPathName: String? = call.argument("ivwPath")
    val event = IOLViewEvent(IOLViewEvent.IOLViewEventType.Appeared)
    event.category = ivwPathName
    IOLSession.getSessionForType(IOLSessionType.SZM).logEvent(event)

    result.success(true)
  }

  private fun callLogEventIOMb(call: MethodCall, result: Result) {
    val ivwPathName: String? = call.argument("ivwPath")

    val event = de.infonline.lib.iomb.IOLViewEvent(type = de.infonline.lib.iomb.IOLViewEvent.IOLViewEventType.Appeared, category = ivwPathName)
    IOMB_SESSION.logEvent(event)

    result.success(true)
  }

  private fun callInitialize(call: MethodCall, result: Result) {
    val appId: String = call.argument("appId") ?: "iamtest"
    val debug: Boolean = call.argument("debug") ?: true
    if (appId == null || appId.isEmpty()) {
      result.error("no_app_id", "a null or empty appId was provided", null)
      return
    } else if (applicationContext != null) {
      try {
        Log.d("[IVW Init]", "Init with" + debug.toString() + appId);
        // The IOLSession needs the application context to log enterForeground & enterBackground events correctly.
        IOLSession.init(applicationContext)
        IOLSession.getSessionForType(IOLSessionType.SZM)          // Session Type SZM
                  .initIOLSession(appId,                          // Offer Identifier
                                  debug,              // Debug mode on/off
                                  IOLSessionPrivacySetting.LIN)   // Priv
        result.success(true)
      } catch (e: Exception) {
        result.error("unexpected_error", "${e?.message}", null)
      }
    } else {
      result.error("no_context", "application context was null", null)
    }
  }

  private fun callInitializeIOMb(call: MethodCall, result: Result) {
    val appId: String? = call.argument("appId") ?: "iamtest"
    val url: String? = call.argument("url") ?: "url"
    val debug: Boolean = call.argument("debug") ?: true
    if (appId == null || appId.isEmpty() || url == null || url.isEmpty()) {
      result.error("no_app_id", "a null or empty appId/url was provided", null)
      return
    } else if (applicationContext != null) {
      try {
        IOLDebug.debugMode = debug
    //    IOLDebug.debugMode = debug
        // The IOLSession needs the application context to log enterForeground & enterBackground events correctly.
        val setup = IOMBSetup(baseUrl = url,
        offerIdentifier = appId)

        IOMB.create(setup).subscribe { it ->
          IOMB_SESSION = it
        }
        result.success(true)
      } catch (e: Exception) {
        result.error("unexpected_error", "${e?.message}", null)
      }
    } else {
      result.error("no_context", "application context was null", null)
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
      "initializeIOMb" -> callInitializeIOMb(call, result)
      "logEvent" -> callLogEvent(call, result)
      "logEventIOMb" -> callLogEventIOMb(call, result)
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

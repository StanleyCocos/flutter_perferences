package com.addcn.flutterperferences

import android.content.Context
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** FlutterperferencesPlugin */
public class FlutterperferencesPlugin: FlutterPlugin, MethodCallHandler {

  private var channel: MethodChannel? = null
  private var context: Context? = null

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    onAttachedToEngine(flutterPluginBinding.applicationContext, flutterPluginBinding.binaryMessenger)
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val instance = FlutterperferencesPlugin()
      instance.onAttachedToEngine(registrar.context(), registrar.messenger())
    }
  }

  private fun onAttachedToEngine(context: Context, binaryMessenger: BinaryMessenger) {
    this.context =context
    channel = MethodChannel(binaryMessenger, "flutterperferences")
    channel?.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getCache") {
      val map = HashMap<String, Any>()
      if (context == null) {
        result.success(map)
        return
      }
      val sp = context!!.getSharedPreferences("share_data", Context.MODE_PRIVATE)
      val isNewUser = sp.getBoolean("guildeNew", false)
      val isSelectLabel = sp.getBoolean("newGuide", false)
      val userInfo = sp.getString("userinfo", "") ?: ""
      val userBean = JsonUtil.fromJson(userInfo, UserInfo::class.java)
      map["isNewUser"] = isNewUser
      map["isSelectLabel"] = isSelectLabel
      map["refreshToken"] = userBean?.data?.refreshToken ?: ""
      map["accessToken"] = userBean?.data?.accessToken ?: ""
      result.success(map)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
  }
}


package paymentez.com.flutter_card_io_v2

import android.content.Intent
import androidx.annotation.NonNull;
import io.card.payment.CardIOActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** FlutterCardIoV2Plugin */
public class FlutterCardIoV2Plugin: FlutterPlugin , ActivityAware {

  lateinit var handlerImpl: MethodCallHandlerImpl
  lateinit var methodChannel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_card_io_v2")
    handlerImpl = MethodCallHandlerImpl()
    methodChannel.setMethodCallHandler(handlerImpl)
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
      val channel = MethodChannel(registrar.messenger(), "flutter_card_io_v2")
      val handler = MethodCallHandlerImpl()
      channel.setMethodCallHandler(handler)
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
  }

  override fun onDetachedFromActivity() {
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    handlerImpl.setActivityPluginBinding(binding)

  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    handlerImpl.setActivityPluginBinding(binding)

  }

  override fun onDetachedFromActivityForConfigChanges() {
  }


}

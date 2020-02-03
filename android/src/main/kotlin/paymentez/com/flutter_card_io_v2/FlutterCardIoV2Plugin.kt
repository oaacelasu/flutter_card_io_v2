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
    handlerImpl = MethodCallHandlerImpl(flutterPluginBinding)
    methodChannel.setMethodCallHandler(handlerImpl)
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

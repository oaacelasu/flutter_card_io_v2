package paymentez.com.flutter_card_io_v2

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import io.card.payment.CardIOActivity
import io.card.payment.CardType
import io.card.payment.CreditCard
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener
import java.util.*

const val MY_SCAN_REQUEST_CODE = 100

class MethodCallHandlerImpl: MethodChannel.MethodCallHandler , ActivityResultListener{

    private lateinit var mActivityPluginBinding : ActivityPluginBinding
    var mResult : MethodChannel.Result? = null

    fun setActivityPluginBinding(@Nullable activityPluginBinding: ActivityPluginBinding) {
        this.mActivityPluginBinding = activityPluginBinding
    }


    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
        when (call.method) {
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }
            "scanCard" -> handleScanCard(call, result)
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun handleScanCard(call: MethodCall, result: MethodChannel.Result) {
        mResult = result
        mActivityPluginBinding.addActivityResultListener(this)

        val scanIntent = Intent(mActivityPluginBinding.activity, CardIOActivity::class.java)
        var requireExpiry = false
        if (call.hasArgument("requireExpiry")) {
            requireExpiry = call.argument("requireExpiry")?:false
        }
        var requireCVV = false
        if (call.hasArgument("requireCVV")) {
            requireCVV = call.argument("requireCVV")?:false
        }

        var requirePostalCode = false
        if (call.hasArgument("requirePostalCode")) {
            requirePostalCode = call.argument("requirePostalCode")?:false
        }

        var requireCardHolderName = false
        if (call.hasArgument("requireCardHolderName")) {
            requireCardHolderName = call.argument("requireCardHolderName")?:false
        }

        var restrictPostalCodeToNumericOnly = false
        if (call.hasArgument("restrictPostalCodeToNumericOnly")) {
            restrictPostalCodeToNumericOnly = call.argument("restrictPostalCodeToNumericOnly")?:false
        }

        var scanExpiry = true
        if (call.hasArgument("scanExpiry")) {
            scanExpiry = call.argument("scanExpiry")?:true
        }

        var scanInstructions: String? = null
        if (call.hasArgument("scanInstructions")) {
            scanInstructions = call.argument("scanInstructions")
        }

        var suppressManualEntry = false
        if (call.hasArgument("suppressManualEntry")) {
            suppressManualEntry = call.argument("suppressManualEntry")?:false
        }

        var suppressConfirmation = false
        if (call.hasArgument("suppressConfirmation")) {
            suppressConfirmation = call.argument("suppressConfirmation")?:false
        }

        var useCardIOLogo = false
        if (call.hasArgument("useCardIOLogo")) {
            useCardIOLogo = call.argument("useCardIOLogo")?:false
        }

        var hideCardIOLogo = false
        if (call.hasArgument("hideCardIOLogo")) {
            hideCardIOLogo = call.argument("hideCardIOLogo")?:false
        }

        var usePayPalActionbarIcon = true
        if (call.hasArgument("usePayPalActionbarIcon")) {
            usePayPalActionbarIcon = call.argument("usePayPalActionbarIcon")?:true
        }

        var keepApplicationTheme = false
        if (call.hasArgument("keepApplicationTheme")) {
            keepApplicationTheme = call.argument("keepApplicationTheme")?:false
        }

        // customize these values to suit your needs.
        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, requireExpiry) // default: false

        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, scanExpiry)
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, requireCVV) // default: false

        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, requirePostalCode) // default: false

        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, requireCardHolderName)
        scanIntent.putExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, restrictPostalCodeToNumericOnly)
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_INSTRUCTIONS, scanInstructions)
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, suppressManualEntry)
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, suppressConfirmation)
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, useCardIOLogo)
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, hideCardIOLogo)
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, usePayPalActionbarIcon)
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, keepApplicationTheme)

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.

        mActivityPluginBinding.activity.startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {


        if (requestCode == MY_SCAN_REQUEST_CODE) {

            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {

                val scanResult: CreditCard? = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)

                val response: MutableMap<String, Any?> = HashMap()
                response["cardholderName"] = scanResult!!.cardholderName
                response["cardNumber"] = scanResult.cardNumber
                var cardType: String? = null
                if (scanResult.cardType != CardType.UNKNOWN && scanResult.cardType != CardType.INSUFFICIENT_DIGITS) {
                    when (scanResult.cardType) {
                        CardType.AMEX -> cardType = "Amex"
                        CardType.DINERSCLUB -> cardType = "DinersClub"
                        CardType.DISCOVER -> cardType = "Discover"
                        CardType.JCB -> cardType = "JCB"
                        CardType.MASTERCARD -> cardType = "MasterCard"
                        CardType.VISA -> cardType = "Visa"
                        CardType.MAESTRO -> cardType = "Maestro"
                        else -> {
                        }
                    }
                }
                response["cardType"] = cardType
                response["redactedCardNumber"] = scanResult.redactedCardNumber
                response["expiryMonth"] = scanResult.expiryMonth
                response["expiryYear"] = scanResult.expiryYear
                response["cvv"] = scanResult.cvv
                response["postalCode"] = scanResult.postalCode

                mResult!!.success(response)
            } else {
                mResult!!.success(null)
            }
            return true
        }
        return false
    }

}

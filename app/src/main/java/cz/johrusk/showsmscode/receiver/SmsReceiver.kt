package cz.johrusk.showsmscode.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import cz.johrusk.showsmscode.service.MsgHandlerService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

/**
 * Broadcast receiver which handles receiving of SMS
 *
 * @author Josef Hruska (pepa.hruska@gmail.com)
 */
class SmsReceiver : BroadcastReceiver(), AnkoLogger {
    override fun onReceive(context: Context, intent: Intent) {
        debug("SMS Received")
        val messages = getMessages(intent)
        for (message in messages) {
            try {
                debug("receiving sms from  ${message!!.displayOriginatingAddress}") //message always have this property - it cant be null
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (message != null) {

                val msgHandler = Intent(context, MsgHandlerService::class.java)
                val msgContent = message.messageBody
                val msgSender = message.displayOriginatingAddress
                val msg = Bundle()

                debug("Content of SMS $msgContent / $msgSender")
                msg.putStringArray("msg", arrayOf(msgSender, msgContent))
                msgHandler.putExtra("msg", msg)
                context.startService(msgHandler)
            }
        }
    }

    @Synchronized fun getMessages(intent: Intent): Array<SmsMessage?> {
        val bundle = intent.extras
        val format = bundle.getString("format") //From API 23+ it is necessary to get format to choose between 3GPP/3GPP2 formatting

        val messages: Array<Any>? = bundle.get("pdus") as Array<Any> // It cant be empty
        if (messages != null) {
            try {
                val smsMessages = arrayOfNulls<SmsMessage>(messages.size)
                for (i in messages.indices) {
                    smsMessages[i] = SmsMessage.createFromPdu(messages[i] as ByteArray, format)
                }
                return smsMessages
            } catch (e: SecurityException) {
                return arrayOfNulls(0)
            }

        } else {
            return arrayOfNulls(0)
        }
    }
}
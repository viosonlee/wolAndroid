package lee.maitarou.wol.wol

import android.content.Context
import androidx.core.content.edit
import lee.maitarou.wol.utils.sp
import lee.maitarou.wol.wol.WolSender.getDeviceInfo
import java.net.SocketException
import java.net.UnknownHostException

/**
 *Author:viosonlee
 *Date:2023/2/7
 *DESCRIPTION:
 */
object WolSender {
    const val setTagOptionID = 19

    private const val bindAddressKey = "bindAddress"
    private const val bindIpKey = "bindIp"
    private const val bindPortKey = "bindPort"
    private const val lastTargetKey = "lastTarget"

    fun getLastTarget(context: Context): String? {
        return context.sp().getString(lastTargetKey, null)
    }

    fun setLastTarget(context: Context, macAddress: String) {
        context.sp {
            edit {
                putString(lastTargetKey, macAddress)
            }
        }
    }

    var bindDeviceCallback: ((success: Boolean) -> Unit)? = null

    fun onSetTag(success: Boolean) {
        bindDeviceCallback?.invoke(success)
    }

    /**
     * 绑定设备
     */
    fun bindDevice(context: Context, macAddress: String, ip: String, port: Int? = 9) {
        context.sp {
            edit(true) {
                putString(bindAddressKey, macAddress)
                putString(bindIpKey, ip)
                putInt(bindPortKey, port ?: 9)
            }
        }
    }

    /**
     * 获取已绑定的设备信息
     */
    fun getDeviceInfo(context: Context): Triple<String, String, Int> {
        val sp = context.sp()
        val macAddress = sp.getString(bindAddressKey, "") ?: ""
        val ip = sp.getString(bindIpKey, "") ?: ""
        val port = sp.getInt(bindPortKey, 9)
        return Triple(macAddress, ip, port)
    }

    /**
     *  发送唤醒指令
     */
    fun sendWol(context: Context?, onResult: (success: Boolean, msg: String) -> Unit) {
        if (context == null) {
            onResult.invoke(false, "context = null")
            return
        }
        val deviceInfo = getDeviceInfo(context)
        try {
            if (deviceInfo.first.isNotEmpty()) {
                MagicPacket.send(deviceInfo.first, deviceInfo.second, deviceInfo.third)
                onResult.invoke(true, "指令发送成功")
                setLastTarget(context, deviceInfo.first)
            } else {
                onResult.invoke(false, "mac 地址不能为空")
            }
        } catch (e: UnknownHostException) {
            onResult.invoke(false, "host错误")
        } catch (e: SocketException) {
            onResult.invoke(false, "socket 连接错误")
        } catch (e: Exception) {
            onResult.invoke(false, e.localizedMessage ?: "发送指令失败")
        }
    }
}
package lee.maitarou.wol.ui.page

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cn.jpush.android.api.JPushInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lee.maitarou.wol.R
import lee.maitarou.wol.alias
import lee.maitarou.wol.wol.WolSender

@Composable
fun ReceiverUi() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var macAddress by remember { mutableStateOf("") }
    var ip by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("9") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var currentDevice by remember { mutableStateOf("当前没有绑定的唤醒对象") }

        LaunchedEffect(key1 = Unit, block = {
            val deviceInfo = WolSender.getDeviceInfo(context)
            currentDevice = if (deviceInfo.first.isNotEmpty()) {
                macAddress = deviceInfo.first
                ip = deviceInfo.second
                port = deviceInfo.third.toString()
                //设置极光推送的tag
                JPushInterface.setAlias(context, 0, macAddress.alias)
                "当前绑定对象：\n${deviceInfo.first};${deviceInfo.second}:${deviceInfo.third}"
            } else {
                "当前没有绑定的唤醒对象"
            }
        })
        Text(text = currentDevice)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = macAddress, onValueChange = { macAddress = it },
            placeholder = {
                Text(text = stringResource(id = R.string.target_pc_mac_address))
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = ip, onValueChange = { ip = it },
            placeholder = {
                Text(text = stringResource(id = R.string.target_pc_ip))
            }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = port, onValueChange = { port = it },
            placeholder = {
                Text(text = stringResource(id = R.string.target_pc_port))
            }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(enabled = macAddress.isNotEmpty() && ip.isNotEmpty() && port.isNotEmpty(),
            onClick = {
                WolSender.bindDeviceCallback = {
                    if (it) {
                        //绑定网卡
                        WolSender.bindDevice(context, macAddress, ip, port.toIntOrNull())
                        currentDevice = "当前绑定对象：\n${macAddress};${ip}:${port}"
                        scope.launch(Dispatchers.Main) {
                            Toast.makeText(context, "绑定成功", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        scope.launch(Dispatchers.Main) {
                            Toast.makeText(context, "绑定失败：推送别名设置失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                //设置极光推送的tag
                JPushInterface.setAlias(context, WolSender.setTagOptionID, macAddress.alias)
            }) {
            Text(text = stringResource(id = R.string.bind))
        }
    }
}


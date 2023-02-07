package lee.maitarou.wol.ui.page

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lee.maitarou.wol.R
import lee.maitarou.wol.push.PushRequest
import lee.maitarou.wol.utils.PushConfig
import lee.maitarou.wol.utils.pushApiService
import lee.maitarou.wol.wol.WolSender

@Composable
fun SenderUi() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //根据mac地址将推送发出去，接收推送端将要唤醒的mac设置为tag，即可收到推送
        var macAddress by remember { mutableStateOf("") }
        LaunchedEffect(key1 = Unit, block = {
            macAddress = WolSender.getLastTarget(context) ?: ""
        })
        OutlinedTextField(value = macAddress, onValueChange = { macAddress = it }, placeholder = {
            Text(text = stringResource(id = R.string.target_pc_mac_address))
        })
        Spacer(modifier = Modifier.height(16.dp))
        Button(enabled = macAddress.isNotEmpty(), onClick = {
            //发送极光推送，接收端接收到后发送wol通知
            pushApiService {
                scope.launch {
                    val request = PushRequest.wolPush(macAddress)
                    val result = push(PushConfig.secret, request)
                    if (result.isSuccessful) {
                        WolSender.setLastTarget(context, macAddress)
                        //发送成功
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }) {
            Text(text = stringResource(id = R.string.send_wol_message))
        }
    }
}

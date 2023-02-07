package lee.maitarou.wol.push

/**
 *Author:viosonlee
 *Date:2023/2/7
 *DESCRIPTION: 极光推送内容
 */
data class PushRequest(
    val audience: HashMap<String, Array<String>>,
    val message: Message,
    val platform: String = "all"
) {
    companion object {
        fun wolPush(macAddress: String): PushRequest {
            val audience = hashMapOf(
                "alias" to arrayOf(macAddress)
            )
            return PushRequest(
                audience,
                Message(macAddress, "text", "wol", hashMapOf())
            )
        }
    }
}

data class Message(
    val msg_content: String,
    val content_type: String,
    val title: String,
    val extras: HashMap<String, String>
)
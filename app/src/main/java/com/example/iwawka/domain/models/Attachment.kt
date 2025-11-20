import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val id: String,
    val type: AttachmentType,
    val url: String,
    val name: String? = null,
    val size: Long? = null,
    val duration: Long? = null
)

enum class AttachmentType { IMAGE, VIDEO, AUDIO, FILE }
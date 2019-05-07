package hoonstudio.com.tutory.data.RoomDB.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "lyric_table")
data class LyricEntry(
    @PrimaryKey(autoGenerate = true)
    val id : Long?,
    @SerializedName("backlink_url")
    val backlinkUrl: String,
    val instrumental: Int,
    @SerializedName("lyrics_body")
    val lyricsBody: String,
    @SerializedName("lyrics_copyright")
    val lyricsCopyright: String,
    @SerializedName("lyrics_id")
    val lyricsId: Int,
    @SerializedName("lyrics_language")
    val lyricsLanguage: String,
    @SerializedName("pixel_tracking_url")
    val pixelTrackingUrl: String,
    val restricted: Int,
    @SerializedName("script_tracking_url")
    val scriptTrackingUrl: String,
    @SerializedName("updated_time")
    val updatedTime: String
)
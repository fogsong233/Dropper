package top.fogsong.dropper

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

/**
 * @author：FogSong
 * @time：2023-02-03  9:34
 */
class DroppableRef<T>(
    private val callBack: DroppableEventCallBack<T>,
    val dropper: Dropper<T>
) {
    internal val id = ++nowNum
    internal var size: IntSize = IntSize.Zero
    internal var location: Offset = Offset.Unspecified

    private companion object {
        var nowNum = 0
    }

    fun onEnter(name: String, info: T) {
        callBack.onEnter(name, info)
    }

    fun onLeave(name: String) {
        callBack.onLeave(name)
    }

    fun onDrop(name: String, info: T) {
        callBack.onDrop(name, info)
    }
}
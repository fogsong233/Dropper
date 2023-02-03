package top.fogsong.dropper

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

/**
 * @author：FogSong
 * @time：2023-02-03  8:21
 */
class DraggableRef<T> internal constructor(
    val name: String,
    val info: T,
    private val callBack: DraggableEventCallBack,
    val dropper: Dropper<T>
){
    internal var size: IntSize = IntSize.Zero
    internal var location: Offset = Offset.Unspecified
    var totalOffset = mutableStateOf(Offset.Zero)
    private set

    internal fun onDrag(offset: Offset) {
        totalOffset.value += offset
        callBack.onDrag(offset)
        dropper.checkInDroppable(this)
    }

    internal fun onDragStart(offset: Offset) {
        totalOffset.value += offset
        callBack.onDragStart(offset)
    }

    private fun onDropped() {
        callBack.onDropped()
        totalOffset.value = Offset.Zero
    }

    internal fun onCancelled() {
        callBack.onCancelled()
        totalOffset.value = Offset.Zero
    }

    // 判断当drag结束后是否可以drop
    internal fun checkIsDrop() {
        dropper.checkInDroppable(this).let { list ->
            if (list.isNotEmpty()) {
                onDropped()
                list.forEach {
                    dropper.dropIn(it, this)
                }
            } else {
                onCancelled()
            }
        }
    }
}
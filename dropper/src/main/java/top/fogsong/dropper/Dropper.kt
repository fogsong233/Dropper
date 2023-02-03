package top.fogsong.dropper

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.abs

/**
 * @author：FogSong
 * @time：2023-02-03  8:14
 */
class Dropper<T> {

    private val droppableList = mutableListOf<DroppableRef<T>>()
    private var previousEnteredDroppableList = listOf<DroppableRef<T>>()

    fun createDraggableRef(name: String, info: T, callBack: DraggableEventCallBack) =
        DraggableRef(name, info, callBack, this)

    fun createDroppableRef(callBack: DroppableEventCallBack<T>) =
        DroppableRef(callBack, this).apply {
            droppableList.add(this)
        }

    // 当在droppable里时，返回id，否则返回-1
    internal fun checkInDroppable(draggableRef: DraggableRef<T>): List<Int> = droppableList.filter { droppableRef ->
        val center1 = draggableRef.location + draggableRef.size.run {
            Offset(width.toFloat(), height.toFloat()) / 2F
        }
        val center2 = droppableRef.location + droppableRef.size.run {
            Offset(width.toFloat(), height.toFloat()) / 2F
        }
        val limitWidth = (draggableRef.size.width + droppableRef.size.width) / 2
        val limitHeight = (draggableRef.size.height + droppableRef.size.height) / 2
        (center1 - center2).let {
            val gap = Size(width = abs(it.x), height = abs(it.y))
            return@filter gap.width < limitWidth && gap.height < limitHeight
        }
    }.apply {
        // 加入onEnter
        val newDroppableList = this.filterNot { it in previousEnteredDroppableList }
        newDroppableList.forEach {
            it.onEnter(draggableRef.name, draggableRef.info)
        }
        // 对移出的drop实施onLeave
        val leftDroppableList = previousEnteredDroppableList.filterNot { it in this }
        leftDroppableList.forEach { it.onLeave(draggableRef.name) }
        previousEnteredDroppableList = this
    }.map { it.id }

    internal fun dropIn(droppableId: Int, draggableRef: DraggableRef<T>) {
        val droppable = droppableList.first { it.id == droppableId }
        droppable.onDrop(draggableRef.name, draggableRef.info)
    }
}
package top.fogsong.dropper

data class DroppableEventCallBack<T>(
    val onEnter: (name: String, info: T) -> Unit = {_, _ ->},
    val onLeave: (name: String) -> Unit = {},
    val onDrop: (name: String, info: T) -> Unit
)

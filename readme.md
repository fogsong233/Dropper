# Dropper
##### An easy Drop And Drag Api provider for Jetpack Compose.
### Usage
first, create a new `Dropper` instance, and you can create `DroppableRef` and `DraggableRef`
```
// <String> is which type of addition info the draggable provides to droppable with
val dropper by remember { mutableStateOf(Dropper<String>()) }
```
Now you can create refs. Info is the addition type, `Dropper` can automatically check the position of draggable and invoke the callback.
```
val dropRef = dropper.createDroppableRef(DroppableEventCallBack(
                onEnter = { name, info ->
                    Log.d(tag, "enter: $name, info: $info")
                },
                onDrop = { name, info ->
                    Log.d(tag, "drop: $name, info: $info")
                },
                onLeave = { name ->
                    Log.d(tag, "leave: $name")
                }
            ))
```
then create draggable:
```
val dragRef = dropper.createDraggableRef("name", "info: T", DraggableEventCallBack(
    // where you can listen drop, drag, dragStart, dragCancelled events.
    onDrop = {}
    ...
))
```
Now bind them to component:
```
Box(modifier = Modifier.draggable(dragRef, afterLongPress = true/false))
Box( modifier = Modifier.droppable(dropRef))
```
#### Custom Offset Behavior
The offset of draggable can be processed by `Dragger`, but if you intend to add animation to offset, for example, when the `draggable` isn't drop in a droppable, and `onCancelled` is invoked, you can write like that:
```
val offset = remember {
                Animatable(Offset.Zero, Offset.VectorConverter)
            }
            val scope = rememberCoroutineScope()
            val dragRef = dropper.createDraggableRef("name", "info", DraggableEventCallBack(
                onDrag = {
                    scope.launch { offset.snapTo(offset.value + it) }
                },
                onDragStart = {
                    scope.launch { offset.snapTo(offset.value + it) }
                },
                onDropped = {
                    scope.launch { offset.snapTo(Offset.Zero) }
                },
                onCancelled = {
                    scope.launch { offset.animateTo(Offset.Zero) }
                }
            ))
Box(modifier = Modifier.offset {
                        offset.value.let {
                            IntOffset(x = it.x.roundToInt(), y = it.y.roundToInt())
                        }
                }.draggable(dragRef, isSetOffset = false))
```
You can also do other transform like scale.
__But `Dropper` doesn't surpport rotation, as the size and location of `Draggable` may get wrong.
#### Multiple Draggable and Droppable
`Dropper` surpports lots of `Draggables` and `Droppables`. However, each of thems belong to one `Dropper` must have a common addition info type. It is the **T** in `Dropper<T>()`.  
`Dropper` can automatically process every event to `Draggable` and `Droppable`.
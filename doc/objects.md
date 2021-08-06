# Objects and methods

Methods are executed on objects like this:

```
someList = arrayList(:number)
someList.size()
```

## Shape

See [Drawings and Shapes](functions.md#drawings-and-shapes) on how to create shapes.

All the setters return the shape itself so they can be chained:

```
setPosX(posX: number): Shape
setPosY(posY: number): Shape
setScaleX(scaleX: number): Shape
setScaleY(scaleY: number): Shape
setRotationDeg(rotationDeg: number): Shape
setSpeedPerSecondX(speedX: number): Shape
setSpeedPerSecondY(speedY: number): Shape
setAccelerationPerSecondX(accelerationX: number): Shape
setAccelerationPerSecondY(accelerationY: number): Shape
setRotationDegSpeedPerSecond(rotationSpeed: number): Shape
setColorBackground(): Shape // Set the color to background color
setColorForeground(): Shape // Set the color to foreground color (default)
```

The properties can also be retrieved:
```
getPosX(): number
getPosY(): number
getScaleX(): number
getScaleY(): number
getRotationDeg(): number
getSpeedPerSecondX(): number
getSpeedPerSecondY(): number
getAccelerationPerSecondX(): number
getAccelerationPerSecondY(): number
getRotationDegSpeedPerSecond(): number
```

You can also add children to a shape, see [Drawing shapes](language.md#drawing-shapes) for more information.
```
addChild(child: Shape): Shape // returns the shape you invoked the method on, not the child.
```

Shapes that are drawn are automatically updated according to the speed, acceleration etc. If
you need to keep a shape animation going even if it isn't drawn, you can call `update()`.
```
update()
```

## List

See [Array List](functions.md#array-list) and [Linked List](functions.md#linked-list) on how to
create a list. The elements in the list can be any type, when referencing the inner type, `T` is used.

```
// returns null if the index is outside the list range
get(index: number): T  

// Replaces the element at the given position with the given element. Does nothing if the index is out of range.
set(index: number, element: T)   

// Adds an element to the end of the list
append(element: T)

// Returns the number of elements in the list
size(): number

// Pushes the element to the start of the list. Bad performance on Array List
push(element: T)

// Removes the first element and returns it, or null if the list is empty. Bad performance on Array List
pop(): T

// Returns the first element in the list, or null if it's empty
peek(): T

// Pushes the element to the end of the list.
pushLast(element: T)

// Removes the last element and returns it, or null if the list is empty.
popLast(): T

// Returns the last element in the list, or null if it's empty
peekLast(): T

// Remove the first occurance of the specified element from the list. Returns true if it existed and false if it didn't.
remove(elem: T): bool

// Remove the element at the specified position and return it. Returns null if the index was out of range.
removeAt(index: number): T

// Adds all items of the specified collection to this list.
addAll(items: Iterable<T>)

// Remove all items of the specified collection to this list.
removeAll(items: Iterable<T>)
```

## HashSet

See [Hash Set](functions.md#hash-set) on how to create a HashSet. The elements in the set 
can be any type, when referencing the inner type, `T` is used.

```
// Ads an element to the set. Returns true if it's a new element.
add(elem: T): bool

// Returns the size of the set
size(): number

// Remove the specified element from the set. Returns true if it contained the element.
remove(elem: T): bool

// Returns true if the element is in the set.
contains(elem: T): bool

// Adds all items of the specified collection to the set
addAll(elements: Iterable<T>)

// Removes all items of the specified collection from the set
removeAll(elements: Iterable<T>)
```

## TreeSet

See [Tree Set](functions.md#tree-set) on how to create a TreeSet. The elements in the set 
can be any type, when referencing the inner type, `T` is used.

```  
// Ads an element to the set. Returns true if it's a new element.
add(elem: T): bool

// Returns the size of the set
size(): number

// Remove the specified element from the set. Returns true if it contained the element.
remove(elem: T): bool

// Returns true if the element is in the set.
contains(elem: T): bool

// Get the first element in the set, or null if the set is empty.
first(): T

// Get the last element in the set, or null if the set is empty.
last(): T

// Returns the element after the specified element, or null if no such element was found.
next(elem: T): T

// Returns the element before the specified element, or null if no such element was found.
previous(elem: T): T

// Adds all items of the specified collection to the set
addAll(elements: Iterable<T>)

// Removes all items of the specified collection from the set
removeAll(elements: Iterable<T>)
```

## IndexedHashSet

See [Indexed Hash Set](functions.md#indexed-hash-set) on how to create a IndexedHashSet. The elements in the set 
can be any type, when referencing the inner type, `T` is used.

```
// Ads an element to the set. Returns true if it's a new element.
add(elem: T): bool

// Returns the size of the set
size(): number

// Remove the specified element from the set. Returns true if it contained the element.
remove(elem: T): bool

// Returns true if the element is in the set.
contains(elem: T): bool

// Gets an element at an arbitrary position. Usually used with rand: set.get(rand(0, set.size() - 1))
get(index: number): T

// Removes and returns an element at an arbitrary position. Usually used with rand: set.removeAt(rand(0, set.size() - 1))
removeAt(index: number): T

// Adds all items of the specified collection to the set
addAll(elements: Iterable<T>)

// Removes all items of the specified collection from the set
removeAll(elements: Iterable<T>)
```

## HashMap

See [Hash Map](functions.md#hash-map) on how to create a HashMap. The key type is referenced as `K`
and the value type is referenced as `V`.

```
// Returns the size of the map
size(): number

// Returns the value assoicated with the given key, or null if no such key existed.
get(key: K): V

// Returns the value assoicated with the given key, or the specified default if no such key existed.
getOrDefault(key: K, default: V): V

// Add a key to the map with the associated value. If the key existed, the previous value is remove and returned. If it didn't exist, null is returned
put(key: K, value: V): V

// Returns true if the set contains the given key
containsKey(key: K): bool

// Remove the specified key and return the associated value, or null if it didn't exist.
removeKey(key: K): V

// Remove the specified key if the given value is the same as the stored value.
remove(key: K, value: V): bool

// Returns a list of all values in this map.
values(): List<V>

// Returns an HashSet with all the keys in this map
keys(): HashSet<K>

// Put all entries in the given map to this map
putAll(entries: Iterable<MapEntry<K,V>>)

// Remove all entries in the given map from this map if the values also match.
removeAll(entries: Iterable<MapEntry<K,V>>)

// Remove all key in the given collection from this map
removeAllKeys(keysToRemove: Iterable<K>)
```

## TreeMap

See [Hash Map](functions.md#hash-map) on how to create a HashMap. The key type is referenced as `K`
and the value type is referenced as `V`.

```

// Returns the size of the map
size(): number

// Returns the value assoicated with the given key, or null if no such key existed.
get(key: K): V

// Returns the value assoicated with the given key, or the specified default if no such key existed.
getOrDefault(key: K, default: V): V

// Add a key to the map with the associated value. If the key existed, the previous value is remove and returned. If it didn't exist, null is returned
put(key: K, value: V): V

// Returns true if the set contains the given key
containsKey(key: K): bool

// Remove the specified key and return the associated value, or null if it didn't exist.
removeKey(key: K): V

// Remove the specified key if the given value is the same as the stored value.
remove(key: K, value: V): bool

// Returns the key after the specified key, or null.
nextKey(key: K): K

// Returns the key before the specified key, or null.
previousKey(key: K): K

// Returns the entry for the key after the specified key, or null.
nextEntry(key: K): MapEntry<K, V>

// Returns the entry for the key before the specified key, or null.
previousEntry(key: K): MapEntry<K, V>

// Returns the first key in the map, or null if it's empty
firstKey(): K

// Returns the last key in the map, or null if it's empty
lastKey(): K

// Returns the first entry in the map, or null if it's empty
firstEntry(): MapEntry<K, V>

// Returns the last entry in the map, or null if it's empty
lastEntry(): MapEntry<K, V>

// Returns a list of all values in this map.
values(): List<V>

// Returns an HashSet with all the keys in this map
keys(): HashSet<K>

// Put all entries in the given map to this map
putAll(entries: Iterable<MapEntry<K,V>>)

// Remove all entries in the given map from this map if the values also match.
removeAll(entries: Iterable<MapEntry<K,V>>)

// Remove all key in the given collection from this map
removeAllKeys(keysToRemove: Iterable<K>)
```

## MapEntry

MapEntry is a key-value pair most often used with maps. Iterating over maps will
give you MapEntry objects. Maps can also be created using MapEntry. See [Map Entry](functions.md#map-entry)
on how to create it.

```
getKey(): K
getValue(): V
```
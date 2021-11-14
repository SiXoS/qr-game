# Built-in functions

## Drawings and shapes

### Draw

Draws the shape passed to the function.

```
draw(shape: Shape)
```

### Simple shapes

[Object methods](objects.md#shape)

Creates a shape with the given dimensions at the given position. The position is the center of the shape.

```
createRectangle(posX: number, posY: number, width: number, height: number): Shape
createEllipse(posX: number, posY: number, width: number, height: number): Shape
createTriangle(posX: number, posY: number, width: number, height: number): Shape
```

### Composite shape

[Object methods](objects.md#shape)

A composite shape allows you to connect multiple shapes into one. The position, 
rotation and scale of the children are in relation to parent composite shape. Scaling and
rotating the composite shape will transform the child shapes as one unit. When using composite shapes
you must pass the composite to the `draw()` function, not the children.

```
createCompositeShape(posX: number, posY: number, children: List<Shape>): Shape
```

## Miscellaneous

### Random number

Returns a random decimal number between `min` inclusive and `max` inclusive.

```
rand(min: number, max: number): number
```

### Time

Returns the number of seconds passed since the game started.

```
time(): number
```

### Time diff

Returns the number of seconds since the last frame.

```
timeDiff(): number
```

### Assert

Mainly used in tests but could be useful when debugging. If the `condition` is `false` it
will cause a fatal exception. The exception message will contain `id`.

```
assert(id: number, condition: bool): number
```

## Game state

### Get score

Get the current score.

```
getScore(): number
```

### Set score

Set the score to the given value. High score is not updated until the end of the game.

```
setScore(score: number)
```

### Modify score

Add the `scoreIncrement` parameter to the current score.

```
setScore(scoreIncrement: number)
```

### Won

Ends the game, tracks the high score and displays a game over message to the user.

```
won()
```

### Lost

If the player has lost, you can call `lost(trackHighscore)`. The parameter `trackHighscore` is a boolean.
If it's set to `true`, the current score will be evaluated for high score. This is useful for games like Tetris
where a loss is expected. If a loss should not contribute to the high score, pass `false`. The game will
end and show a message to the user.

```
lost(trackHighscore: bool)
```

## Data structures

Datastructures can contain values of any type. Types are passed as a colon and a type, for example:

```
arrayList(:number)
```

Datastructure functions can also take an arbitrary amount of parameters which will be the elements of the
data structure. You will see these parameters as types of `T...`. 

### Hash Set

[Object methods](objects.md#hashset)

A set is a collection of unique items.

Hash Set pros: Very fast. All operations on single elements are O(1)
Hash Set con: The order of elements is arbitrary.

```
hashSet(type: Type<T>): HashSet<T>
hashSet(values: T...): HashSet<T>
```

### Indexed Hash Set

[Object methods](objects.md#indexedhashset)

A set is a collection of unique items.

This has all the pros and cons of Hash Set with one extra feature. It allows retrieving/removing
items by position. The position is deterministic but arbitrary, it is not related to the value of the item.
If the list is changed, item's position may also change. Main usage is fetching random elements from the
set, as in: `indexedHashSet.get(rand(0, indexedHashSet.size() - 1))`.

It has some overhead compared to a plain Hash Set so this should only be used if you need the above feature.

```
indexedHashSet(type: Type<T>): IndexedHashSet<T>
indexedHashSet(values: T...): IndexedHashSet<T>
```

### Tree Set

[Object methods](objects.md#treeset)

A set is a collection of unique items.

Tree Set pros: Keeps the element sorted.<br/>
Tree Set cons: Hash Set is faster for most operations.

```
treeSet(type: Type<T>): TreeSet<T>
treeSet(values: T...): TreeSet<T>
treeSet(comparator: fun (T, T) -> number): TreeSet<T>
```

### Array List

[Object methods](objects.md#list)

A list is a collection of items where the order depends on order of insertion.

Array List pros:
1. Fast at accessing or replacing elements at arbitrary positions in the list.
2. Efficient for large number of elements.

Array List cons:
1. Very slow at removing/inserting elements at the start of the list.

**Note:** Using the sized constructor can greatly improve performance on large lists.

```
arrayList(type: Type<T>): List<T>
arrayList(type: Type<T>, initialSize: number): List<T>
arrayList(values: T...): List<T>
```

### Linked List

[Object methods](objects.md#list)

A list is a collection of items where the order depends on order of insertion.

Linked List pros: 
1. Fast at removing/inserting elements at the start of the list.

Linked List cons:
1. Slow at accessing or replacing elements at arbitrary positions in the list.
2. Inefficient for large number of elements.

```
linkedList(type: Type<T>): List<T>
linkedList(type: Type<T>, initialSize: number): List<T>
linkedList(values: T...): List<T>
```

### Hash Map

[Object methods](objects.md#hashmap)

A map is a key-value store.

Hash map pros:
1. Very fast at inserting/retrieving elements

Hash map cons:
1. The keys are not sorted and the order is arbitrary.

```
hashMap(keyType: Type<K>, valueType: Type<V>): HashMap<K, V>
hashMap(entries: MapEntry<K, V>...): HashMap<K, V>
```

### Tree Map

[Object methods](objects.md#treemap)

A map is a key-value store.

Tree map pros:
1. The keys are sorted in ascending order.

Hash map cons:
1. Slower than Hash Map at inserting/retrieving elements but still fast.

```
treeMap(keyType: Type<K>, valueType: Type<V>): TreeMap<K, V>
treeMap(entries: MapEntry<K, V>...): TreeMap<K, V>
treeMap(comparator: fun (K, K) -> number, valueType: Type<V>): TreeMap<K, V>
```

### Map Entry

[Object methods](objects.md#mapentry)

A key-value pair often used with maps. When iterating over maps the element will be 
a map entry and when constructing maps, mapEntries can be used as the initial values.

```
mapEntry(key: K, value: V): MapEntry<K, V>
```

## Math

Here is a list of all math functions and comments for any unintuitive or hidden behaviors.

```
abs(value: number): number // positive value
aCos(value: number): number // returns radians
aSin(value: number): number // returns radians
aTan(value: number): number // returns radians
ceil(value: number): number // round up
cos(radians: number): number
degToRad(degrees: number): number
e(): number // The constant e
exp(value: number): number // e^value
floor(value: number): number // round down
intDiv(numerator: number, denominator: number): number
intMod(numerator: number, denominator: number): number
log10(value: number): number
log(base: number, value: number): number
logn(value: number): number
max(values: number...): number
min(values: number...): number
pi(): number
pow(base: number, power: number): number // base^power
radToDeg(radians: number): number
round(number: number): number
sin(radians: number): number
sqrt(value: number): number // Square root
tan(radians: number): number
```

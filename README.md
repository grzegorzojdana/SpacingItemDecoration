# SpacingItemDecoration

[![release](https://jitpack.io/v/grzegorzojdana/SpacingItemDecoration.svg)](https://jitpack.io/#grzegorzojdana/SpacingItemDecoration)

ItemDecoration for RecyclerView that allows you to set spacing between and around list items in flexible way.

<img src="https://raw.githubusercontent.com/grzegorzojdana/spacingitemdecoration/master/art/staggered.png" height=500> <img src="https://raw.githubusercontent.com/grzegorzojdana/spacingitemdecoration/master/art/sample.gif" height=500>

## How to install

Add to your root `build.gradle`:
```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency to your project's `build.gradle`:
```groovy
dependencies {
    implementation 'com.github.grzegorzojdana:SpacingItemDecoration:1.1.0'
}
```

## How to use

Decoration with specified _Spacing_ can be created and added to RecyclerView like this:
```kotlin
val spacingItemDecoration = SpacingItemDecoration(Spacing(
        // values in pixels are expected
        horizontal = resources.getDimensionPixelSize(R.dimen.spacing_horizontal),
        vertical = resources.dpToPx(16F).toInt(),
        edges = Rect(0, top, 0, 0)
))
list.addItemDecoration(spacingItemDecoration)
```

There are several spacing modifiers:
* `horizontal` and `vertical` are the gaps between each two items.
* `item` rectangle defines inset for each item (similar to item padding).
* `edges` rectangle means offsets from the parent (RecyclerView) edges (similar to RecyclerView padding).

<img src="https://raw.githubusercontent.com/grzegorzojdana/spacingitemdecoration/master/art/grid.png" height=500> <img src="https://raw.githubusercontent.com/grzegorzojdana/spacingitemdecoration/master/art/grid-draw-with-legend.png" height=500>  

This library doesn't modify padding of any view, but calculates item offsets basing on given parameters.   

Spacing can be easily modified:
```kotlin
spacingItemDecoration.spacing.apply {
    vertical = newVerticalSpacing
    edges.setEmpty()
}
// when modifying spacing properties, need to call invalidateSpacing()
spacingItemDecoration.invalidateSpacing()
// your's RecyclerView needs to know
list.invalidateItemDecorations()
```
If you change Spacing instance, you don't need to call _invalidateSpacing_.
```kotlin
val vItemSpacing = ...
spacingItemDecoration.spacing = Spacing(item = Rect(0, vItemSpacing, 0, vItemSpacing))
list.invalidateItemDecorations()
```

To see how different spacing values impact list layout, run sample app from this repo and play with configuration controls.

_SpacingItemDecoration_ can also draw determined spacing, which is useful for debugging.
```kotlin
spacingItemDecoration.isSpacingDrawingEnabled = true
// you can change default colors used to mark specific spacing
spacingItemDecoration.drawingConfig = DrawingConfig(horizontalColor = Color.MAGENTA)
// if your decoration is already in use (items have been laid out), invalidate decor
list.invalidateItemDecorations()
```

## Caveats

From the fact how this library works (providing item offsets with desired layout spacing without changing number of list rows and columns), list items will in result be laid out smaller than without this decoration, because some spacing is brought uniformly from each item's width and height. 

This library works best if RecyclerView items have set one of the dimension to `MATCH_PARENT` (width if orientation is `VERTICAL`, and height if orientations is `HORIZONTAL`). Otherwise, you can see that when using GridLayoutManager the `item.bottom` or `item.right` spacing could not work.

`StaggeredLayoutManager` is not currently fully supported. For `VERTICAL` orientation, `vertical` spacing won't work, and `edges.top` and `edges.bottom` spacings will behave like `item.top` and `item.bottom` spacings. Similar, for `HORIZONTAL` orientation, `horizontal` spacing, `edges.left` and `edges.right` are broken. However, it is planned to be fixed in some future release.


## Performance tips

If you use GridLayoutManager with list of huge number of items (thousands), you might would like to try this tips:
* Set `spacingItemDecoration.isGroupCountCacheEnabled` to `true`. This will make determined group count be held by decoration implementation, but you will need to call `invalidate()` method each time the number of items or the properties of layout manager changes (orientation, span count, span size lookup object or items span size).
* If you use non-default implementation of _SpanSizeLookup_ but its `getSpanSize(position)` method always returns `1`, you might want to set `spacingItemDecoration.hintSpanSizeAlwaysOne` to `true`. You may also consider [enable span indices caching](https://developer.android.com/reference/android/support/v7/widget/GridLayoutManager.SpanSizeLookup.html#setSpanIndexCacheEnabled(boolean)).

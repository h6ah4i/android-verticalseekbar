VerticalSeekBar
===============

Vertical SeekBar class which supports Android 2.3 - 5.0.

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-VerticalSeekBar-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1436)

---

<a href="./pic/gb.png?raw=true"><img src="./pic/gb.png?raw=true" alt="Example on Android 2.3" width="200" /></a>
<a href="./pic/ics.png?raw=true"><img src="./pic/ics.png?raw=true" alt="Example on Android 4.0" width="200" /></a>
<a href="./pic/lollipop.png?raw=true"><img src="./pic/lollipop.png?raw=true" alt="Example on Android 5.0" width="200" /></a>

---

Target platforms
---

- from Android 2.3.x  (Gingerbread)
- to Android 5.0.x (Lollipop)


Latest version
---

- Version 0.5.1  (January 12, 2015)


Getting started
---

This library is published on jCenter. Just add these lines to `build.gradle`.

```groovy
dependencies {
    compile 'com.h6ah4i.android.widget.verticalseekbar:verticalseekbar:0.5.1'
}
```

Usage
---

### Layout XML

```xml
<!-- This library requires pair of the VerticalSeekBar and VerticalSeekBarWrapper classes -->
<com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBarWrapper
    android:layout_width="wrap_content"
    android:layout_height="150dp">
    <com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar
        android:id="@+id/mySeekBar"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:seekBarRotation="CW90" /> <!-- Rotation: CW90 or CW270 -->
</com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBarWrapper>
```

### Java code

```java
SeekBar seekBar = (SeekBar) findViewById(R.id.mySeekBar);
...
```

License
---

This library is licensed under the [Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0), and contains some source code files delivered from product of Android Open Source Project (AOSP).

See [`LICENSE`](LICENSE) for full of the license text.

    Copyright (C) 2015 Haruki Hasegawa

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

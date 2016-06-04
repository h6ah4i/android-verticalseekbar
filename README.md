VerticalSeekBar
===============

Vertical SeekBar class which supports Android 2.3 - 6.0.

[ ![Download](https://api.bintray.com/packages/h6ah4i/maven/android-verticalseekbar/images/download.svg) ](https://bintray.com/h6ah4i/maven/android-verticalseekbar/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-VerticalSeekBar-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1436)

---

### Screenshots
| Gingerbread |    ICS    | Lollipop   | Marshmallow |
|-------------|-----------|------------|-------------|
| <img src="./pic/gb.png?raw=true" alt="Example on Android 2.3" width="150" /> | <img src="./pic/ics.png?raw=true" alt="Example on Android 4.0" width="150" /> | <img src="./pic/lollipop.png?raw=true" alt="Example on Android 5.0" width="150" /> | <img src="./pic/marshmallow.png?raw=true" alt="Example on Android 5.0" width="150" /> |

### Download the demo app on Google Play
<a href="https://play.google.com/store/apps/details?id=com.h6ah4i.android.example.verticalseekbar&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="48" /></a>

---

Target platforms
---

- from Android 2.3.x  (Gingerbread)
- to Android 6.0.x (Marshmallow)


Latest version
---

- Version 0.6.0  (December 13, 2015)



Getting started
---

This library is published on jCenter. Just add these lines to `build.gradle`.

```groovy
dependencies {
    compile 'com.h6ah4i.android.widget.verticalseekbar:verticalseekbar:0.6.0'
}
```

### Notice
Since v0.6.0 `VerticalSeekBar` has changed to extend `AppCompatSeekBar` from support library. If you want to use platform native SeekBar, please use v0.5.2 instead.


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
        android:splitTrack="false"
        app:seekBarRotation="CW90" /> <!-- Rotation: CW90 or CW270 -->
</com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBarWrapper>
```

**NOTE: `android:splitTrack="false"` is required for Android N.**

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

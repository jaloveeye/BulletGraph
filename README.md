# BulletGraph

 
This repository contains `BulletGraph` that provides Bullet Graph Widget for android

<img src="images/sample.png"/>

## Features

- Configure 3 different types of Pin Field Views to your app.
- Allow your users to paste the characters into your Pin Field which no other library provides
- Support for showing hint in your Pin Fields
- Customize the number of fields you will be requiring.
- Highlight in 4 ways - All the fields, Only Current Field, All Completed Fields, None of the fields.
- Enable or disable cursor to your Pin Fields.
- Curve the edges in your Square Pin Field view.
- Customize the distance between your Pin Fields.
- Set custom distance between your line and text Line Pin Field.
- Keep the keyboard open or closed after the Text is entered.
- Mask your passwords properly.
- Use any type of keyboard you would like for the View.
- Highly configurable with many attributes for your View.

## Setup

**Gradle**

- **Project level `build.gradle`**
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
- **App level `build.gradle`**
```gradle
	dependencies {
	        implementation 'com.github.jaloveeye:BulletGraph:1:0:2'
	}
```

### Usage

```xml
    <com.herace.bulletgraph.BulletGraph
        android:id="@+id/bullet_graph_1"
        android:layout_width="300dp"
        android:layout_height="100dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:noOfFields="5"
        app:bgColor="#FFFFFF"
        app:title="Title"
        app:isWarning="false"
        app:value="120"
        app:isASC="false"
        app:label_1="@array/graphA_LabelA"
        app:label_2="@array/graphA_LabelB"
        app:range="@array/graphA_Range"
    />
```


### Array
```xml
<string-array name="graphA_LabelA">
        <item></item>
        <item>정상</item>
        <item>경도</item>
        <item>중등도</item>
        <item>중증</item>
        <item>말기</item>
    </string-array>

    <string-array name="graphA_LabelB">
        <item></item>
        <item>90</item>
        <item>60</item>
        <item>30</item>
        <item>15</item>
        <item></item>
    </string-array>

    <!-- for calculate marker position -->
    <string-array name="graphA_Range">
        <item>200</item>
        <item>90</item>
        <item>60</item>
        <item>30</item>
        <item>15</item>
        <item>0</item>
    </string-array>

```

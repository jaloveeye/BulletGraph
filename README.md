# BulletGraph

 
This repository contains `BulletGraph` that provides Bullet Graph Widget for android

<img src="images/sample.png"/>

## Features

- Block Graph, Circle Grpah, TobBottom Graph
- Calculation a x-coordinates to marker at range string array


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
	        implementation 'com.github.jaloveeye:BulletGraph:1:0:4'
	}
```

### Usage

```xml
  <com.herace.bulletgraph.BulletTopBottom
        android:layout_width="300dp"
        android:layout_height="100dp"
        app:bgColor="#FFFFFF"
        app:title="수축기 혈압:하위30%"
        app:isWarning="true"
        app:value="30"
        app:isTop="false"
    />
     <com.herace.bulletgraph.BulletBlock
        android:layout_width="400dp"
        android:layout_height="100dp"
        app:noOfFields="6"
        app:bgColor="#FFFFFF"
        app:title="단백뇨:27mg/dL로 약양성에 해당됩니다."
        app:isWarning="true"
        app:value="27"
        app:isASC="true"
        app:label_1="@array/graphB_LabelA"
        app:label_2="@array/graphB_LabelB"
        app:range="@array/graphB_Range"
        />
    <com.herace.bulletgraph.BulletCircle
        android:layout_width="400dp"
        android:layout_height="100dp"
        app:noOfFields="3"
        app:bgColor="#FFFFFF"
        app:title="이완기 혈압"
        app:subTitle="70mmHg으로 정상범위로 안전합니다."
        app:isWarning="false"
        app:value="70"
        app:isASC="true"
        app:label_1="@array/graphC_LabelA"
        app:label_2="@array/graphD_LabelB"
        app:range="@array/graphD_Range"
        />
    />
```


### Array
```xml
    <string-array name="graphA_LabelA">
        <item />
        <item>정상</item>
        <item>경도</item>
        <item>중등도</item>
        <item>중증</item>
        <item>말기</item>
    </string-array>
    
    <string-array name="graphA_LabelB">
        <item />
        <item>90</item>
        <item>60</item>
        <item>30</item>
        <item>15</item>
        <item />
    </string-array>
    
    <string-array name="graphA_Range">
        <item>200</item>
        <item>90</item>
        <item>60</item>
        <item>30</item>
        <item>15</item>
        <item>0</item>
    </string-array>

    <string-array name="graphB_LabelA">
        <item />
        <item>정상</item>
        <item>약양성 +-</item>
        <item>양성 1+</item>
        <item>양성 2+</item>
        <item>양성 3+</item>
        <item>양성 4+</item>
    </string-array>

    <string-array name="graphB_LabelB">
        <item />
        <item>15</item>
        <item>30</item>
        <item>100</item>
        <item>300</item>
        <item>1000</item>
        <item />
    </string-array>

    <string-array name="graphB_Range">
        <item>0</item>
        <item>15</item>
        <item>30</item>
        <item>100</item>
        <item>300</item>
        <item>1000</item>
        <item>2000</item>
    </string-array>

    <string-array name="graphC_LabelA">
        <item />
        <item>정상</item>
        <item>주의</item>
        <item>위험</item>
    </string-array>

    <string-array name="graphC_LabelB">
        <item />
        <item>120mmHg</item>
        <item>139mmHg</item>
        <item />
    </string-array>

    <string-array name="graphC_Range">
        <item>0</item>
        <item>120</item>
        <item>139</item>
        <item>300</item>
    </string-array>

    <string-array name="graphD_LabelB">
        <item />
        <item>80mmHg</item>
        <item>89mmHg</item>
        <item />
    </string-array>

    <string-array name="graphD_Range">
        <item>0</item>
        <item>80</item>
        <item>89</item>
        <item>200</item>
    </string-array>

```

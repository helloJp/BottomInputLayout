##BottomInputLayout 底部输入框容器

###Effect chart(效果图）
![art](https://github.com/helloJp/BottomInputLayout/blob/master/art/bottom_input_layout.gif)
###desc（简述）: 
自定义容器控件。 点击底部输入框外区域，自动收缩软键盘。在布局文件中，将页面内容布局作为本控件的唯一子View,即可。
```
 <me.jp.bottominputlayout.view.BottomInputLayout
        android:id="@+id/input_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        >

        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            >
            <TextView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:lineSpacingMultiplier="1.3"
                android:text="@string/test_string"
                />
        </ScrollView>

    </me.jp.bottominputlayout.view.BottomInputLayout>
```


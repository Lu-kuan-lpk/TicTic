# Minitictok



内部含有



## Icons文件夹

内部含有工程用到的所有图表的png图像





## ppt.md

制作ppt的思路和拍摄视频的思路



## tictok文件

存放工程的文件



## 工程介绍

本工程由芦宽和卢凯炫两位同学共同完成：

分工如下：

* 芦宽：负责所有界面UI的制作以及视频的显示和滑动吸附功能，并完成了视频流，播放，个人信息的逻辑部分
* 卢凯炫：负责网络信息的爬取和传输，视频和图片的压缩和本地读取，完成了拍摄和上传界面的逻辑部分

制作的难点：

* 在播放界面
  * 对于模仿抖音的滑动吸附功能的制作：
    * 通过在Layout中通过**scrollVerticallyBy**等方法知道滑动的方向并操作移入移除元素的播放状态，并知道播放的是上一个视频还是下一个视频
    * 通过其中的**PagerSnapHelper** 控件做到图片的吸附效果
    * 通过重写onMeasure实现视频的自适应播放
  * 对于爱心动画的制作（通过自定义View实现）
    * 首先将画布裁剪出一个爱心的区域，并通过随机函数将其赋予不同透明度的红色
    * 通过组合 x轴，y轴，缩放，透明度，旋转五个动画获得爱心的上漂效果并分装到flyheart函数中
    * 当在播放界面的activity中点击区域的时候，调用函数，这样每次点击就会有爱心飞出
  * 圆形的头像，文字的轮转
    * 头像应用了CircleImageView控件，下方的轮转应用了textview的marquee的属性
* 对于我的界面的动态修改的制作
  * 根据播放界面的是否点赞将点赞了的所有信息储存到 一个静态变量中
  * 每次进入页面中调用进行recycler的绘制
* 信息流界面
  * 通过两列显示并且显示关于作者的相应信息
    * 通过调整recycler中每一块的xml文件
  * 每次从别的界面回到视频流界面的时候进行刷新操作
    * 通过在主函数的Onresume函数中调用刷新实现
* 自定义拍照界面
  - 使用Camera、SurfaceView和MediaRecorder等几个关键类完成了对摄像的预览和储存
  - 视频存储在应用的私有目录，不用额外申请存储权限
* 上传界面
  - 使用了SiliCompressor完成了对视频的压缩（7.98MB的视频压缩之后只有384KB）
  - 正确解析系统相册传回来的Uri，完成了从系统相册选择图片和视频的功能
  - 使用了MediaMetadataRetriever截取了视频的第一帧的bitmap
  - 使用Lottie完成了上传时的等待动画
* 网络连接部分
  * 通过HttpUrlConnection和Gson完成了视频信息的拉取
  * 通过retrofit完成了视频和相关信息的上传



## 界面截图

![image-20210723010457689](C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20210723010457689.png)

![image-20210723010512838](C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20210723010512838.png

![image-20210723010517306](C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20210723010517306.png)

![image-20210723010525210](C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20210723010525210.png)

![image-20210723010530573](C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20210723010530573.png)

![image-20210723010539373](C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20210723010539373.png)

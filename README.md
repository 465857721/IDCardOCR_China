# IDCardOCR_China
基于tesseract，实现摄像头扫描识别中国二代身份证
# Usage
demo主要提供思路，通过摄像头取景，预览裁剪，ocr识别.
# ScreenShots
![] (https://github.com/465857721/IDCardOCR_China/blob/master/ScreenShots/device-2016-12-24-095238-small.jpg)
# TODO
- 姓名ocr识别
- 民族识别

# 建议
1.对esseract进行训练，目前用的文件为eng数据，没有针对二代身份证进行优化，可以自己进行训练，只识别1-0和英文X。数据包也会相应减小。
（ baseApi.setVariable("tessedit_char_whitelist", "0123456789Xx");）也可以设置白名单，代码里设置了，但是这样没有减少训练文件的大小.

2.目前demo仅仅识别了身份证号码，可以裁剪出来其他信息进行ocr，后续进行优化....
#Thanks
https://github.com/tdk-farkas/CameraSFZ    身份证裁剪
#Changelog
V0.0.2(2016/12/24)
- 6.0权限适配
- 识别单词设置白名单 增加识别率

V0.0.1(2016/12/01)
- 项目导入

# IDCardOCR_China
基于tesseract，实现摄像头扫描识别中国二代身份证，可以识别出身份证号码。效率也可以。
#Usage
demo主要提供思路，通过摄像头取景，预览裁剪，ocr识别，没有对身份证ocr进行优化,
#TODO
1.6.0一些权限没有适配

2.代码优化

#建议
如果要做到真实项目可以对两个方面进行专门优化


1.对esseract进行训练，目前用的文件为eng数据，没有针对二代身份证进行优化，可以自己进行训练，只识别1-0和英文X。数据包也会相应减小。
（ baseApi.setVariable("tessedit_char_whitelist", "0123456789Xx");）也可以设置白名单，代码里设置了，但是这样没有减少训练文件的大小。
2.目前demo仅仅识别了身份证号码，可以裁剪出来其他信息进行ocr，后续进行优化....

#Thanks
https://github.com/tdk-farkas/CameraSFZ    身份证裁剪

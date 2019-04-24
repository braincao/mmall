<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
</head>
<body>
<h2>Hello World build test!</h2>

<!--spring mvc上传图片文件 (不上传到ftp服务器)-->
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="spring mvc上传文件"/>
</form>

<!--spring mvc上传富文本图片 (不上传到ftp服务器)-->
<form name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="spring mvc上传富文本图片"/>
</form>

</body>
</html>
<html>
<body>
<h2>Hello World build test!</h2>

<%--spring mvc上传文件--%>
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="spring mvc上传文件"/>
</form>

</body>
</html>
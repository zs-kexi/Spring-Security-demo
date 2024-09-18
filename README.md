端口号为：8080。
登录路径为：ip:8080/user/login。  账号密码：test/123456。  返回值值为jwt令牌。 请求方式为POST。
将返回值放入请求头中，参数名为：Authorization，之后才能请求下面路径获取值。
鉴权路径：ip:8080/user/hello。  需要权限：test。  返回值为"Hello World"。

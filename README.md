# windows 版本 java jar 程序打包发布工具


### 发布程序参数说明
  * 服务ID号: windows服务的唯一标识ID 不能存在重复
  * 服务名称: 服务安装完成后再windows中显示的名称
  * 存储路径: java程序存放的目录 例如：F:\program
  * 运行参数: java 启动jar 的参数配置. 从 -jar 开始 配置执行参数。 [JAR] 表示jar程序所在目录，系统会自动填充。此处表示占位符
  
  -jar -Xms1024m -Xmx1024m [JAR] --spring.profiles.active=dev
  
  * 运行文件: jar程序 spring boot项目
 
## 说明
  * 程序中使用WebSocket 如果程序通过nginx转发 需要配置nginx 对webSocket的支持
## Nginx配置WebSocket 支持
 	server {
        listen       80;
        server_name  localhost;
        client_max_body_size 200M;
        proxy_set_header X-Forwarded-Host $host;
        proxy_set_header X-Forwarded-Server $host;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        location / {
            proxy_pass http://127.0.0.1:8030;
            proxy_http_version 1.1;
            proxy_connect_timeout 1s;
            proxy_read_timeout 310s; 
            proxy_send_timeout 300s; 
            
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
        }
 		
    }

## 项目构建
spring boot + webSocket
## 项目基础信息
请求地址： http://localhost:1000/
登录账户：admin
登录密码：123456

FROM livingobjects/jre8
# 工作目录
WORKDIR /app
# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone
# 拷贝文件到工作目录
ADD ./*.jar /app
# 运行jar
ENV PORT=8580
ENTRYPOINT java -jar -DXms=128m -Dxmx=256m /app/webapi-1.0.jar --server.port=$PORT

# docker build -t simm-tools:v1.0 .
# docker run -d -p 8580:8080 -e PORT=8080 --restart=always --name=simm-tools simm-tools:v1.0
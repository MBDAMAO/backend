# 使用官方Java镜像作为基础镜像
FROM openjdk:17-jdk

# 设置环境变量，例如JAVA_OPTS可以用于设置JVM参数
#ENV JAVA_OPTS="-Xmx1024m"

# 将当前目录下的所有文件复制到容器中的/app目录
COPY . /app

# 切换工作目录为/app
WORKDIR /app

# 给mvnw文件添加可执行权限
RUN chmod +x mvnw

# 构建项目，这里假设你使用Maven作为构建工具
# 如果你使用其他构建工具，例如Gradle，请替换为相应的命令
RUN ./mvnw -s .mvn/settings.xml package

# 暴露端口，根据你的应用需求设置
EXPOSE 8081

# 定义容器启动后执行的命令，这里假设你的应用是一个Spring Boot应用
# 如果是其他类型的应用，需要替换为相应的启动命令
CMD ["java", "-Xmx1024m", "-jar", "./target/MainApplication-1.0.0.jar"]
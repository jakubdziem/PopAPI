# Base image with a minimal JDK
FROM openjdk:21-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Install wget, curl, and other dependencies
RUN apt-get update && apt-get install -y --no-install-recommends wget curl unzip

# Download and install Maven 3.9.8
RUN wget https://archive.apache.org/dist/maven/maven-3/3.9.8/binaries/apache-maven-3.9.8-bin.zip && \
    unzip apache-maven-3.9.8-bin.zip -d /opt && \
    mv /opt/apache-maven-3.9.8 /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/bin/mvn && \
    rm apache-maven-3.9.8-bin.zip

# Set Maven environment variables
ENV MAVEN_HOME /opt/maven
ENV PATH $MAVEN_HOME/bin:$PATH

# Verify Maven version
RUN mvn -version

# Copy the pom.xml and source code
COPY pom.xml ./
COPY src /app/src

# Download the JDK
RUN wget https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21+35/OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz && \
    tar -xzf OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz -C /opt && \
    mv /opt/jdk-21+35 /opt/jdk-21 && \
    update-alternatives --install /usr/bin/java java /opt/jdk-21/bin/java 1 && \
    update-alternatives --install /usr/bin/javac javac /opt/jdk-21/bin/javac 1 && \
    update-alternatives --set java /opt/jdk-21/bin/java && \
    update-alternatives --set javac /opt/jdk-21/bin/javac && \
    rm OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz

# Clean up package lists
RUN rm -rf /var/lib/apt/lists/*

# Build the application
RUN mvn clean package

# Final stage to use the official OpenJDK image for running the application
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/PopAPI-0.0.1-SNAPSHOT.jar /app/PopAPI-0.0.1-SNAPSHOT.jar

# Expose the port your application will run on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/PopAPI-0.0.1-SNAPSHOT.jar"]

COPY src/main/resources/restricted_words.txt /app/src/main/resources/restricted_words.txt

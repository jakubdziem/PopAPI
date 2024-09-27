# Use the official Maven image with JDK 11
FROM maven:3.8.6-openjdk-11 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src /app/src

# Install wget
RUN apt-get update && apt-get install -y --no-install-recommends wget

# Download the JDK
RUN wget https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21+35/OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz

# Extract the JDK and set up alternatives
RUN tar -xzf OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz -C /opt && \
    mv /opt/jdk-21+35 /opt/jdk-21 && \
    update-alternatives --install /usr/bin/java java /opt/jdk-21/bin/java 1 && \
    update-alternatives --install /usr/bin/javac javac /opt/jdk-21/bin/javac 1 && \
    update-alternatives --set java /opt/jdk-21/bin/java && \
    update-alternatives --set javac /opt/jdk-21/bin/javac && \
    rm OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz

# Clean up package lists
RUN rm -rf /var/lib/apt/lists/*

# Verify JDK version
RUN java -version

# Build the application
RUN mvn clean package

# Use the official OpenJDK 21 image to run the application
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/PopAPI-0.0.1-SNAPSHOT.jar /app/PopAPI-0.0.1-SNAPSHOT.jar

# Expose the port your application will run on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/PopAPI-0.0.1-SNAPSHOT.jar"]

# Base Image
FROM openjdk:11

# Add the fatjar in the image
COPY target/authorize-1.0-snapshot.jar /

# Default command
CMD java -jar /authorize-1.0-snapshot.jar
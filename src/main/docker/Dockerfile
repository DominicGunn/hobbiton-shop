FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp

ARG BUILD_VERSION

# Environmental Vars
ENV JAVA_OPTS=""

# Copy app to Docker.
COPY shop-$BUILD_VERSION.jar /opt/hobbiton/service.jar
RUN sh -c 'touch /opt/hobbiton/service.jar'

# Launch Application
CMD ["java", "-Xmx200m", "-jar", "/opt/hobbiton/service.jar"]
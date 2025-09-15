FROM maven:3.9.9-eclipse-temurin-24 AS builder

ARG BUILD_MODE
ARG API_URL

ENV VITE_API_URL=$API_URL

WORKDIR /home/build
COPY pom.xml .
COPY src ./src

RUN if [ -n "$BUILD_MODE" ]; then \
      mvn clean package -DskipTests -Pnative-jlink -Dnpm.build.mode="$BUILD_MODE"; \
    else \
      mvn clean package -DskipTests -Pnative-jlink; \
    fi

RUN jar xvf target/*jar &&\
    jdeps \
    --ignore-missing-deps \
    -q \
    --recursive \
    --multi-release 24 \
    --print-module-deps \
    --class-path 'BOOT-INF/lib/*' \
    --module-path 'BOOT-INF/lib/*' \
    target/*.jar > jre-deps.txt &&\
    jlink \
    --verbose \
    --add-modules $(cat jre-deps.txt) \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress 2 \
    --output jre

FROM debian:bullseye-slim

RUN useradd -m miras

WORKDIR /home/miras/data
RUN chown miras:miras /home/miras/data

WORKDIR /home/miras

COPY --from=builder /home/build/jre ./jre
COPY --from=builder /home/build/target/*.jar app.jar

ENV JAVA_HOME=/home/miras/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

EXPOSE 8080
USER miras

ENTRYPOINT ["java", "-jar", "app.jar"]

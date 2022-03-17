FROM openjdk:17.0.2-slim-bullseye

RUN groupadd -r -g 999 kafka && useradd -r -g kafka -u 999 kafka

ENV GOSU_VERSION 1.14
RUN set -eux; \
	savedAptMark="$(apt-mark showmanual)"; \
	apt-get update; \
	apt-get install -y --no-install-recommends ca-certificates dirmngr gnupg wget; \
	rm -rf /var/lib/apt/lists/*; \
	dpkgArch="$(dpkg --print-architecture | awk -F- '{ print $NF }')"; \
	wget -O /usr/local/bin/gosu "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$dpkgArch"; \
	wget -O /usr/local/bin/gosu.asc "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$dpkgArch.asc"; \
	export GNUPGHOME="$(mktemp -d)"; \
	gpg --batch --keyserver hkps://keys.openpgp.org --recv-keys B42F6819007F00F88E364FD4036A9C25BF357DD4; \
	gpg --batch --verify /usr/local/bin/gosu.asc /usr/local/bin/gosu; \
	gpgconf --kill all; \
	rm -rf "$GNUPGHOME" /usr/local/bin/gosu.asc; \
	apt-mark auto '.*' > /dev/null; \
	[ -z "$savedAptMark" ] || apt-mark manual $savedAptMark > /dev/null; \
	apt-get purge -y --auto-remove -o APT::AutoRemove::RecommendsImportant=false; \
	chmod +x /usr/local/bin/gosu; \
	gosu --version; \
	gosu nobody true

COPY kafka_2.13-3.1.0.tar /usr/src/
COPY docker-entrypoint.sh /usr/local/bin/

ENV KAFKA_HOME /usr/local/kafka
ENV PATH $KAFKA_HOME/bin:$PATH

RUN set -eux; \
	mkdir -p /usr/local/kafka; \
	chmod +x /usr/local/bin/*.sh;\
	mkdir /tmp/kafka-logs && chown kafka:kafka /tmp/kafka-logs;\
	chown kafka:kafka /usr/local/kafka;\
	tar -xf /usr/src/kafka_2.13-3.1.0.tar -C /usr/local/kafka --strip-components=1;

VOLUME /tmp/kafka-logs
WORKDIR /tmp/kafka-logs

ENTRYPOINT ["docker-entrypoint.sh"]

EXPOSE 9092
CMD ["/usr/local/kafka/config/server.properties"]

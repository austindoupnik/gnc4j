ARG GNUCASH_VERSION=4.9

FROM ubuntu:jammy-20220428 as builder

ARG GNUCASH_VERSION
ARG MAKE_JOBS=4

RUN sed -i.backup 's/# deb-src/deb-src/' /etc/apt/sources.list \
  && apt-get update \
  && apt-get build-dep --no-install-recommends -y gnucash \
  && apt-get install --no-install-recommends -y curl \
  && mv /etc/apt/sources.list.backup /etc/apt/sources.list \
  && rm -rf /var/lib/apt/lists/* \
  && cd /tmp \
  && curl -OL https://github.com/Gnucash/gnucash/releases/download/${GNUCASH_VERSION}/gnucash-${GNUCASH_VERSION}.tar.gz \
  && tar xf gnucash-${GNUCASH_VERSION}.tar.gz \
  && rm gnucash-${GNUCASH_VERSION}.tar.gz \
  && mkdir build-gnucash-${GNUCASH_VERSION} \
  && cd build-gnucash-${GNUCASH_VERSION} \
  && cmake -DCMAKE_INSTALL_PREFIX=/opt/gnucash-${GNUCASH_VERSION} ../gnucash-${GNUCASH_VERSION} \
  && make -j${MAKE_JOBS} \
  && make install \
  && cd .. \
  && rm -rf gnucash-${GNUCASH_VERSION} build-gnucash-${GNUCASH_VERSION} 

FROM ubuntu:jammy-20220428

ARG GNUCASH_VERSION

COPY --from=builder /opt/gnucash-${GNUCASH_VERSION}/lib /opt/gnucash-${GNUCASH_VERSION}/lib

RUN apt-get update \
  && apt-get install -y libglib2.0-bin libboost-regex1.74.0 libboost-locale1.74.0 libboost-filesystem1.74.0 libdbi1 \
  && rm -rf /var/lib/apt/lists/*

RUN apt-get update \
  && apt-get install -y openjdk-8-jdk-headless \
  && rm -rf /var/lib/apt/lists/*

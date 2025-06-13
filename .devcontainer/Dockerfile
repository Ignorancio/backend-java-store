FROM mcr.microsoft.com/devcontainers/java:17

RUN apt-get update && apt-get install -y \
    maven \
    curl \
    unzip \
    procps \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libfreetype6 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
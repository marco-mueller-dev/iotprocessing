FROM ubuntu:24.04.3
LABEL authors="lars_"

ENTRYPOINT ["top", "-b"]
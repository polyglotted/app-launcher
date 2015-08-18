FROM polyglotted/pg-app-base

RUN mkdir /app
ADD ${project.artifactId}-${project.version}-binary.tar.gz /app/
WORKDIR /app
RUN ln -s ${project.artifactId} current
CMD ["/app/current/bin/appservice","start"]

FROM polyglotted/pg-app-base

RUN mkdir /app
ADD *-binary.tar.gz /app/
# ADD ${project.artifactId}-${project.version}-binary.tar.gz /app/
WORKDIR /app
RUN ln -s $(ls) current
# RUN ln -s ${project.artifactId} current
CMD ["/app/current/bin/appservice","start"]

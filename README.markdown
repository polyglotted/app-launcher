webapp-launcher
===============

Launch an in-process web application using Jetty; this work is highly inspired from [Embedded Jetty and Spring MVC with Maven](http://steveliles.github.com/setting_up_embedded_jetty_8_and_spring_mvc_with_maven.html). However there are subtle differences with the original work and this artifact is made embeddable into other projects (see below for usage). Also the artifact is uploaded to Maven central repository.

There are two modes for using this launcher - directly within an IDE like Eclipse / IntelliJ or running it as a service from the commandline (tested in Linux / MacOS).

For both the use-cases the basic requirement is to first include this project within the maven dependency pom. The snippet for that is shown below.

    <dependency>
        <groupId>org.polyglotted</groupId>
        <artifactId>webapp-launcher</artifactId>
        <version>1.0.1</version>
    </dependency>

Using it to run a web-application within the IDE
-------------------------------------------------

Add the dependency to the project POM as shown above.

Set the type of the project to be _"jar"_ and not a _"war"_. But set your project to have the default maven structure for developing a web-application; i.e. the web application code should live under the _"src/main/webapp"_ directory.

Ensure that the _"src/main/webapp"_ directory is added as a source folder to the build path (i.e. included in the classpath when executing code) within the IDE.

Create a new java launcher, set the main class to be _"org.polyglotted.webapp.launcher.Main"_ and add a new VM argument "-Dwebapp.in.ide=true".

That's it. Your application should be running in the IDE.

Packaging the launcher along with your application
--------------------------------------------------

Add the dependency to the project POM as shown above and set the type of the project to be _"jar"_.

Add an unpack execution step to unzip the configuration and launcher scripts to the target directory. The code is given below.

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <version>2.6</version>
        <executions>
            <execution>
                <id>unpack-deps</id>
                <phase>prepare-package</phase>
                <goals>
                    <goal>unpack</goal>
                </goals>
                <configuration>
                    <artifactItems>
                        <artifactItem>
                            <groupId>org.polyglotted</groupId>
                            <artifactId>webapp-launcher</artifactId>
                            <type>zip</type>
                            <classifier>binary</classifier>
                            <overWrite>false</overWrite>
                            <outputDirectory>${project.build.directory}/config</outputDirectory>
                        </artifactItem>
                    </artifactItems>
                </configuration>
            </execution>
        </executions>
    </plugin>

Create a new binary assembly for your package, including all the dependencies and configurations. Add an binary execution step to the assembly to create the packages. 

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
            <descriptors>
                <descriptor>src/main/assembly/binary.xml</descriptor>
            </descriptors>
        </configuration>
        <executions>
            <execution>
                <id>make-assembly</id>
                <phase>package</phase>
                <goals>
                    <goal>single</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

Create a _"binary.xml"_ file under the _"src/main/assembly"_ directory.

    <assembly xmlns="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.0 http://maven.apache.org/xsd/assembly-1.1.0.xsd">
        <id>binary</id>
        <includeBaseDirectory>true</includeBaseDirectory>
        <baseDirectory>${project.artifactId}</baseDirectory>
        <formats>
            <format>tar.gz</format>
            <format>zip</format>
        </formats>
        <fileSets>
            <fileSet>
                <directory>${project.basedir}</directory>
                <outputDirectory>/</outputDirectory>
                <includes>
                    <include>README*</include>
                    <include>LICENSE*</include>
                    <include>NOTICE*</include>
                </includes>
            </fileSet>
            <fileSet>
                <directory>${project.build.directory}/config</directory>
                <outputDirectory>/</outputDirectory>
                <lineEnding>unix</lineEnding>
                <fileMode>0755</fileMode>
            </fileSet>
            <fileSet>
                <directory>${project.basedir}/src/main/config</directory>
                <outputDirectory>/</outputDirectory>
                <lineEnding>unix</lineEnding>
                <fileMode>0755</fileMode>
            </fileSet>
        </fileSets>
        <dependencySets>
            <dependencySet>
                <outputDirectory>/lib</outputDirectory>
                <useProjectArtifact>true</useProjectArtifact>
            </dependencySet>
        </dependencySets>
    </assembly>

Override any system / JVM arguments specific for your application (refer to next section for details)

Executing `mvn package` will create your final assembled package that you can unzip in a target environment.

Once unzipped, change to the main directory and you can call _"bin/appservice start"_ to start your web application. You can also use _"bin/appservice check"_ to check if the application is running and _"bin/appservice stop"_ to stop your web application.

Additional VM and System Configuration
--------------------------------------

The launcher script on the command line uses two files to configure the JVM and system properties for the running environment. These files are located under the _"src/main/config/etc/"_ directory and are named _"vmargs.file"_ and _"sysargs.properties"_ appropriately. You can override these values by creating equivalent files in your project under the _"src/main/config/etc"_ folder. Unfortunately the assembly plugin is not capable of merging files, so you will have to override all the properties that are configured in the default file.

### vmargs.file ###

    -Xms128m
    -Xmx128m

### sysargs.properties ###

    jetty.http.port=8080
    jetty.http.minThreads=10
    jetty.http.maxThreads=100
    webapp.log.path=target/accesslogs/yyyy_mm_dd.request.log
    webapp.in.ide=false

Please ensure not to have any empty lines on these files, as the bash script only does a simple read of them when launching the web application. The system properties can be overriden within the IDE in your launcher if you prefer.

Also there is a _"bin/appstart"_ launcher that you can use to run arbitrary classes in the foreground.

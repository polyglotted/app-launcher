webapp-launcher
===============

Launch an in-process web application using Jetty; this work is highly inspired from [Embedded Jetty and Spring MVC with Maven](http://steveliles.github.com/setting_up_embedded_jetty_8_and_spring_mvc_with_maven.html). However there are subtle differences with the original work and this artifact is made embeddable into other projects (see below for usage). Also the artifact is uploaded to Maven central repository.

There are two modes for using this launcher - directly within an IDE like Eclipse / IntelliJ or running it as a service from the commandline (tested in Linux / MacOS).

For both the use-cases the basic requirement is to first include this project within the maven dependency pom. The snippet for that is shown below.

    <dependency>
        <groupId>org.polyglotted</groupId>
        <artifactId>webapp-launcher</artifactId>
        <version>1.0.3</version>
    </dependency>

Using it to run a web-application within the IDE
-------------------------------------------------

Add the dependency to the project POM as shown above.

Set the type of the project to be _"jar"_ and not a _"war"_. But set your project to have the default maven structure for developing a web-application; i.e. the web application code should live under the _"src/main/webapp"_ directory.

Ensure that the _"src/main/webapp"_ directory is available as a folder within the IDE.

When you use maven to compile the project, it automatically generates a ${artifactId}-webapp.launch file that you can use to launch the application.

It is always a good idea to create a _"src/main/config/etc/sysargs.properties"_ to setup your application profile. Refer to the section below for details.

That's it. Your application should be running in the IDE.

Packaging the launcher along with your application
--------------------------------------------------

Add the dependency to the project POM as shown above and set the type of the project to be _"jar"_.

Add the following plugin configuration to your POM. Unfortunately copy-maven-plugin does not work on Maven 3.1.x so will have to be used only with Maven 3.0. executable.

    <plugin>
        <groupId>com.github.goldin</groupId>
        <artifactId>copy-maven-plugin</artifactId>
        <version>0.2.5</version>
        <executions>
            <execution>
                <id>prepare-web-archive</id>
                <phase>generate-sources</phase>
                <goals>
                    <goal>copy</goal>
                </goals>
                <configuration>
                    <resources>
                        <resource>
                            <targetPath>${project.build.directory}/webapp-launcher</targetPath>
                            <dependency>
                                <groupId>org.polyglotted</groupId>
                                <artifactId>webapp-launcher</artifactId>
                                <version>${webapplauncher.version}</version>
                                <type>zip</type>
                                <classifier>binary</classifier>
                            </dependency>
                            <unpack>true</unpack>
                            <skipIdentical>true</skipIdentical>
                        </resource>
                        <resource>
                            <targetPath>${basedir}</targetPath>
                            <file>${project.build.directory}/webapp-launcher/launcher/webapp.launch</file>
                            <destFileName>${project.artifactId}-Main.launch</destFileName>
                            <filtering>true</filtering>
                            <skipIdentical>true</skipIdentical>
                        </resource>
                        <resource>
                            <targetPath>${basedir}/.idea/runConfigurations</targetPath>
                            <file>${project.build.directory}/webapp-launcher/launcher/webapp-idea.xml</file>
                            <destFileName>${project.artifactId}-Main.xml</destFileName>
                            <filtering>true</filtering>
                            <skipIdentical>true</skipIdentical>
                        </resource>
                        <resource>
                            <targetPath>${project.build.directory}/config</targetPath>
                            <directory>${project.build.directory}/webapp-launcher</directory>
                            <includes>
                                <include>bin/**</include>
                                <include>etc/**</include>
                            </includes>
                            <preservePath>true</preservePath>
                            <filtering>true</filtering>
                        </resource>
                        <resource>
                            <targetPath>${project.build.directory}/config/etc</targetPath>
                            <directory>${basedir}/src/main/config/etc</directory>
                            <filtering>true</filtering>
                            <failIfNotFound>false</failIfNotFound>
                        </resource>
                        <resource>
                            <targetPath>${project.build.outputDirectory}/webapp</targetPath>
                            <directory>${basedir}/src/main/webapp</directory>
                            <preservePath>true</preservePath>
                            <skipIdentical>true</skipIdentical>
                            <failIfNotFound>false</failIfNotFound>
                        </resource>
                    </resources>
                </configuration>
            </execution>
        </executions>
    </plugin>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
            <descriptors>
                <descriptor>${project.build.directory}/webapp-launcher/templates/assembly.xml</descriptor>
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


Override any system / JVM arguments specific for your application (refer to next section for details)

Executing `mvn package` will create your final assembled package that you can unzip in a target environment.

Once unzipped, change to the main directory and you can call _"bin/appservice start"_ to start your web application. You can also use _"bin/appservice check"_ to check if the application is running and _"bin/appservice stop"_ to stop your web application.

Please find a sample usage of this launcher at <https://github.com/polyglotted/graphonomy>

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
    webapp.context.path=/
    
Please ensure not to have any empty lines on these files, as the bash script only does a simple read of them when launching the web application. The system properties can be overriden within the IDE in your launcher if you prefer.

Also there is a _"bin/appstart"_ launcher that you can use to run arbitrary classes in the foreground.

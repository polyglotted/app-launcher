package io.polyglotted.app.core;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import fj.data.Option;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@RequiredArgsConstructor
public class Gaveti {
    public static final String GroupId = "groupId";
    public static final String ArtifactId = "artifactId";
    public static final String Version = "version";
    public static final String Environment = "environment";
    public static final String InstanceType = "instanceType";
    public static final String InstanceName = "instanceName";

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String environment;
    private final Option<String> instanceType;
    private final Option<String> instanceName;

    public Gaveti(String groupId, String artifactId, String version, String environment) {
        this(groupId, artifactId, version, environment, Option.<String>none(), Option.<String>none());
    }

    public static Gaveti from(Config config) {
        Option<String> instanceType = config.hasPath(InstanceType) ? Option.some(config.getString(InstanceType))
                : Option.<String>none();
        Option<String> instanceName = config.hasPath(InstanceName) ? Option.some(config.getString(InstanceName))
                : Option.<String>none();

        return new Gaveti(config.getString(GroupId), config.getString(ArtifactId), config.getString(Version),
                config.getString(Environment), instanceType, instanceName);
    }

    public Config asConfig() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put(Gaveti.GroupId, groupId());
        builder.put(Gaveti.ArtifactId, artifactId());
        builder.put(Gaveti.Version, version());
        builder.put(Gaveti.Environment, environment());
        putIfPresent(builder, Gaveti.InstanceType, instanceType());
        putIfPresent(builder, Gaveti.InstanceName, instanceName());
        return ConfigFactory.parseMap(builder.build());
    }

    private static void putIfPresent(ImmutableMap.Builder<String, Object> builder, String name, Option<String> option) {
        if (option.isSome()) {
            builder.put(name, option.some());
        }
    }
}

package io.polyglotted.app.core;

import org.testng.annotations.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PathsTest {

    @Test
    public void with_instance() {
        final Paths pathsVar = new Paths("local", Optional.of("anInstance"));
        final Stream<String> paths = pathsVar.forName("aName.properties");
        assertThat(newArrayList(paths.toArray()),
                is(asList("local/anInstance/aName.properties", "local/aName-anInstance.properties", "local/aName.properties", "aName.properties")));
    }

    @Test
    public void no_instance() {
        final Paths pathsVar = new Paths("local", Optional.<String>empty());
        final Stream<String> paths = pathsVar.forName("aName.properties");
        assertThat(newArrayList(paths.toArray()),
                is(asList("local/aName.properties", "aName.properties")));
    }

    @Test
    public void no_extension_with_instance() {
        final Paths pathsVar = new Paths("local", Optional.of("anInstance"));
        final Stream<String> paths = pathsVar.forName("aName");
        assertThat(newArrayList(paths.toArray()),
                is(asList("local/anInstance/aName", "local/aName-anInstance", "local/aName", "aName")));
    }

    @Test
    public void no_extension_no_instance() {
        final Paths pathsVar = new Paths("local", Optional.<String>empty());
        final Stream<String> paths = pathsVar.forName("aName");
        assertThat(newArrayList(paths.toArray()), is(asList("local/aName", "aName")));
    }
}
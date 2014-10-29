package org.polyglotted.webapp.core;

import fj.data.Option;
import fj.data.Stream;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PathsTest {

    @Test
    public void with_instance() {
        final Paths pathsVar = new Paths("local", Option.some("anInstance"));
        final Stream<String> paths = pathsVar.forName("aName.properties");
        assertThat(newArrayList(paths.toCollection()),
                is(asList("local/anInstance/aName.properties", "local/aName-anInstance.properties", "local/aName.properties", "aName.properties")));
    }

    @Test
    public void no_instance() {
        final Paths pathsVar = new Paths("local", Option.<String>none());
        final Stream<String> paths = pathsVar.forName("aName.properties");
        assertThat(newArrayList(paths.toCollection()),
                is(asList("local/aName.properties", "aName.properties")));
    }

    @Test
    public void no_extension_with_instance() {
        final Paths pathsVar = new Paths("local", Option.some("anInstance"));
        final Stream<String> paths = pathsVar.forName("aName");
        assertThat(newArrayList(paths.toCollection()),
                is(asList("local/anInstance/aName", "local/aName-anInstance", "local/aName", "aName")));
    }

    @Test
    public void no_extension_no_instance() {
        final Paths pathsVar = new Paths("local", Option.<String>none());
        final Stream<String> paths = pathsVar.forName("aName");
        assertThat(newArrayList(paths.toCollection()),
                is(asList("local/aName", "aName")));
    }
}
package org.polyglotted.webapp.launcher.util;

import java.io.File;
import java.io.FileFilter;

class ZipFileFilter implements FileFilter {

    @Override
    public boolean accept(File path) {
        if (path.isFile() && path.getName().toLowerCase().endsWith(".jar"))
            return true;

        return false;
    }
}

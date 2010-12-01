package com.dsklyut.virgo.deployer.ear.artifact.descriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parsed application.xml
 * <p/>
 * User: dsklyut
 * Date: 11/18/10
 * Time: 1:05 PM
 */
public final class EarDescriptor {

    private static final String DEFAULT_LIBRARY_DIRECTORY = "lib".intern();

    /**
     * Name of the application (right now only for v6).
     */
    private final String applicationName;

    /**
     * description
     */
    private final String description;

    /**
     * display name
     */
    private final String displayName;

    /**
     * directory to look for libraries to add to classpath
     * i.e. Bundle-ClassPath like.
     * TODO: HOW TO HANDLE ORDERING?
     */
    private final String libraryDirectory;

    // NOTE: not dealing with images...
    // NOTE: not dealing with security config right now.

    /**
     * List of modules
     */
    private final List<Module> modules;

    public EarDescriptor(String applicationName, String description, String displayName, String libraryDirectory, List<Module> modules) {
        this.applicationName = applicationName;
        this.description = description;
        this.displayName = displayName;
        this.libraryDirectory = libraryDirectory == null ? DEFAULT_LIBRARY_DIRECTORY : libraryDirectory;

        this.modules = Collections.unmodifiableList(modules == null ? Collections.<Module>emptyList() : modules);
    }

    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Free text description of the app
     *
     * @return
     */
    public String getDescription() {
        return description;
    }


    /**
     * Display name of the app
     *
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Library directory to look for artifacts.
     *
     * @return
     */
    public String getLibraryDirectory() {
        return libraryDirectory;
    }

    /**
     * Unmodifiable view of modules in an ear
     *
     * @return
     */
    public List<Module> getModules() {
        return modules;
    }


    public List<Module> listUnsupportedModules() {
        List<Module> result = new ArrayList<Module>();
        for (Module m : getModules()) {
            if (!m.getType().isSupported()) {
                result.add(m);
            }
        }
        return result;

    }

}

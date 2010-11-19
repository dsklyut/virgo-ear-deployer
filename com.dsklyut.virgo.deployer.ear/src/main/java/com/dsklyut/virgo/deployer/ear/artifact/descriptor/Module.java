package com.dsklyut.virgo.deployer.ear.artifact.descriptor;

/**
 * User: dsklyut
 * Date: 11/18/10
 * Time: 1:06 PM
 * <p/>
 * TODO: consider extending ArtifactSpecification
 */
public class Module {

    private final ModuleType type;
    private final String path;

    public Module(ModuleType type, String path) {
        this.type = type;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public ModuleType getType() {
        return type;
    }
}

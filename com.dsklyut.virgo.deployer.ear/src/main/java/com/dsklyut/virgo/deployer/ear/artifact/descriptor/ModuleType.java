package com.dsklyut.virgo.deployer.ear.artifact.descriptor;

/**
 * User: dsklyut
 * Date: 11/18/10
 * Time: 1:07 PM
 */
public enum ModuleType {

    /**
     * enterprise java beans - not supported
     */
    EJB("ejb", false),
    /**
     * resource adapter (RAR) - not supported
     * see jca spec
     */
    RAR("connector", false),
    /**
     * in jee - this is a client jar.
     * in virgo - this is any jar to define order of deployment - must be a valid bundle!!!!
     */
    JAVA("java", true),
    /**
     * web application - can be a WAR or a WAB
     */
    WEB("web", true);

    private final boolean supported;
    private final String type;

    private ModuleType(String type, boolean supported) {
        this.type = type;
        this.supported = supported;
    }

    public boolean isSupported() {
        return supported;
    }

    public String getType() {
        return type;
    }

    public static ModuleType fromStringType(String type) {
        for (ModuleType mtype : values()) {
            if (mtype.getType().equals(type)) {
                return mtype;
            }
        }
        throw new IllegalArgumentException(type + " type is not supported");
    }
}

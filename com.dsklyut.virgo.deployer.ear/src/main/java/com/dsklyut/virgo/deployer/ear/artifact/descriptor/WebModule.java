package com.dsklyut.virgo.deployer.ear.artifact.descriptor;

/**
 * User: dsklyut
 * Date: 11/18/10
 * Time: 2:49 PM
 */
public final class WebModule extends Module {

    private final String contextPath;

    public WebModule(String path, String contextPath) {
        super(ModuleType.WEB, path);
        this.contextPath = contextPath;
    }

    public String getContextPath() {
        return contextPath;
    }
}

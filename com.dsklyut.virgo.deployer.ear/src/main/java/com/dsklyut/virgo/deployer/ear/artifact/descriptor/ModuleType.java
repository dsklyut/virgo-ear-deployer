package com.dsklyut.virgo.deployer.ear.artifact.descriptor;

import org.eclipse.virgo.util.common.Assert;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

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
    WEB("web", true) {
        @Override
        public Module fromXml(Element xml) {
            WebModule result = null;
            Element webElement = DomUtils.getChildElementByTagName(xml, getType());
            if (webElement != null) {
                result = new WebModule(DomUtils.getChildElementValueByTagName(webElement, "web-uri"),
                                       DomUtils.getChildElementValueByTagName(webElement, "context-root"));
            }
            return result;
        }
    };


    private final boolean supported;
    private final String type;

    private ModuleType(String type, boolean supported) {
        this.type = type;
        this.supported = supported;
    }

    public Module fromXml(Element xml) {
        Module result = null;
        Element element = DomUtils.getChildElementByTagName(xml, getType());
        if (element != null) {
            result = new Module(this, element.getNodeValue());
        }
        return result;
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

    public static Module fromXmlElement(Element moduleElement) {
        Assert.notNull(moduleElement, "{0} must not be null", "Module");
        for (ModuleType mtype : values()) {
            Module m = mtype.fromXml(moduleElement);
            if (m != null) {
                return m;
            }
        }
        throw new IllegalArgumentException("Could not determine module type for " + moduleElement.getNodeName() + " xml element");
    }
}

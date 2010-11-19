package com.dsklyut.virgo.deployer.ear.artifact.descriptor;

import com.dsklyut.virgo.deployer.ear.artifact.EarBridge;
import com.dsklyut.virgo.deployer.ear.artifact.descriptor.internal.EarReaderEntityResolver;
import com.dsklyut.virgo.deployer.ear.artifact.descriptor.internal.EarReaderErrorHandler;
import org.slf4j.LoggerFactory;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple parser of jee application (ear) descriptor.
 * <p/>
 * NOTE: some code stolen from PlanReader
 * <p/>
 * This parser does not parse out any of the extended attributes of the jee application at this time.
 * No ejb refs, jpa, security....
 * <p/>
 * Just simply modules and descriptor info...
 *
 * @author dsklyut
 * @see org.eclipse.virgo.kernel.artifact.plan.PlanReader
 *      <p/>
 */
public final class EarDescriptorReader {


    public static final String DISPLAY_NAME_ELEMENT = "display-name".intern();
    public static final String DESCRIPTION_ELEMENT = "description".intern();
    public static final String APPLICATION_NAME_ELEMENT = "application-name".intern();

    public static final String LIBRARY_DIRECTORY_ELEMENT = "library-directory".intern();
    public static final String MODULE_ELEMENT = "module".intern();


    private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";


    public EarDescriptor read(InputStream inputStream) {
        try {
            Document doc = readDocument(inputStream);
            Element element = doc.getDocumentElement();
            return parseApplicationElement(element);
        } catch (Exception ex) {
            throw new RuntimeException("Error parsing input application.xml", ex);
        }
    }

    private EarDescriptor parseApplicationElement(Element root) {
        String displayName = DomUtils.getChildElementValueByTagName(root, DISPLAY_NAME_ELEMENT);
        String description = DomUtils.getChildElementValueByTagName(root, DESCRIPTION_ELEMENT);

        int version = Integer.parseInt(root.getAttribute("version"));

        String applicationName = ApplicationNameHolder.get();
        if (version == 6) {
            applicationName = DomUtils.getChildElementValueByTagName(root, APPLICATION_NAME_ELEMENT);
        }

        String libraryDir = DomUtils.getChildElementValueByTagName(root, LIBRARY_DIRECTORY_ELEMENT);

        List<Module> modules = parseModules(DomUtils.getChildElementsByTagName(root, MODULE_ELEMENT));

        return new EarDescriptor(applicationName, description, displayName, libraryDir, modules);
    }

    private List<Module> parseModules(List<Element> moduleElements) {
        List<Module> result = new ArrayList<Module>();
        for (Element e : moduleElements) {
            result.add(parseModule(e));
        }

        return result;
    }

    private Module parseModule(Element mod) {
        // run down possible node types
        Module result = parseModuleJava(mod);
        if (result == null) {
            result = parseModuleWeb(mod);
        }
        return result;
    }

    private Module parseModuleWeb(Element mod) {
        WebModule result = null;
        Element webElement = DomUtils.getChildElementByTagName(mod, "web");
        if (webElement != null) {
            result = new WebModule(DomUtils.getChildElementValueByTagName(webElement, "web-uri"),
                                   DomUtils.getChildElementValueByTagName(webElement, "context-root"));
        }
        return result;
    }

    private Module parseModuleJava(Element mod) {
        Module result = null;
        Element javaElement = DomUtils.getChildElementByTagName(mod, "java");
        if (javaElement != null) {
            result = new Module(ModuleType.JAVA, javaElement.getNodeValue());
        }
        return result;
    }

    private Document readDocument(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = createDocumentBuilderFactory().newDocumentBuilder();
        builder.setEntityResolver(new EarReaderEntityResolver());
        builder.setErrorHandler(new EarReaderErrorHandler(LoggerFactory.getLogger(EarBridge.class)));
        return builder.parse(inputStream);
    }

    private DocumentBuilderFactory createDocumentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
        return factory;
    }
}

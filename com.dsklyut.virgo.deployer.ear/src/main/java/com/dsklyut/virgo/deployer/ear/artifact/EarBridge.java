package com.dsklyut.virgo.deployer.ear.artifact;

import com.dsklyut.virgo.deployer.ear.artifact.descriptor.EarDescriptorReader;
import com.dsklyut.virgo.deployer.ear.util.EarUtils;
import org.eclipse.virgo.repository.ArtifactBridge;
import org.eclipse.virgo.repository.ArtifactDescriptor;
import org.eclipse.virgo.repository.ArtifactGenerationException;
import org.eclipse.virgo.repository.HashGenerator;
import org.eclipse.virgo.repository.builder.ArtifactDescriptorBuilder;
import org.eclipse.virgo.repository.builder.AttributeBuilder;
import org.eclipse.virgo.util.common.StringUtils;
import org.eclipse.virgo.util.osgi.manifest.BundleManifest;
import org.osgi.framework.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * User: dsklyut
 * Date: 11/17/10
 * Time: 2:12 PM
 */
@Component
public class EarBridge implements ArtifactBridge {

    private final static String BRIDGE_TYPE = "ear";


    private final HashGenerator hashGenerator;
    private final EarDescriptorReader reader = new EarDescriptorReader();

    @Autowired
    public EarBridge(HashGenerator hashGenerator) {
        Assert.notNull(hashGenerator);
        this.hashGenerator = hashGenerator;
    }


    public ArtifactDescriptor generateArtifactDescriptor(File artifactFile) throws ArtifactGenerationException {
        Reader appDescriptorReader;
//        BundleManifest manifest;

        try {
            appDescriptorReader = EarUtils.readEarApplicationDescriptor(artifactFile);
//            manifest = EarUtils.readBundleManifest(artifactFile, BRIDGE_TYPE);
        } catch (IOException ioe) {
            throw new ArtifactGenerationException("Failed to read manifest from " + artifactFile, BRIDGE_TYPE, ioe);
        }

        throw new UnsupportedOperationException("not implemented yet");
    }


//    private ArtifactDescriptor createDescriptorFromManifest(BundleManifest manifest, File artifactFile) throws
//                                                                                                        ArtifactGenerationException {
//
//        String symbolicName = getApplicationSymbolicName(manifest);
//
//        if (symbolicName == null) {
//            return null;
//        }
//
//        Version version = getApplicationVersion(manifest);
//
//        ArtifactDescriptorBuilder builder = new ArtifactDescriptorBuilder();
//        builder.setType(BRIDGE_TYPE).setName(symbolicName).setVersion(version).setUri(artifactFile.toURI());
//
//        applyAttributeIfPresent(HEADER_APPLICATION_NAME, manifest, builder);
//        applyAttributeIfPresent(HEADER_APPLICATION_DESCRIPTION, manifest, builder);
//
//        this.hashGenerator.generateHash(builder, artifactFile);
//
//        return builder.build();
//    }
//
//    private void applyAttributeIfPresent(String headerName, BundleManifest manifest, ArtifactDescriptorBuilder builder) {
//        String headerValue = manifest.getHeader(headerName);
//        if (headerValue != null) {
//            AttributeBuilder attributeBuilder = new AttributeBuilder();
//            builder.addAttribute(attributeBuilder.setName(headerName).setValue(headerValue).build());
//        }
//    }
//
//    private Version getApplicationVersion(BundleManifest manifest) throws ArtifactGenerationException {
//        String versionString = manifest.getHeader(HEADER_APPLICATION_VERSION);
//        Version version;
//
//        if (!StringUtils.hasText(versionString)) {
//            version = Version.emptyVersion;
//        } else {
//            try {
//                version = new Version(versionString);
//            } catch (IllegalArgumentException iae) {
//                throw new ArtifactGenerationException("Version '" + versionString + "' is ill-formed", iae);
//            }
//        }
//        return version;
//    }
//
//    private String getApplicationSymbolicName(BundleManifest manifest) throws ArtifactGenerationException {
//        String symbolicName = manifest.getHeader(HEADER_APPLICATION_SYMBOLIC_NAME);
//
//        if (!StringUtils.hasText(symbolicName)) {
//            return null;
//        }
//        if (!symbolicName.matches(SYMBOLIC_NAME_REGEX)) {
//            throw new ArtifactGenerationException(HEADER_APPLICATION_SYMBOLIC_NAME + " '" + symbolicName + "' contains illegal characters");
//        }
//        return symbolicName;
//    }
}

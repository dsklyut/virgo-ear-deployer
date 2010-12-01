package com.dsklyut.virgo.deployer.ear.artifact;

import com.dsklyut.virgo.deployer.ear.artifact.descriptor.EarDescriptor;
import com.dsklyut.virgo.deployer.ear.artifact.descriptor.EarDescriptorReader;
import com.dsklyut.virgo.deployer.ear.artifact.descriptor.Module;
import com.dsklyut.virgo.deployer.ear.artifact.descriptor.internal.EarUtils;
import org.eclipse.virgo.repository.ArtifactBridge;
import org.eclipse.virgo.repository.ArtifactDescriptor;
import org.eclipse.virgo.repository.ArtifactGenerationException;
import org.eclipse.virgo.repository.HashGenerator;
import org.eclipse.virgo.repository.builder.ArtifactDescriptorBuilder;
import org.eclipse.virgo.util.common.CollectionUtils;
import org.eclipse.virgo.util.common.StringUtils;
import org.eclipse.virgo.util.io.IOUtils;
import org.osgi.framework.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.Reader;
import java.util.List;

/**
 * User: dsklyut
 * Date: 11/17/10
 * Time: 2:12 PM
 */
@Component
public class EarBridge implements ArtifactBridge {

    private static final String SYMBOLIC_NAME_REGEX = "[-_0-9a-zA-Z]+(\\.[-_0-9a-zA-Z]+)*";
    private final static String BRIDGE_TYPE = "ear";


    private final HashGenerator hashGenerator;
    private final EarDescriptorReader reader = new EarDescriptorReader();

    @Autowired
    public EarBridge(HashGenerator hashGenerator) {
        Assert.notNull(hashGenerator);
        this.hashGenerator = hashGenerator;
    }


    public ArtifactDescriptor generateArtifactDescriptor(File artifactFile) throws ArtifactGenerationException {
        EarDescriptor earDescriptor = null;
        Reader appDescriptorReader = null;
        try {
            // EAR MUST have a META-INF/application.xml
            appDescriptorReader = EarUtils.readEarApplicationDescriptor(artifactFile);
            earDescriptor = reader.read(appDescriptorReader);

            // TODO: figure out if there should be a fallback on the MANIFEST.MF for par/bundle headers? 

        } catch (Exception ex) {
            throw new ArtifactGenerationException("Failed to read application descriptor from " + artifactFile, BRIDGE_TYPE, ex);
        } finally {
            IOUtils.closeQuietly(appDescriptorReader);
        }

        validateDescriptor(earDescriptor);

        return createArtifactDescriptorFromEarDescriptor(earDescriptor, artifactFile);
    }

    private void validateDescriptor(EarDescriptor earDescriptor) throws ArtifactGenerationException {
        List<Module> unsupportedModules = earDescriptor.listUnsupportedModules();
        if (!CollectionUtils.isEmpty(unsupportedModules)) {
            StringBuilder sb = new StringBuilder("Failed to validate deployment.  Contains unsupported deployment types ");
            for (Module m : unsupportedModules) {
                sb.append("'").append(m.getType().getType()).append("' ");
            }
            throw new ArtifactGenerationException(sb.toString(), BRIDGE_TYPE);
        }
    }


    private ArtifactDescriptor createArtifactDescriptorFromEarDescriptor(EarDescriptor descriptor, File artifactFile) throws
                                                                                                                      ArtifactGenerationException {

        String symbolicName = getApplicationSymbolicName(descriptor, artifactFile.getName());

        if (symbolicName == null) {
            return null;
        }

        Version version = getApplicationVersion(descriptor);

        ArtifactDescriptorBuilder builder = new ArtifactDescriptorBuilder();
        builder.setType(BRIDGE_TYPE).setName(symbolicName).setVersion(version).setUri(artifactFile.toURI());

        // TODO: bundle name, description, etc
        // TODO: should we add all found modules right in here like a plan?

        this.hashGenerator.generateHash(builder, artifactFile);

        return builder.build();
    }

    // TODO: figure out how to compute version
    private Version getApplicationVersion(EarDescriptor descriptor) throws ArtifactGenerationException {
        return Version.emptyVersion;
    }


    private boolean isValidSymbolicName(String symbolicName) {
        return !StringUtils.hasText(symbolicName) && !symbolicName.matches(SYMBOLIC_NAME_REGEX);
    }

    private String getApplicationSymbolicName(EarDescriptor descriptor, String fallback) throws ArtifactGenerationException {

        String symbolicName = descriptor.getApplicationName();

        if (isValidSymbolicName(symbolicName)) {
            return symbolicName;
        }

        symbolicName = StringUtils.stripFilenameExtension(StringUtils.getFilename(fallback));
        // otherwise try fallback (i.e. filename)
        if (isValidSymbolicName(symbolicName)) {
            return symbolicName;
        }

        throw new ArtifactGenerationException(String.format("Error validating application symbolic name.  Both application-name '%s' and fallback file name '%s' a contains illegal characters",
                                                            descriptor.getApplicationName(),
                                                            symbolicName));
    }
}

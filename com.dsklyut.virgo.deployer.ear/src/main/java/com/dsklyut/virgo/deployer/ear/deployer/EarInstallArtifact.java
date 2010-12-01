package com.dsklyut.virgo.deployer.ear.deployer;

import org.eclipse.virgo.kernel.artifact.ArtifactSpecification;
import org.eclipse.virgo.kernel.deployer.core.DeploymentException;
import org.eclipse.virgo.kernel.install.artifact.ArtifactIdentity;
import org.eclipse.virgo.kernel.install.artifact.ArtifactIdentityDeterminer;
import org.eclipse.virgo.kernel.install.artifact.ArtifactStorage;
import org.eclipse.virgo.kernel.install.artifact.InstallArtifactTreeFactory;
import org.eclipse.virgo.kernel.install.artifact.ScopeServiceRepository;
import org.eclipse.virgo.kernel.install.artifact.internal.ArtifactStateMonitor;
import org.eclipse.virgo.kernel.install.artifact.internal.ArtifactStorageFactory;
import org.eclipse.virgo.kernel.install.artifact.internal.InstallArtifactRefreshHandler;
import org.eclipse.virgo.kernel.install.artifact.internal.StandardPlanInstallArtifact;
import org.eclipse.virgo.kernel.serviceability.NonNull;
import org.eclipse.virgo.kernel.shim.scope.ScopeFactory;
import org.eclipse.virgo.medic.eventlog.EventLogger;

import java.util.Collections;

/**
 * User: dsklyut
 * Date: 11/22/10
 * Time: 10:43 AM
 */
public class EarInstallArtifact extends StandardPlanInstallArtifact {


    protected EarInstallArtifact(@NonNull ArtifactIdentity identity,
                                 @NonNull ArtifactStorage artifactStorage,
                                 @NonNull ArtifactStateMonitor artifactStateMonitor,
                                 @NonNull ScopeServiceRepository scopeServiceRepository,
                                 @NonNull ScopeFactory scopeFactory,
                                 @NonNull EventLogger eventLogger,
                                 @NonNull InstallArtifactTreeFactory bundleInstallArtifactTreeFactory,
                                 @NonNull InstallArtifactRefreshHandler refreshHandler,
                                 String repositoryName,
                                 @NonNull InstallArtifactTreeFactory configInstallArtifactTreeFactory,
                                 @NonNull ArtifactStorageFactory artifactStorageFactory,
                                 @NonNull ArtifactIdentityDeterminer artifactIdentityDeterminer) throws DeploymentException {
        super(identity,
              true,
              true,
              artifactStorage,
              artifactStateMonitor,
              scopeServiceRepository,
              scopeFactory,
              eventLogger,
              refreshHandler,
              repositoryName,
              Collections.<ArtifactSpecification>emptyList());
    }


}

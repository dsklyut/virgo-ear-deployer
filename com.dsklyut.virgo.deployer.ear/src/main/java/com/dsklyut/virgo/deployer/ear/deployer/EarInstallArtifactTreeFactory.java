package com.dsklyut.virgo.deployer.ear.deployer;

import org.eclipse.virgo.kernel.deployer.core.DeploymentException;
import org.eclipse.virgo.kernel.install.artifact.ArtifactIdentity;
import org.eclipse.virgo.kernel.install.artifact.ArtifactIdentityDeterminer;
import org.eclipse.virgo.kernel.install.artifact.ArtifactStorage;
import org.eclipse.virgo.kernel.install.artifact.InstallArtifact;
import org.eclipse.virgo.kernel.install.artifact.InstallArtifactTreeFactory;
import org.eclipse.virgo.kernel.install.artifact.ScopeServiceRepository;
import org.eclipse.virgo.kernel.install.artifact.internal.ArtifactStateMonitor;
import org.eclipse.virgo.kernel.install.artifact.internal.ArtifactStorageFactory;
import org.eclipse.virgo.kernel.install.artifact.internal.InstallArtifactRefreshHandler;
//import org.eclipse.virgo.kernel.install.artifact.internal.ParPlanInstallArtifact;
import org.eclipse.virgo.kernel.serviceability.NonNull;
import org.eclipse.virgo.kernel.shim.scope.ScopeFactory;
import org.eclipse.virgo.medic.eventlog.EventLogger;
import org.eclipse.virgo.util.common.ThreadSafeArrayListTree;
import org.eclipse.virgo.util.common.Tree;
import org.osgi.framework.BundleContext;

import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: dsklyut Date: 11/22/10 Time: 11:33 AM
 */
public class EarInstallArtifactTreeFactory implements InstallArtifactTreeFactory {

    private final BundleContext bundleContext;

    private final ScopeServiceRepository scopeServiceRepository;

    private final ScopeFactory scopeFactory;

    private final EventLogger eventLogger;

    private final InstallArtifactRefreshHandler refreshHandler;

    private final InstallArtifactTreeFactory bundleInstallArtifactTreeFactory;
    private final ArtifactStorageFactory artifactStorageFactory;
    private final ArtifactIdentityDeterminer artifactIdentityDeterminer;

    public EarInstallArtifactTreeFactory(@NonNull BundleContext bundleContext,
                                         @NonNull ScopeServiceRepository scopeServiceRepository,
                                         @NonNull ScopeFactory scopeFactory,
                                         @NonNull EventLogger eventLogger,
                                         @NonNull InstallArtifactRefreshHandler refreshHandler,
                                         @NonNull InstallArtifactTreeFactory bundleInstallArtifactTreeFactory,
                                         @NonNull ArtifactStorageFactory artifactStorageFactory,
                                         @NonNull ArtifactIdentityDeterminer artifactIdentityDeterminer) {
        this.bundleContext = bundleContext;
        this.scopeServiceRepository = scopeServiceRepository;
        this.scopeFactory = scopeFactory;
        this.eventLogger = eventLogger;
        this.refreshHandler = refreshHandler;
        this.bundleInstallArtifactTreeFactory = bundleInstallArtifactTreeFactory;

        // not sure if that is needed or how to get artifactIdentityDeterminer injected if coming from external bundle.
        this.artifactStorageFactory = artifactStorageFactory;
        this.artifactIdentityDeterminer = artifactIdentityDeterminer;
    }

    @Override
    public Tree<InstallArtifact> constructInstallArtifactTree(ArtifactIdentity artifactIdentity,
                                                              ArtifactStorage artifactStorage,
                                                              Map<String, String> deploymentProperties,
                                                              String repositoryName) throws DeploymentException {

        ArtifactStateMonitor monitor = new ArtifactStateMonitor(this.bundleContext);
        return constructInstallTree(new EarInstallArtifact(artifactIdentity,
                                                           artifactStorage,
                                                           monitor,
                                                           scopeServiceRepository,
                                                           scopeFactory,
                                                           eventLogger,
                                                           bundleInstallArtifactTreeFactory,
                                                           refreshHandler,
                                                           repositoryName,
                                                           null,
                                                           artifactStorageFactory,
                                                           artifactIdentityDeterminer));
    }

//    private Tree<InstallArtifact> createParTree(ArtifactIdentity artifactIdentity, ArtifactStorage artifactStorage, String repositoryName)
//        throws DeploymentException {
//
//        ParPlanInstallArtifact parArtifact = this.parFactory.createParPlanInstallArtifact(artifactIdentity, artifactStorage, repositoryName);
//        Tree<InstallArtifact> tree = constructInstallTree(parArtifact);
//        parArtifact.setTree(tree);
//        return tree;
//    }

    private Tree<InstallArtifact> constructInstallTree(InstallArtifact rootArtifact) {
        return new ThreadSafeArrayListTree<InstallArtifact>(rootArtifact);
    }
}

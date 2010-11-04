package org.sonatype.aether;

/*******************************************************************************
 * Copyright (c) 2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

import org.sonatype.aether.transfer.TransferListener;

/**
 * A listener being notified of events from the repository system. The listener may be called from an arbitrary thread.
 * <em>Note:</em> Implementors are strongly advised to inherit from {@link AbstractRepositoryListener} instead of
 * directly implementing this interface.
 * 
 * @author Benjamin Bentmann
 * @see TransferListener
 */
public interface RepositoryListener
{

    void artifactDescriptorInvalid( RepositoryEvent event );

    void artifactDescriptorMissing( RepositoryEvent event );

    void metadataInvalid( RepositoryEvent event );

    void artifactResolving( RepositoryEvent event );

    void artifactResolved( RepositoryEvent event );

    void metadataResolving( RepositoryEvent event );

    void metadataResolved( RepositoryEvent event );

    void artifactInstalling( RepositoryEvent event );

    void artifactInstalled( RepositoryEvent event );

    void metadataInstalling( RepositoryEvent event );

    void metadataInstalled( RepositoryEvent event );

    void artifactDeploying( RepositoryEvent event );

    void artifactDeployed( RepositoryEvent event );

    void metadataDeploying( RepositoryEvent event );

    void metadataDeployed( RepositoryEvent event );

}

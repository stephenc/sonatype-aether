package org.sonatype.aether.impl.internal;

/*******************************************************************************
 * Copyright (c) 2010-2011 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * The Apache License v2.0 is available at
 *   http://www.apache.org/licenses/LICENSE-2.0.html
 * You may elect to redistribute this code under either of these licenses.
 *******************************************************************************/

import org.sonatype.aether.RepositoryEvent;
import org.sonatype.aether.RepositoryListener;
import org.sonatype.aether.impl.RepositoryEventDispatcher;

/**
 * @author Benjamin Bentmann
 */
public class StubRepositoryEventDispatcher
    implements RepositoryEventDispatcher
{

    public void dispatch( RepositoryEvent event )
    {
        RepositoryListener listener = event.getSession().getRepositoryListener();
        if ( listener == null )
        {
            return;
        }

        switch ( event.getType() )
        {
            case ARTIFACT_DEPLOYED:
                listener.artifactDeployed( event );
                break;
            case ARTIFACT_DEPLOYING:
                listener.artifactDeploying( event );
                break;
            case ARTIFACT_DESCRIPTOR_INVALID:
                listener.artifactDescriptorInvalid( event );
                break;
            case ARTIFACT_DESCRIPTOR_MISSING:
                listener.artifactDescriptorMissing( event );
                break;
            case ARTIFACT_DOWNLOADED:
                listener.artifactDownloaded( event );
                break;
            case ARTIFACT_DOWNLOADING:
                listener.artifactDownloading( event );
                break;
            case ARTIFACT_INSTALLED:
                listener.artifactInstalled( event );
                break;
            case ARTIFACT_INSTALLING:
                listener.artifactInstalling( event );
                break;
            case ARTIFACT_RESOLVED:
                listener.artifactResolved( event );
                break;
            case ARTIFACT_RESOLVING:
                listener.artifactResolving( event );
                break;
            case METADATA_DEPLOYED:
                listener.metadataDeployed( event );
                break;
            case METADATA_DEPLOYING:
                listener.metadataDeploying( event );
                break;
            case METADATA_DOWNLOADED:
                listener.metadataDownloaded( event );
                break;
            case METADATA_DOWNLOADING:
                listener.metadataDownloading( event );
                break;
            case METADATA_INSTALLED:
                listener.metadataInstalled( event );
                break;
            case METADATA_INSTALLING:
                listener.metadataInstalling( event );
                break;
            case METADATA_INVALID:
                listener.metadataInvalid( event );
                break;
            case METADATA_RESOLVED:
                listener.metadataResolved( event );
                break;
            case METADATA_RESOLVING:
                listener.metadataResolving( event );
                break;
            default:
                throw new IllegalStateException( "unknown repository event type " + event.getType() );
        }
    }

}

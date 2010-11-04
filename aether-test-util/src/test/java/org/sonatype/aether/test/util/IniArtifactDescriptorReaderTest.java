package org.sonatype.aether.test.util;

/*******************************************************************************
 * Copyright (c) 2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.Exclusion;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactDescriptorException;
import org.sonatype.aether.resolution.ArtifactDescriptorRequest;
import org.sonatype.aether.resolution.ArtifactDescriptorResult;
import org.sonatype.aether.test.impl.TestRepositorySystemSession;
import org.sonatype.aether.test.util.impl.StubArtifact;

/**
 * @author Benjamin Hanzelmann
 *
 */
public class IniArtifactDescriptorReaderTest
{

    private IniArtifactDescriptorReader reader;

    private RepositorySystemSession session;

    @Before
    public void setup()
    {
        reader = new IniArtifactDescriptorReader( "org/sonatype/aether/test/util/" );
        session = new TestRepositorySystemSession();
    }

    @Test( expected = ArtifactDescriptorException.class )
    public void testMissingDescriptor()
        throws ArtifactDescriptorException
    {
        StubArtifact art = new StubArtifact( "missing:aid:ver:ext" );
        ArtifactDescriptorRequest request = new ArtifactDescriptorRequest( art, null, "" );
        reader.readArtifactDescriptor( session, request );
    }

    @Test
    public void testLookup()
        throws ArtifactDescriptorException
    {
        StubArtifact art = new StubArtifact( "gid:aid:ext:ver" );
        ArtifactDescriptorRequest request = new ArtifactDescriptorRequest( art, null, "" );
        ArtifactDescriptorResult description = reader.readArtifactDescriptor( session, request );
        
        assertEquals(request, description.getRequest());
        assertEquals( art, description.getArtifact() );
        
        assertEquals( 1, description.getRelocations().size() );
        Artifact artifact = description.getRelocations().get( 0 );
        assertEquals( "gid", artifact.getGroupId() );
        assertEquals( "aid", artifact.getArtifactId() );
        assertEquals( "ver", artifact.getVersion() );
        assertEquals( "ext", artifact.getExtension() );

        assertEquals( 1, description.getRepositories().size() );
        RemoteRepository repo = description.getRepositories().get( 0 );
        assertEquals( "id", repo.getId() );
        assertEquals( "type", repo.getContentType() );
        assertEquals( "protocol://some/url?for=testing", repo.getUrl() );

        assertDependencies( description.getDependencies() );
        assertDependencies( description.getManagedDependencies() );

    }

    private void assertDependencies( List<Dependency> deps )
    {
        assertEquals( 4, deps.size() );

        Dependency dep = deps.get( 0 );
        assertEquals( "scope", dep.getScope() );
        assertEquals( false, dep.isOptional() );
        assertEquals( 2, dep.getExclusions().size() );
        Iterator<Exclusion> it = dep.getExclusions().iterator();
        Exclusion excl = it.next();
        assertEquals( "gid3", excl.getGroupId() );
        assertEquals( "aid", excl.getArtifactId() );
        excl = it.next();
        assertEquals( "gid2", excl.getGroupId() );
        assertEquals( "aid2", excl.getArtifactId() );

        Artifact art = dep.getArtifact();
        assertEquals( "gid", art.getGroupId() );
        assertEquals( "aid", art.getArtifactId() );
        assertEquals( "ver", art.getVersion() );
        assertEquals( "ext", art.getExtension() );
        
        dep = deps.get( 1 );
        assertEquals( "scope", dep.getScope() );
        assertEquals( true, dep.isOptional() );
        assertEquals( 0, dep.getExclusions().size() );

        art = dep.getArtifact();
        assertEquals( "gid", art.getGroupId() );
        assertEquals( "aid2", art.getArtifactId() );
        assertEquals( "ver", art.getVersion() );
        assertEquals( "ext", art.getExtension() );

        dep = deps.get( 2 );
        assertEquals( "scope", dep.getScope() );
        assertEquals( true, dep.isOptional() );
        assertEquals( 0, dep.getExclusions().size() );

        art = dep.getArtifact();
        assertEquals( "gid", art.getGroupId() );
        assertEquals( "aid", art.getArtifactId() );
        assertEquals( "ver3", art.getVersion() );
        assertEquals( "ext", art.getExtension() );

        dep = deps.get( 3 );
        assertEquals( "scope5", dep.getScope() );
        assertEquals( true, dep.isOptional() );
        assertEquals( 0, dep.getExclusions().size() );

        art = dep.getArtifact();
        assertEquals( "gid1", art.getGroupId() );
        assertEquals( "aid", art.getArtifactId() );
        assertEquals( "ver", art.getVersion() );
        assertEquals( "ext", art.getExtension() );
    }

}

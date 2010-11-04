package org.sonatype.aether.version;

/*******************************************************************************
 * Copyright (c) 2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

import org.sonatype.aether.RepositoryException;

/**
 * Thrown when a version or version range could not be parsed.
 * 
 * @author Benjamin Bentmann
 */
public class InvalidVersionSpecificationException
    extends RepositoryException
{

    private final String version;

    public InvalidVersionSpecificationException( String version, String message )
    {
        super( message );
        this.version = version;
    }

    public InvalidVersionSpecificationException( String version, Throwable cause )
    {
        super( "Could not parse version specification " + version + getMessage( ": ", cause ), cause );
        this.version = version;
    }

    public String getVersion()
    {
        return version;
    }

}

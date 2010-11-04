package org.sonatype.aether.spi.log;

/*******************************************************************************
 * Copyright (c) 2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

/**
 * A simple logger to facilitate emission of debug messages.
 * 
 * @author Benjamin Bentmann
 */
public interface Logger
{

    /**
     * Indicates whether debug logging is enabled.
     * 
     * @return {@code true} if debug logging is enabled, {@code false} otherwise.
     */
    boolean isDebugEnabled();

    /**
     * Emits the specified message.
     * 
     * @param msg The message to log, must not be {@code null}.
     */
    void debug( String msg );

    /**
     * Emits the specified message along with a stack trace of the given exception.
     * 
     * @param msg The message to log, must not be {@code null}.
     * @param error The exception to log, may be {@code null}.
     */
    void debug( String msg, Throwable error );

}

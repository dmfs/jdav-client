/*
 * Copyright (C) 2015 Marten Gajda <marten@dmfs.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package org.dmfs.davclient;

import org.dmfs.httpclientinterfaces.ContentType;


/**
 * A collection of various constant values that are used throughout the project.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class Constants
{
	/**
	 * The name of the Depth header.
	 */
	public final static String HEADER_DEPTH = "Depth";

	/**
	 * The name of the prefer header.
	 */
	public final static String HEADER_PREFER = "Prefer";

	/**
	 * The preferred content type for DAV XML responses and requests.
	 */
	public final static ContentType CONTENT_TYPE_APPLICATION_XML = new ContentType("application/xml", ContentType.CharSet("utf-8"));

	/**
	 * The alternate content type for DAV XML responses and requests.
	 */
	public final static ContentType CONTENT_TYPE_TEXT_XML = new ContentType("text/xml", ContentType.CharSet("utf-8"));

}

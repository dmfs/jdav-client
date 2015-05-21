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

import org.dmfs.httpclientinterfaces.IHttpRequest;


/**
 * Base class for all DAV requests.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 * 
 * @param <T>
 *            The type of the expected response.
 */
public abstract class BaseDavRequest<T> implements IHttpRequest<T>
{
	/**
	 * The {@link DavContext} to use for the request.
	 */
	protected final DavContext mDavContext;


	public BaseDavRequest(DavContext davContext)
	{
		mDavContext = davContext;
	}
}

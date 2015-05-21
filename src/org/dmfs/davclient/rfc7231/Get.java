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

package org.dmfs.davclient.rfc7231;

import org.dmfs.httpclientinterfaces.HttpMethod;
import org.dmfs.httpclientinterfaces.IHttpRequest;
import org.dmfs.httpclientinterfaces.IHttpRequestEntity;


/**
 * An abstract GET request
 * 
 * @author Marten Gajda <marten@dmfs.org>
 *
 * @param <T>
 *            The type of the GET response.
 */
public abstract class Get<T> implements IHttpRequest<T>
{

	@Override
	public final HttpMethod getMethod()
	{
		return HttpMethod.GET;
	}


	@Override
	public final IHttpRequestEntity getRequestEntity()
	{
		return null;
	}

}

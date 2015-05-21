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

package org.dmfs.davclient.rfc3253;

import java.io.IOException;

import org.dmfs.dav.PropertyRequest;
import org.dmfs.dav.rfc4918.Depth;
import org.dmfs.dav.rfc4918.MultiStatus;
import org.dmfs.davclient.DavContext;
import org.dmfs.davclient.MultistatusResponseReader;
import org.dmfs.httpclientinterfaces.HttpStatus;
import org.dmfs.httpclientinterfaces.IHttpResponse;
import org.dmfs.httpclientinterfaces.IResponseHandler;
import org.dmfs.httpclientinterfaces.exceptions.ProtocolError;
import org.dmfs.httpclientinterfaces.exceptions.ProtocolException;
import org.dmfs.xmlobjects.ElementDescriptor;


/**
 * Base class for {@link Report}s that request specific DAV properties and expect a {@link MultiStatus} response in return.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public abstract class MultiStatusReport extends Report<MultistatusResponseReader>
{
	/**
	 * The request object.
	 */
	protected PropertyRequest mRequest;


	/**
	 * Constructor of a {@link MultiStatusReport} using the given {@link DavContext} and {@link Depth}.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            The value of the {@link Depth} header to send, may be <code>null</code> to send no Depth header.
	 */
	public MultiStatusReport(DavContext davContext, Depth depth)
	{
		super(davContext, depth);
	}


	/**
	 * Add properties to the request.
	 * 
	 * @param properties
	 *            The {@link ElementDescriptor}s of the DAV properties to request.
	 * @return This instance.
	 */
	public MultiStatusReport addProperties(ElementDescriptor<?>... properties)
	{
		if (properties != null)
		{
			for (ElementDescriptor<?> property : properties)
			{
				mRequest.addProperty(property);
			}
		}
		return this;
	}


	/**
	 * Remove properties from the request.
	 * 
	 * @param properties
	 *            The {@link ElementDescriptor}s of the DAV properties to remove.
	 * @return This instance.
	 */
	public MultiStatusReport removeProperties(ElementDescriptor<?>... properties)
	{
		if (properties != null)
		{
			for (ElementDescriptor<?> property : properties)
			{
				mRequest.removeProperty(property);
			}
		}
		return this;
	}


	@Override
	public IResponseHandler<MultistatusResponseReader> getResponseHandler(IHttpResponse response) throws IOException, ProtocolError, ProtocolException
	{
		if (response.getStatusCode() == HttpStatus.MULTISTATUS)
		{
			return mDavContext.getMultistatusResponseHandler();
		}

		return null;
	}

}

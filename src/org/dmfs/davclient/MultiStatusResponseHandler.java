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

import java.io.IOException;

import org.dmfs.dav.DavParserContext;
import org.dmfs.httpclientinterfaces.IHttpResponse;
import org.dmfs.httpclientinterfaces.IHttpResponseEntity;
import org.dmfs.httpclientinterfaces.IResponseHandler;
import org.dmfs.httpclientinterfaces.Utils;
import org.dmfs.httpclientinterfaces.exceptions.ProtocolError;
import org.dmfs.httpclientinterfaces.exceptions.ProtocolException;


/**
 * An {@link IResponseHandler} for DAV multistatus responses.
 * <p>
 * The {@link #handleResponse(IHttpResponse)} method will return a {@link MultistatusResponseReader} object to read the response.
 * </p>
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class MultiStatusResponseHandler implements IResponseHandler<MultistatusResponseReader>
{

	private final DavParserContext mParserContext;


	/**
	 * Create a new {@link MultiStatusResponseHandler} using the given {@link DavParserContext}.
	 * 
	 * @param parserContext
	 *            A {@link DavParserContext}.
	 */
	public MultiStatusResponseHandler(DavParserContext parserContext)
	{
		if (parserContext == null)
		{
			throw new IllegalArgumentException("DavParserContext must not be null");
		}
		mParserContext = parserContext;
	}


	@Override
	public MultistatusResponseReader handleResponse(IHttpResponse response) throws IOException, ProtocolError, ProtocolException
	{
		IHttpResponseEntity entity = response.getContentEntity();

		/*
		 * Check that the content-type is correct. The specs say that application/xml is the right one, but text/xml should be tolerated.
		 */
		if (!Utils.verifyContentType(entity, Constants.CONTENT_TYPE_APPLICATION_XML, Constants.CONTENT_TYPE_TEXT_XML))
		{
			throw new ProtocolException("invalid content-type '" + entity.getContentType() + "'");
		}

		return new MultistatusResponseReader(response, mParserContext);
	}

}

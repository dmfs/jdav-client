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

import java.io.IOException;

import org.dmfs.httpclientinterfaces.HttpMethod;
import org.dmfs.httpclientinterfaces.HttpStatus;
import org.dmfs.httpclientinterfaces.IHeaderEditor;
import org.dmfs.httpclientinterfaces.IHttpRequest;
import org.dmfs.httpclientinterfaces.IHttpRequestEntity;
import org.dmfs.httpclientinterfaces.IHttpResponse;
import org.dmfs.httpclientinterfaces.IResponseHandler;
import org.dmfs.httpclientinterfaces.exceptions.ProtocolError;
import org.dmfs.httpclientinterfaces.exceptions.ProtocolException;


/**
 * An OPTIONS request.
 * <p>
 * TODO: at present this class doesn't return a response handler.
 * </p>
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class Options implements IHttpRequest<HttpOptions>
{

	private final static IResponseHandler<HttpOptions> HANDLER = new IResponseHandler<HttpOptions>()
	{

		@Override
		public HttpOptions handleResponse(IHttpResponse response) throws IOException, ProtocolError, ProtocolException
		{
			return new HttpOptions(response);
		}
	};


	@Override
	public HttpMethod getMethod()
	{
		return HttpMethod.OPTIONS;
	}


	@Override
	public IHttpRequestEntity getRequestEntity()
	{
		// no request body needed
		return null;
	}


	@Override
	public IResponseHandler<HttpOptions> getResponseHandler(IHttpResponse response) throws IOException, ProtocolError, ProtocolException
	{
		int statusCode = response.getStatusCode();
		if (statusCode != HttpStatus.OK && statusCode != HttpStatus.NO_CONTENT)
		{
			return null;
		}
		return HANDLER;
	}


	@Override
	public void updateHeaders(IHeaderEditor headerEditor)
	{
		// not needed
	}

}

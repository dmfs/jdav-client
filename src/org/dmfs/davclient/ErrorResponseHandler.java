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
import org.dmfs.dav.rfc4918.Error;
import org.dmfs.dav.rfc4918.MultiStatus;
import org.dmfs.dav.rfc4918.WebDav;
import org.dmfs.httpclientinterfaces.IHttpResponse;
import org.dmfs.httpclientinterfaces.IResponseHandler;
import org.dmfs.httpclientinterfaces.exceptions.ProtocolError;
import org.dmfs.xmlobjects.pull.XmlObjectPullParserException;
import org.xmlpull.v1.XmlPullParserException;


/**
 * {@link IResponseHandler} for error responses.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class ErrorResponseHandler extends XmlResponseReader<Error>
{

	public ErrorResponseHandler(IHttpResponse response, DavParserContext context) throws IOException, ProtocolError
	{
		super(response, context);
	}


	/**
	 * Returns the error element.
	 * 
	 * @return The {@link MultiStatus} object.
	 * @throws IOException
	 * @throws ProtocolError
	 */
	public Error getError() throws IOException, ProtocolError
	{
		try
		{
			return mObjectPull.pull(WebDav.ERROR, null, EMPTY_PATH);
		}
		catch (XmlObjectPullParserException e)
		{
			throw new ProtocolError("can not read error", e);
		}
		catch (XmlPullParserException e)
		{
			throw new ProtocolError("can not read error", e);
		}
	}
}

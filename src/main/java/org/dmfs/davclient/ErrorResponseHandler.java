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

import org.dmfs.dav.DavParserContext;
import org.dmfs.dav.rfc4918.Error;
import org.dmfs.dav.rfc4918.MultiStatus;
import org.dmfs.dav.rfc4918.WebDav;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.xmlobjects.pull.XmlObjectPullParserException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


/**
 * {@link HttpResponseHandler} for error responses.
 */
public class ErrorResponseHandler extends XmlResponseReader<Error>
{

    public ErrorResponseHandler(HttpResponse response, DavParserContext context) throws IOException, ProtocolError
    {
        super(response, context);
    }


    /**
     * Returns the error element.
     *
     * @return The {@link MultiStatus} object.
     */
    public Error getError() throws IOException, ProtocolError
    {
        try
        {
            return mObjectPull.pull(WebDav.ERROR, null, EMPTY_PATH);
        }
        catch (XmlObjectPullParserException | XmlPullParserException e)
        {
            throw new ProtocolError("can not read error", e);
        }
    }
}

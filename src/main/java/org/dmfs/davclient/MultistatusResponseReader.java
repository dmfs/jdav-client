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
import org.dmfs.dav.rfc4918.MultiStatus;
import org.dmfs.dav.rfc4918.Response;
import org.dmfs.dav.rfc4918.WebDav;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.xmlobjects.pull.XmlObjectPullParserException;
import org.dmfs.xmlobjects.pull.XmlPath;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URI;


/**
 * {@link HttpResponseHandler} for multistatus responses. This class provides methods to retrieve the individual response objects or the entire multistatus
 * response.
 */
public class MultistatusResponseReader extends XmlResponseReader<MultiStatus>
{
    private final static XmlPath MULTISTATUS_PATH = new XmlPath(WebDav.MULTISTATUS);

    /**
     * The {@link URI} of the server the response came from.
     */
    private final URI mUri;


    /**
     * Creates a {@link MultistatusResponseReader} reading the given {@link HttpResponse} using the given {@link DavParserContext}.
     *
     * @param response
     *     The {@link HttpResponse} to read.
     * @param context
     *     A {@link DavParserContext}.
     */
    public MultistatusResponseReader(HttpResponse response, DavParserContext context) throws IOException, ProtocolError
    {
        super(response, context);
        mUri = response.responseUri();
    }


    /**
     * Checks if there is another {@link Response} to read. If there are not other responses you should call {@link #close()} to make sure all resources are
     * released properly.
     *
     * @return <code>true</code> if there is at least one more response, <code>false</code> otherwise.
     */
    public boolean hasNextResponse() throws IOException, ProtocolError
    {
        try
        {
            return mObjectPull.moveToNextSibling(WebDav.RESPONSE, MULTISTATUS_PATH);
        }
        catch (XmlPullParserException | XmlObjectPullParserException e)
        {
            throw new ProtocolError("can not read response", e);
        }
    }


    /**
     * Read the next {@link Response}.
     *
     * @param recycle
     *     A {@link Response} that can be recycled or <code>null</code>.
     *
     * @return The next response or <code>null</code> if there is no other response.
     */
    public Response getNextResponse(Response recycle) throws IOException, ProtocolError
    {
        try
        {
            Response response = mObjectPull.pull(WebDav.RESPONSE, recycle, MULTISTATUS_PATH);
            // resolve hrefs, because the caller wouldn't know the correct URI if the request has been redirected.
            response.resolveHRefs(mUri);
            return response;
        }
        catch (XmlPullParserException | XmlObjectPullParserException e)
        {
            throw new ProtocolError("can not read response", e);
        }
    }


    /**
     * Parse the entire multistatus response and return it. All {@link Response}s that have not been pulled by {@link #getNextResponse(Response)} will be added
     * to the {@link MultiStatus} object.
     * <p>
     * You should {@link #close()} this object after calling this.
     * </p>
     *
     * @return The {@link MultiStatus} object.
     */
    public MultiStatus getMultistatus() throws ProtocolError, IOException
    {
        try
        {
            MultiStatus result = mObjectPull.pull(WebDav.MULTISTATUS, null, EMPTY_PATH);
            if (result == null)
            {
                throw new ProtocolError("can not read multistatus element!");
            }

            // resolve hrefs, because the caller wouldn't know the correct URI if the request has been redirected.
            result.resolveHRefs(mUri);

            return result;
        }
        catch (XmlObjectPullParserException | XmlPullParserException e)
        {
            throw new ProtocolError("can not read multistatus", e);
        }
    }


    @Override
    public void close() throws IOException
    {
        // make sure we've read the entire response before closing the stream, otherwise some implementations can reuse the connection.
        try
        {
            getMultistatus();
        }
        catch (ProtocolError e)
        {
            throw new IOException("can't read response", e);
        }
        finally
        {
            super.close();
        }
    }
}

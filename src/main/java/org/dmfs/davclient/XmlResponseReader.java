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
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.optional.decorators.Mapped;
import org.dmfs.jems.single.combined.Backed;
import org.dmfs.xmlobjects.pull.XmlObjectPull;
import org.dmfs.xmlobjects.pull.XmlPath;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;


/**
 * An abstract wrapper for XML responses.
 */
public abstract class XmlResponseReader<T>
{
    protected final static XmlPath EMPTY_PATH = new XmlPath();

    /**
     * The {@link XmlObjectPull} parser for the content.
     */
    protected final XmlObjectPull mObjectPull;

    private final InputStream mInput;


    /**
     * Creates a handler for the given {@link HttpResponse} and {@link DavParserContext}.
     *
     * @param response
     *     The response to handle
     * @param context
     *     A {@link DavParserContext}.
     */
    public XmlResponseReader(HttpResponse response, DavParserContext context) throws IOException, ProtocolError
    {
        try
        {
            HttpResponseEntity content = response.responseEntity();

            if (content == null)
            {
                // no content, nothing to do
                mObjectPull = null;
                mInput = null;
                return;
            }

            // check the content type first
            Optional<MediaType> contentType = content.contentType();

            // get a pull parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            // use the charset param of the response, if there is any
            String charset = new Backed<String>(new Mapped<>(ct -> ct.charset("UTF-8"), contentType), () -> "UTF-8").value();

            mInput = content.contentStream();

            parser.setInput(mInput, charset);

            mObjectPull = new XmlObjectPull(parser, context);
        }
        catch (XmlPullParserException e)
        {
            throw new ProtocolError("can not read multistatus response", e);
        }
    }


    /**
     * Close the input stream. Depending on your HTTP client implementation, this may be necessary to re-use the connection for further requests.
     * <p>
     * Don't use this instance after closing the input stream.
     * </p>
     */
    public void close() throws IOException
    {
        if (mInput != null)
        {
            mInput.close();
        }
    }
}

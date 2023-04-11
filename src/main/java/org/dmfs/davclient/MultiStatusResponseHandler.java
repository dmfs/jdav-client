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
import org.dmfs.davclient.utils.TypeEquals;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.jems.optional.decorators.Sieved;
import org.dmfs.jems.predicate.composite.AnyOf;

import java.io.IOException;

import static org.dmfs.davclient.Constants.CONTENT_TYPE_APPLICATION_XML;
import static org.dmfs.davclient.Constants.CONTENT_TYPE_TEXT_XML;


/**
 * An {@link HttpResponseHandler} for DAV multistatus responses.
 * <p>
 * The {@link #handleResponse(HttpResponse)} method will return a {@link MultistatusResponseReader} object to read the response.
 * </p>
 */
public class MultiStatusResponseHandler implements HttpResponseHandler<MultistatusResponseReader>
{

    private final DavParserContext mParserContext;


    /**
     * Create a new {@link MultiStatusResponseHandler} using the given {@link DavParserContext}.
     *
     * @param parserContext
     *     A {@link DavParserContext}.
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
    public MultistatusResponseReader handleResponse(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        HttpResponseEntity entity = response.responseEntity();

        /*
         * Check that the content-type is correct. The specs say that application/xml is the right one, but text/xml should be tolerated.
         */
        if (!new Sieved<>(
            new AnyOf<>(new TypeEquals(CONTENT_TYPE_APPLICATION_XML), new TypeEquals(CONTENT_TYPE_TEXT_XML)),
            entity.contentType()).isPresent())
        {
            throw new ProtocolException("invalid content-type '" + entity.contentType() + "'");
        }

        return new MultistatusResponseReader(response, mParserContext);
    }
}

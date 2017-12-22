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

package org.dmfs.davclient.rfc4918;

import org.dmfs.dav.rfc4918.PropertyUpdate;
import org.dmfs.dav.rfc4918.WebDav;
import org.dmfs.davclient.BaseDavRequest;
import org.dmfs.davclient.DavContext;
import org.dmfs.davclient.MultistatusResponseReader;
import org.dmfs.davclient.XmlRequestEntity;
import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.methods.IdempotentMethod;
import org.dmfs.httpessentials.responsehandlers.FailResponseHandler;
import org.dmfs.xmlobjects.ElementDescriptor;

import java.io.IOException;


/**
 * Creates a request to alter or remove DAV properties on a specific resource as specified in <a href="http://tools.ietf.org/html/rfc4918#section-9.2">RFC 4918,
 * section 9.2</a>.
 */
public final class PropPatch extends BaseDavRequest<MultistatusResponseReader>
{
    private final PropertyUpdate mRequest;


    /**
     * Create a new {@link PropPatch} request.
     *
     * @param davContext
     *     A {@link DavContext}.
     */
    public PropPatch(DavContext davContext)
    {
        super(davContext);
        mRequest = new PropertyUpdate();
    }


    /**
     * Set the given property (identified by its {@link ElementDescriptor}) to the given value.
     *
     * @param property
     *     The {@link ElementDescriptor} of the property.
     * @param value
     *     The new value of the property.
     *
     * @return This instance.
     */
    public <T> PropPatch setProperty(ElementDescriptor<T> property, T value)
    {
        mRequest.set(property, value);
        return this;
    }


    /**
     * Remove the given property (identified by its {@link ElementDescriptor}).
     *
     * @param property
     *     The {@link ElementDescriptor} of the property.
     *
     * @return This instance.
     */
    public PropPatch deleteProperty(ElementDescriptor<?> property)
    {
        mRequest.remove(property);
        return this;
    }


    /**
     * Remove all changes for the given property (identified by its {@link ElementDescriptor}). The property will not be altered by this request.
     *
     * @param property
     *     The {@link ElementDescriptor} of the property.
     *
     * @return This instance.
     */
    public PropPatch clearProperty(ElementDescriptor<?> property)
    {
        mRequest.clear(property);
        return this;
    }


    @Override
    public HttpMethod method()
    {
        return new IdempotentMethod("PROPPATCH", true);
    }


    @Override
    public Headers headers()
    {
        return EmptyHeaders.INSTANCE;
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        return new XmlRequestEntity<PropertyUpdate>(ElementDescriptor.DEFAULT_CONTEXT, WebDav.PROPERTYUPDATE, mRequest);
    }


    @Override
    public HttpResponseHandler<MultistatusResponseReader> responseHandler(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        if (response.status().equals(HttpStatus.MULTISTATUS))
        {
            return mDavContext.getMultistatusResponseHandler();
        }

        return new FailResponseHandler<>();
    }

}

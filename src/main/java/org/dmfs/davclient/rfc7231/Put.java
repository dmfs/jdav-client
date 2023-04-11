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

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.responsehandlers.FailResponseHandler;


/**
 * A PUT request
 *
 * @param <T>
 *     The type of the PUT response.
 */
public final class Put<T> implements HttpRequest<HttpStatus>
{
    private final HttpRequestEntity mPayload;


    public Put(HttpRequestEntity payload)
    {
        mPayload = payload;
    }


    @Override
    public final HttpMethod method()
    {
        return HttpMethod.PUT;
    }


    @Override
    public Headers headers()
    {
        return EmptyHeaders.INSTANCE;
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        return mPayload;
    }


    @Override
    public HttpResponseHandler<HttpStatus> responseHandler(HttpResponse response)
    {
        HttpStatus statusCode = response.status();
        if (!statusCode.equals(HttpStatus.OK) && !statusCode.equals(HttpStatus.NO_CONTENT) && !statusCode.equals(HttpStatus.CREATED))
        {
            return new FailResponseHandler<>();
        }
        return response1 -> {
            response1.responseEntity().contentStream().close();
            return response1.status();
        };
    }

}

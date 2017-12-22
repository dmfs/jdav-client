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
import org.dmfs.httpessentials.entities.EmptyHttpRequestEntity;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.responsehandlers.FailResponseHandler;


/**
 * An OPTIONS request.
 * <p>
 * TODO: at present this class doesn't return a response handler.
 * </p>
 */
public class Options implements HttpRequest<HttpOptions>
{

    private final static HttpResponseHandler<HttpOptions> HANDLER = HttpOptions::new;


    @Override
    public HttpMethod method()
    {
        return HttpMethod.OPTIONS;
    }


    @Override
    public Headers headers()
    {
        return EmptyHeaders.INSTANCE;
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        // no request body needed
        return new EmptyHttpRequestEntity();
    }


    @Override
    public HttpResponseHandler<HttpOptions> responseHandler(HttpResponse response)
    {
        HttpStatus statusCode = response.status();
        if (!statusCode.equals(HttpStatus.OK) && !statusCode.equals(HttpStatus.NO_CONTENT))
        {
            return new FailResponseHandler<>();
        }
        return HANDLER;
    }

}

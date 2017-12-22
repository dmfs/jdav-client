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

package org.dmfs.davclient.rfc3253;

import org.dmfs.dav.rfc4918.Depth;
import org.dmfs.davclient.BaseDavRequest;
import org.dmfs.davclient.DavContext;
import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.headers.BasicSingletonHeaderType;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.SingletonHeaders;
import org.dmfs.httpessentials.methods.SafeMethod;
import org.dmfs.httpessentials.typedentity.EntityConverter;


/**
 * Base class for requests that use the Report method as defined in <a href="http://tools.ietf.org/html/rfc3253#section-3.6">RFC 3253, section 3.6</a>
 *
 * @param <T>
 *     The type of the expected result.
 */
public abstract class Report<T> extends BaseDavRequest<T>
{
    /**
     * The value of the depth header.
     */
    private final Depth mDepth;


    /**
     * Constructor of a Report.
     *
     * @param davContext
     *     The {@link DavContext}.
     * @param depth
     *     The value of the {@link Depth} header or <code>null</code> to omit the depth header.
     */
    public Report(DavContext davContext, Depth depth)
    {
        super(davContext);
        mDepth = depth;
    }


    @Override
    public final HttpMethod method()
    {
        return new SafeMethod("REPORT", true);
    }


    @Override
    public Headers headers()
    {
        if (mDepth != null)
        {
            return new SingletonHeaders(new BasicSingletonHeaderType<>("Depth", new EntityConverter<Depth>()
            {
                @Override
                public Depth value(String valueString)
                {
                    throw new UnsupportedOperationException();
                }


                @Override
                public String valueString(Depth value)
                {
                    return value.toString();
                }
            }).entity(mDepth));
        }
        return EmptyHeaders.INSTANCE;
    }
}

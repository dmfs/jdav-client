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


/**
 * Stores all kind of information and helpers a DAV client needs.
 */
public class DavContext
{
    DavParserContext parserContext;
    private MultiStatusResponseHandler mMultistatusResponseHandler;


    /**
     * Creates a new {@link DavContext} with default parser settings. The parser will be set to be strict and it won't keep properties with status {@link
     * HttpStatus#NOT_FOUND}.
     */
    public DavContext()
    {
        this.parserContext = new DavParserContext();
        this.parserContext.setStrict(true);
        this.parserContext.setKeepNotFoundProperties(false);
    }


    /**
     * Creates a new {@link DavContext} using the given {@link DavParserContext} context.
     */
    public DavContext(DavParserContext parserContext)
    {
        if (parserContext == null)
        {
            throw new IllegalArgumentException("DavParserContext must not be null");
        }
        this.parserContext = parserContext;
    }


    /**
     * Return a {@link MultiStatusResponseHandler}.
     *
     * @return A {@link MultiStatusResponseHandler}.
     */
    public MultiStatusResponseHandler getMultistatusResponseHandler()
    {
        /*
         * MultistatusResponseHandlers are stateless and can be used multiple times, so we return the same instance on every call.
         */
        if (mMultistatusResponseHandler == null)
        {
            mMultistatusResponseHandler = new MultiStatusResponseHandler(parserContext);
        }
        return mMultistatusResponseHandler;
    }

}

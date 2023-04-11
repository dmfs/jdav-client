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

package org.dmfs.davclient.rfc6578;

import org.dmfs.dav.rfc4918.Depth;
import org.dmfs.dav.rfc6578.SyncCollection;
import org.dmfs.dav.rfc6578.SyncLevel;
import org.dmfs.dav.rfc6578.WebDavSync;
import org.dmfs.davclient.DavContext;
import org.dmfs.davclient.XmlRequestEntity;
import org.dmfs.davclient.rfc3253.MultiStatusReport;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.xmlobjects.ElementDescriptor;


/**
 * A sync-report as specified in <a href="http://tools.ietf.org/html/rfc6578">RFC 6578</a>. To perform an initial sync, use the
 * {@link #SyncReport(DavContext, SyncLevel)} constructor. To create an incremental sync request use the {@link #SyncReport(DavContext, SyncLevel, String)}
 * constructor, providing the sync-token returned in response to the previous sync report request.
 */
public class SyncReport extends MultiStatusReport
{

    /**
     * Constructor for an initial sync-collection Report.
     *
     * @param davContext
     *     A {@link DavContext}.
     * @param syncLevel
     *     The sync level.
     */
    public SyncReport(DavContext davContext, SyncLevel syncLevel)
    {
        this(davContext, syncLevel, null);
    }


    /**
     * Constructor for a sync-collection report.
     *
     * @param davContext
     *     A {@link DavContext}.
     * @param syncLevel
     *     The sync level.
     * @param syncToken
     *     A sync token or <code>null</code> to perform an initial sync.
     */
    public SyncReport(DavContext davContext, SyncLevel syncLevel, String syncToken)
    {
        // actually sync-report doesn't support the depth header, but sending 1 seems to work with all servers
        // on the other hand, some servers complain if we omit this header
        super(davContext, Depth.one);
        SyncCollection request = new SyncCollection();
        request.setSyncLevel(syncLevel);
        request.setSyncToken(syncToken);
        mRequest = request;
    }


    /**
     * Sets a number of results limit.
     * <p>
     * <strong>Note:</strong> Due to the bad support for this it's not recommended to set a limit unless you know for sure your server does it right.
     * </p>
     *
     * @param limit
     *     The maximum number of results to return.
     *
     * @return This instance.
     */
    public SyncReport setResultLimit(int limit)
    {
        ((SyncCollection) mRequest).limitNumberOfResults(limit);
        return this;
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        return new XmlRequestEntity<SyncCollection>(ElementDescriptor.DEFAULT_CONTEXT, WebDavSync.SYNC_COLLECTION, (SyncCollection) mRequest);
    }

}

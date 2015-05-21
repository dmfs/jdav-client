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

package org.dmfs.davclient.rfc6352;

import java.net.URI;

import org.dmfs.dav.rfc4918.Depth;
import org.dmfs.dav.rfc6352.AddressbookMultiget;
import org.dmfs.dav.rfc6352.CardDav;
import org.dmfs.davclient.DavContext;
import org.dmfs.davclient.XmlRequestEntity;
import org.dmfs.davclient.rfc3253.MultiStatusReport;
import org.dmfs.httpclientinterfaces.IHttpRequestEntity;
import org.dmfs.xmlobjects.ElementDescriptor;


/**
 * A report to query multiple elements of an addressbook collection in one request as specified in <a href="https://tools.ietf.org/html/rfc6352#section-8.7">RFC
 * 6352, section 8.7</a>.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class AddressbookMultigetReport extends MultiStatusReport
{

	/**
	 * Creates a addressbook-multiget allprop report.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            A {@link Depth}.
	 * @return A {@link AddressbookMultigetReport}.
	 */
	public static AddressbookMultigetReport ReportAllProp(DavContext davContext, Depth depth)
	{
		return new AddressbookMultigetReport(davContext, depth, true, false);
	}


	/**
	 * Creates a addressbook-multiget propname report.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            A {@link Depth}.
	 * @return A {@link AddressbookMultigetReport}.
	 */
	public static AddressbookMultigetReport ReportPropName(DavContext davContext, Depth depth)
	{
		return new AddressbookMultigetReport(davContext, depth, false, true);
	}


	/**
	 * Creates a {@link AddressbookMultigetReport} without depth header. Use {@link #AddressbookMultigetReport(DavContext, Depth)} to set a specific
	 * {@link Depth}.
	 * <p>
	 * To build an allprop report use {@link #ReportAllProp(DavContext, Depth)}, to build a propname report use {@link #ReportPropName(DavContext, Depth)}.
	 * </p>
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 */
	public AddressbookMultigetReport(DavContext davContext)
	{
		this(davContext, null, false, false);
	}


	/**
	 * Creates a {@link AddressbookMultigetReport} using the given {@link Depth}.
	 * <p>
	 * To build an allprop report use {@link #ReportAllProp(DavContext, Depth)}, to build a propname report use {@link #ReportPropName(DavContext, Depth)}.
	 * </p>
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            A {@link Depth}, may be <code>null</code>.
	 */
	public AddressbookMultigetReport(DavContext davContext, Depth depth)
	{
		this(davContext, depth, false, false);
	}


	/**
	 * Builds a new {@link AddressbookMultigetReport}. This constructor is private to ensure you can't set allProp and propName at the same time.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            The {@link Depth} of the report.
	 * @param allProp
	 *            The allprop flag.
	 * @param propName
	 *            The propname flag.
	 */
	private AddressbookMultigetReport(DavContext davContext, Depth depth, boolean allProp, boolean propName)
	{
		super(davContext, depth);

		AddressbookMultiget request = new AddressbookMultiget();
		request.setAllProp(allProp);
		request.setPropName(propName);
		mRequest = request;
	}


	/**
	 * Request additional URLs from the server to return.
	 * 
	 * @param hrefs
	 *            URLs of the objects to return.
	 * @return This instance.
	 * 
	 * @see #removeHrefs(URI...)
	 */
	public AddressbookMultigetReport addHrefs(URI... hrefs)
	{
		if (hrefs != null)
		{
			for (URI href : hrefs)
			{
				((AddressbookMultiget) mRequest).addHref(href);
			}
		}
		return this;
	}


	/**
	 * Remove URL that have been previously added with {@link #addHrefs(URI...)}.
	 * 
	 * @param hrefs
	 *            The URLs to remove.
	 * @return This instance.
	 */
	public AddressbookMultigetReport removeHrefs(URI... hrefs)
	{
		if (hrefs != null)
		{
			for (URI href : hrefs)
			{
				((AddressbookMultiget) mRequest).removeHref(href);
			}
		}
		return this;
	}


	@Override
	public IHttpRequestEntity getRequestEntity()
	{
		return new XmlRequestEntity<AddressbookMultiget>(ElementDescriptor.DEFAULT_CONTEXT, CardDav.ADDRESSBOOK_MULTIGET, (AddressbookMultiget) mRequest);
	}

}

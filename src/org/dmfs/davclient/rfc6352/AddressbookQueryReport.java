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

import org.dmfs.dav.rfc4918.Depth;
import org.dmfs.dav.rfc6352.AddressbookQuery;
import org.dmfs.dav.rfc6352.CardDav;
import org.dmfs.dav.rfc6352.filter.PropFilter;
import org.dmfs.davclient.DavContext;
import org.dmfs.davclient.XmlRequestEntity;
import org.dmfs.davclient.rfc3253.MultiStatusReport;
import org.dmfs.httpclientinterfaces.IHttpRequestEntity;
import org.dmfs.xmlobjects.ElementDescriptor;


/**
 * Represents the addressbook-query report as specified in <a href="https://tools.ietf.org/html/rfc6352#section-8.6">RFC 6352, section 8.6</a>.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class AddressbookQueryReport extends MultiStatusReport
{

	/**
	 * Creates a addressbook-query allprop report.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            A {@link Depth}.
	 * @return A {@link AddressbookQueryReport}.
	 */
	public static AddressbookQueryReport ReportAllProp(DavContext davContext, Depth depth)
	{
		return new AddressbookQueryReport(davContext, depth, true, false);
	}


	/**
	 * Creates a addressbook-query propname report.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            A {@link Depth}.
	 * @return A {@link AddressbookQueryReport}.
	 */
	public static AddressbookQueryReport ReportPropName(DavContext davContext, Depth depth)
	{
		return new AddressbookQueryReport(davContext, depth, false, true);
	}


	/**
	 * Creates a {@link AddressbookQueryReport} without depth header. Use {@link #AddressbookQueryReport(DavContext, Depth)} to set a specific {@link Depth}.
	 * <p>
	 * To build an allprop report use {@link #ReportAllProp(DavContext, Depth)}, to build a propname report use {@link #ReportPropName(DavContext, Depth)}.
	 * </p>
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 */
	public AddressbookQueryReport(DavContext davContext)
	{
		this(davContext, null, false, false);
	}


	/**
	 * Creates a {@link AddressbookQueryReport} using the given {@link Depth}.
	 * <p>
	 * To build an allprop report use {@link #ReportAllProp(DavContext, Depth)}, to build a propname report use {@link #ReportPropName(DavContext, Depth)}.
	 * </p>
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            A {@link Depth}, may be <code>null</code>.
	 */
	public AddressbookQueryReport(DavContext davContext, Depth depth)
	{
		this(davContext, depth, false, false);
	}


	/**
	 * Builds a new {@link AddressbookQueryReport}. This constructor is private to ensure you can't set allProp and propName at the same time.
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
	private AddressbookQueryReport(DavContext davContext, Depth depth, boolean allProp, boolean propName)
	{
		super(davContext, depth);

		AddressbookQuery request = new AddressbookQuery();
		request.setAllProp(allProp);
		request.setPropName(propName);
		mRequest = request;
	}


	/**
	 * Add a {@link PropFilter} to the request to filter the result on the server side.
	 * 
	 * @param filter
	 *            A {@link PropFilter}.
	 * @return This instance.
	 */
	public AddressbookQueryReport addFilter(PropFilter filter)
	{
		((AddressbookQuery) mRequest).addFilter(filter);
		return this;
	}


	/**
	 * Sets a number of results limit
	 * 
	 * @param limit
	 *            The maximum number of results to return.
	 * @return This instance.
	 */
	public AddressbookQueryReport setResultLimit(int limit)
	{
		((AddressbookQuery) mRequest).limitNumberOfResults(limit);
		return this;
	}


	@Override
	public IHttpRequestEntity getRequestEntity()
	{
		return new XmlRequestEntity<AddressbookQuery>(ElementDescriptor.DEFAULT_CONTEXT, CardDav.ADDRESSBOOK_QUERY, (AddressbookQuery) mRequest);
	}

}

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

package org.dmfs.davclient.rfc4791;

import org.dmfs.dav.rfc4791.CalDav;
import org.dmfs.dav.rfc4791.CalendarQuery;
import org.dmfs.dav.rfc4791.filter.CompFilter;
import org.dmfs.dav.rfc4918.Depth;
import org.dmfs.davclient.DavContext;
import org.dmfs.davclient.XmlRequestEntity;
import org.dmfs.davclient.rfc3253.MultiStatusReport;
import org.dmfs.httpclientinterfaces.IHttpRequestEntity;
import org.dmfs.xmlobjects.ElementDescriptor;


/**
 * Represents the calendar-query report as specified in <a href="https://tools.ietf.org/html/rfc4791#section-7.8">RFC 4791, section 7.8</a>.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class CalendarQueryReport extends MultiStatusReport
{

	/**
	 * Creates a calendar-query allprop report.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            A {@link Depth}.
	 * @return A {@link CalendarQueryReport}.
	 */
	public static CalendarQueryReport ReportAllProp(DavContext davContext, Depth depth)
	{
		return new CalendarQueryReport(davContext, depth, true, false);
	}


	/**
	 * Creates a calendar-query propname report.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            A {@link Depth}.
	 * @return A {@link CalendarQueryReport}.
	 */
	public static CalendarQueryReport ReportPropName(DavContext davContext, Depth depth)
	{
		return new CalendarQueryReport(davContext, depth, false, true);
	}


	/**
	 * Creates a {@link CalendarQueryReport} without depth header. Use {@link #CalendarQueryReport(DavContext, Depth)} to set a specific {@link Depth}.
	 * <p>
	 * To build an allprop report use {@link #ReportAllProp(DavContext, Depth)}, to build a propname report use {@link #ReportPropName(DavContext, Depth)}.
	 * </p>
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 */
	public CalendarQueryReport(DavContext davContext)
	{
		this(davContext, null, false, false);
	}


	/**
	 * Creates a {@link CalendarQueryReport} using the given {@link Depth}.
	 * <p>
	 * To build an allprop report use {@link #ReportAllProp(DavContext, Depth)}, to build a propname report use {@link #ReportPropName(DavContext, Depth)}.
	 * </p>
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            A {@link Depth}, may be <code>null</code>.
	 */
	public CalendarQueryReport(DavContext davContext, Depth depth)
	{
		this(davContext, depth, false, false);
	}


	/**
	 * Builds a new {@link CalendarQueryReport}. This constructor is private to ensure you can't set allProp and propName at the same time.
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
	private CalendarQueryReport(DavContext davContext, Depth depth, boolean allProp, boolean propName)
	{
		super(davContext, depth);

		CalendarQuery request = new CalendarQuery();
		request.setAllProp(allProp);
		request.setPropName(propName);
		mRequest = request;
	}


	/**
	 * Set a {@link CompFilter} to the request to filter the result on the server side.
	 * 
	 * @param filter
	 *            A {@link CompFilter}.
	 * @return This instance.
	 */
	public CalendarQueryReport setFilter(CompFilter filter)
	{
		((CalendarQuery) mRequest).setFilter(filter);
		return this;
	}


	@Override
	public IHttpRequestEntity getRequestEntity()
	{
		return new XmlRequestEntity<CalendarQuery>(ElementDescriptor.DEFAULT_CONTEXT, CalDav.CALENDAR_QUERY, (CalendarQuery) mRequest);
	}

}

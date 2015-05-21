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

import java.io.IOException;

import org.dmfs.dav.rfc4918.Depth;
import org.dmfs.dav.rfc4918.WebDav;
import org.dmfs.davclient.BaseDavRequest;
import org.dmfs.davclient.Constants;
import org.dmfs.davclient.DavContext;
import org.dmfs.davclient.MultistatusResponseReader;
import org.dmfs.davclient.XmlRequestEntity;
import org.dmfs.httpclientinterfaces.HttpMethod;
import org.dmfs.httpclientinterfaces.HttpStatus;
import org.dmfs.httpclientinterfaces.IHeaderEditor;
import org.dmfs.httpclientinterfaces.IHttpRequestEntity;
import org.dmfs.httpclientinterfaces.IHttpResponse;
import org.dmfs.httpclientinterfaces.IResponseHandler;
import org.dmfs.httpclientinterfaces.exceptions.ProtocolError;
import org.dmfs.httpclientinterfaces.exceptions.ProtocolException;
import org.dmfs.xmlobjects.ElementDescriptor;


/**
 * Represents the PropFind request as defined in <a href="https://tools.ietf.org/html/rfc4918#section-9.1">RFC 4918, section 9.1</a>.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class PropFind extends BaseDavRequest<MultistatusResponseReader>
{
	/**
	 * The value of the {@link Depth} header.
	 */
	private final Depth mDepth;

	/**
	 * The actual request body.
	 */
	private final org.dmfs.dav.rfc4918.PropFind mRequest;


	/**
	 * Creates a PropFind allprop request with the given {@link Depth} and no include element.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            The value of the {@link Depth} header or <code>null</code> to omit the depth header.
	 * @return A {@link PropFind} request.
	 */
	public static PropFind PropFindAllProp(DavContext davContext, Depth depth)
	{
		return new PropFind(davContext, depth, true, false);
	}


	/**
	 * Creates a PropFind allprop request with the given {@link Depth} and included elements.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            The value of the {@link Depth} header or <code>null</code> to omit the depth header.
	 * @param include
	 *            A list of {@link ElementDescriptor}s of the properties to include in the response.
	 * @return A {@link PropFind} request.
	 */
	public static PropFind PropFindAllProp(DavContext davContext, Depth depth, ElementDescriptor<?>... include)
	{
		return new PropFind(davContext, depth, true, false).addProperties(include);
	}


	public static PropFind PropFindPropName(DavContext davContext, Depth depth)
	{
		return new PropFind(davContext, depth, false, true);
	}


	/**
	 * Constructor for a PROPFIND request for the given properties using the given depth.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            The {@link Depth}, may be <code>null</code> to omit a depth header (and default to the server implementation).
	 * @param properties
	 *            Properties to request, may be empty.
	 */
	public PropFind(DavContext davContext, Depth depth, ElementDescriptor<?>... properties)
	{
		this(davContext, depth, false, false);
		addProperties(properties);
	}


	/**
	 * Private constructor to ensure clients can't set allProp and propName at the same time.
	 * 
	 * @param davContext
	 *            A {@link DavContext}.
	 * @param depth
	 *            The {@link Depth} of the request.
	 * @param allProp
	 *            The allprop flag.
	 * @param propName
	 *            The propname flag.
	 */
	private PropFind(DavContext davContext, Depth depth, boolean allProp, boolean propName)
	{
		super(davContext);
		mDepth = depth;
		mRequest = new org.dmfs.dav.rfc4918.PropFind();
		mRequest.setAllProp(allProp);
		mRequest.setPropName(propName);
	}


	/**
	 * Add more properties to the request. If this has been created with {@link #PropFindAllProp(DavContext, Depth)} or
	 * {@link #PropFindAllProp(DavContext, Depth, ElementDescriptor...)} the properties will be added to the <code>include</code> element. If this request has
	 * been created with {@link #PropFindPropName(DavContext, Depth)} this method will throw an {@link IllegalStateException}.
	 * 
	 * @param properties
	 *            The {@link ElementDescriptor}s of the properties to send with the request.
	 * @return This instance.
	 * 
	 * @throws IllegalStateException
	 *             If this request has been created with {@link #PropFindPropName(DavContext, Depth)}.
	 */
	public PropFind addProperties(ElementDescriptor<?>... properties)
	{
		if (mRequest.getPropName())
		{
			throw new IllegalStateException("no properties allowed in a propname request.");
		}

		if (properties != null)
		{
			for (ElementDescriptor<?> property : properties)
			{
				mRequest.addProperty(property);
			}
		}
		return this;
	}


	/**
	 * Remove properties from the request. This has no effect if the property has not been added previously using {@link #addProperties(ElementDescriptor...)}
	 * or as a parameter to {@link #PropFind(DavContext, Depth, ElementDescriptor...)}.
	 * 
	 * @param properties
	 *            The {@link ElementDescriptor}s of the properties to remove.
	 * @return This instance.
	 */
	public PropFind removeProperties(ElementDescriptor<?>... properties)
	{
		if (properties != null)
		{
			for (ElementDescriptor<?> property : properties)
			{
				mRequest.removeProperty(property);
			}
		}
		return this;
	}


	@Override
	public HttpMethod getMethod()
	{
		return WebDav.METHOD_PROPFIND;
	}


	@Override
	public IHttpRequestEntity getRequestEntity()
	{
		return new XmlRequestEntity<org.dmfs.dav.rfc4918.PropFind>(ElementDescriptor.DEFAULT_CONTEXT, WebDav.PROPFIND, mRequest, mRequest.getProperties());
	}


	@Override
	public IResponseHandler<MultistatusResponseReader> getResponseHandler(IHttpResponse response) throws IOException, ProtocolError, ProtocolException
	{
		if (response.getStatusCode() == HttpStatus.MULTISTATUS)
		{
			return mDavContext.getMultistatusResponseHandler();
		}

		return null;
	}


	@Override
	public void updateHeaders(IHeaderEditor headerEditor)
	{
		if (mDepth != null)
		{
			headerEditor.setHeader(Constants.HEADER_DEPTH, mDepth.toString());
		}
	}

}

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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import org.dmfs.httpclientinterfaces.ContentType;
import org.dmfs.httpclientinterfaces.IHttpRequestEntity;
import org.dmfs.xmlobjects.ElementDescriptor;
import org.dmfs.xmlobjects.XmlContext;
import org.dmfs.xmlobjects.serializer.SerializerContext;
import org.dmfs.xmlobjects.serializer.XmlObjectSerializer;


/**
 * An XML request entity.
 * <p>
 * Note: The XML data is rendered on demand, which means that the content length is not known in advance and {@link #getContentLength()} will always return
 * <code>-1</code>.
 * </p>
 * 
 * @author Marten Gajda <marten@dmfs.org>
 * 
 * @param <T>
 *            The type of the XML root element.
 */
public class XmlRequestEntity<T> implements IHttpRequestEntity
{

	private final ElementDescriptor<T> mDescriptor;
	private final T mData;
	private final XmlContext mXmlContext;
	private final Collection<ElementDescriptor<?>> mNamespaces;


	/**
	 * Creates a new {@link XmlRequestEntity}
	 * 
	 * @param context
	 *            An {@link XmlContext}.
	 * @param descriptor
	 *            The {@link ElementDescriptor} of the XML root element.
	 * @param data
	 *            The XML root element.
	 */
	public XmlRequestEntity(XmlContext context, ElementDescriptor<T> descriptor, T data)
	{
		mDescriptor = descriptor;
		mData = data;
		mXmlContext = context;
		mNamespaces = null;
	}


	/**
	 * Creates a new {@link XmlRequestEntity}
	 * 
	 * @param context
	 *            An {@link XmlContext}.
	 * @param descriptor
	 *            The {@link ElementDescriptor} of the XML root element.
	 * @param data
	 *            The XML root element.
	 * @param namespaces
	 *            An optional collection of {@link ElementDescriptor}s that may by rendered. This is used to allocate required namespaces early.
	 */
	public XmlRequestEntity(XmlContext context, ElementDescriptor<T> descriptor, T data, Collection<ElementDescriptor<?>> namespaces)
	{
		mDescriptor = descriptor;
		mData = data;
		mXmlContext = context;
		mNamespaces = namespaces;
	}


	@Override
	public ContentType getContentType()
	{
		return Constants.CONTENT_TYPE_APPLICATION_XML;
	}


	@Override
	public long getContentLength() throws IOException
	{
		// the content lenght is unknown
		return -1;
	}


	@Override
	public void writeContent(OutputStream out) throws IOException
	{
		try
		{
			SerializerContext context = new SerializerContext(mXmlContext);
			XmlObjectSerializer serializer = new XmlObjectSerializer();
			if (mNamespaces != null)
			{
				for (ElementDescriptor<?> descriptor : mNamespaces)
				{
					serializer.useNamespace(context, descriptor);
				}
			}
			serializer.setOutput(context, out, getContentType().getCharset());
			serializer.serialize(context, mDescriptor, mData);
		}
		catch (Exception e)
		{
			throw new IOException("can not serialize to xml", e);
		}
	}
}

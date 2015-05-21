
# jDAV-Client

__A DAV client for Java__


## Requirements

[XmlObjects](https://github.com/dmfs/xmlobjects) for parsing and serialization.
[jDav](https://github.com/dmfs/jdav) for definitions of DAV XML elements.
[http-client-interfaces](https://github.com/dmfs/http-client-interfaces) to model the HTTP client.

## Example

The following example shows how this library is to be used.

Please note: The example is kept simple and doesn't contain exception handling. In a real application you always need to be prepared for all kinds of exceptions, like connection or authentication issues.
The Executor used in the example needs to implement the IHttpExecutor interface from the http-client-interfaces library. The Executor implementation is not part of this library.

```java

    // create a DavContext. This needs to be done only once. Multiple requests can share the same DavContext, when executed consecutively.
    DavContext davContext = new DavContext();

    // create the request and add a few properties to query from the server
    PropFind request = new PropFind(davContext, Depth.one);
    propfind.addProperties(WebDav.Properties.DISPLAYNAME,
        WebDav.Properties.RESOURCETYPE,
        WebDav.Properties.GETETAG);

    // send the request and get the MultistatusResponseReader to iterate the Response elements
    MultistatusResponseReader result = executor.execute(requestUrl, request);

    Response response = null;
    while (result.hasNextResponse())
    {
        // get the next response, recycling the previous one to avoid
        // allocation of new instances whenever possible.
        response = result.getNextResponse(response);

        // check if the response contains Properties or a status
        if (response.getStatus() == Response.STATUS_NONE)
        {
            // this is a response with properties

            // get the response URL
            URI url = response.getHref();

            // get the property values, this returns null if the property was not returned by the server
            String displayName = response.getPropertyValue(WebDav.Properties.DISPLAYNAME);

            // do something with the response ...
            // ...
	}
    }

```

## License

Copyright (C) 2014 Marten Gajda <marten@dmfs.org>

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published
by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
USA

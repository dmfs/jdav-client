package org.dmfs.davclient.utils;

import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.jems2.Predicate;


public final class TypeEquals implements Predicate<MediaType>
{
    private final MediaType mType;


    public TypeEquals(MediaType type)
    {
        mType = type;
    }


    @Override
    public boolean satisfiedBy(MediaType testedInstance)
    {
        return mType.mainType().equals(testedInstance.mainType()) && mType.subType().equals(testedInstance.subType());
    }
}

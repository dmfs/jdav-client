package org.dmfs.davclient.utils;

import org.dmfs.httpessentials.types.StructuredMediaType;
import org.saynotobugs.confidence.junit5.engine.Confidence;
import org.saynotobugs.confidence.junit5.engine.Verifiable;

import static org.dmfs.jems2.confidence.Jems2.satisfiedBy;
import static org.saynotobugs.confidence.junit5.engine.ConfidenceEngine.assertThat;
import static org.saynotobugs.confidence.quality.Core.allOf;
import static org.saynotobugs.confidence.quality.Core.not;


@Confidence
class TypeEqualsTest
{
    Verifiable TypeEquals = assertThat(new TypeEquals(new StructuredMediaType("x", "y", "utf-8")),
        allOf(
            satisfiedBy(new StructuredMediaType("x", "y")),
            satisfiedBy(new StructuredMediaType("x", "y", "utf-8")),
            satisfiedBy(new StructuredMediaType("x", "y", "latin-1")),
            not(satisfiedBy(new StructuredMediaType("x", "a"))),
            not(satisfiedBy(new StructuredMediaType("a", "y"))),
            not(satisfiedBy(new StructuredMediaType("a", "b")))
        ));
}
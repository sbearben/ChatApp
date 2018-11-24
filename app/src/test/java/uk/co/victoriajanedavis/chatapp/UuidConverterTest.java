package uk.co.victoriajanedavis.chatapp;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class UuidConverterTest {

    private UUID mUuid;

    @Before
    public void setup() {
        mUuid = UUID.randomUUID();
    }

    @Test
    public void testUuidConverter() {
        String uuidStr = mUuid.toString();
        assertEquals(UUID.fromString(uuidStr), mUuid);
    }
}

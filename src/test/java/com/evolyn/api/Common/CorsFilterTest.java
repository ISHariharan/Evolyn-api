package com.evolyn.api.Common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

class CorsFilterTest {

    @Test
    void parsesTheListTrimmingSpacesAndTrailingSlashes() {
        assertEquals(Set.of("https://a.vercel.app", "http://localhost:3000"),
                CorsFilter.parse(" https://a.vercel.app/ , http://localhost:3000,, "));
    }

    @Test
    void onlyExactOriginsAreAllowed() {
        CorsFilter cors = new CorsFilter("https://evolyn-mg16.vercel.app");
        assertTrue(cors.isAllowed("https://evolyn-mg16.vercel.app"));
        assertFalse(cors.isAllowed("https://evolyn-mg16.vercel.app.evil.com"));
        assertFalse(cors.isAllowed("http://evolyn-mg16.vercel.app"));
        assertFalse(cors.isAllowed(null));
    }

    @Test
    void anEmptyListAllowsNobody() {
        assertFalse(new CorsFilter("").isAllowed("http://localhost:3000"));
    }
}

package io.hedwig.igniteusage.app;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;

import io.hedwig.igniteusage.samples.encodeURLService;

public class TestAppStartup {

    public static void main(String[] args) throws IgniteException {
        Ignite ignite = Ignition.start("config/client-node-config.xml");
        IgniteCache<String, String> urlCache = ignite.cache("URL");

        encodeURLService
            encoder = ignite.services().serviceProxy(encodeURLService.SERVICE_NAME, encodeURLService.class, true);

        String url = "https://www.linkedin.com/search/results/content/?anchorTopic=68378&" +
                         "keywords=Ford%20gets%20new%20CEO%20amid%20shake-up&" +
                            "origin=NEWS_MODULE_FROM_DESKTOP_HOME";

        String hashed = encoder.getHash(url);
        urlCache.put(url, hashed);
        System.out.println("http://UrlZipper.com/" + urlCache.get(url));
    }
}

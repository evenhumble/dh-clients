package io.hedwig.igniteusage.samples;

import org.apache.ignite.services.Service;


public interface encodeURLService extends Service
{
    public static final String SERVICE_NAME = "encodeURLService";
    public String getHash(String url);
}
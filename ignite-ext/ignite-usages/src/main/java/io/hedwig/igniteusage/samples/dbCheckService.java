package io.hedwig.igniteusage.samples;

import org.apache.ignite.services.Service;

public interface dbCheckService extends Service
{
    public static final String SERVICE_NAME = "dbChecker";
    public Boolean ifDuplicate(String url);
}
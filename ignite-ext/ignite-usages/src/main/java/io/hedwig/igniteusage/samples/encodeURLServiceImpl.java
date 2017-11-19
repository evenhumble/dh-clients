package io.hedwig.igniteusage.samples;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.ServiceContext;

import io.hedwig.igniteusage.store.MongoDataStore;

public class encodeURLServiceImpl implements encodeURLService {
    @IgniteInstanceResource
    private Ignite ignite;
    private IgniteCache<Integer, URL> urlCache;
    private IgniteAtomicSequence sequence;

    public void init(ServiceContext ctx) throws Exception {
        System.out.println("\n ** Initializing encodeURL Service on node:" + ignite.cluster().localNode() + " **");
        urlCache = ignite.cache("URL");
        sequence = ignite.atomicSequence("Url_ID", 1, true);
    }

    public void execute(ServiceContext ctx) throws Exception {
        System.out.println("\n ** Executing encodeURL service on node:" + ignite.cluster().localNode() + " **");
    }

    public void cancel(ServiceContext ctx) {
        System.out.println("\n ** Stopping Maintenance Service on node:" + ignite.cluster().localNode() + " **");
    }

    public String getUid() {
        return Long.toString(sequence.getAndIncrement());
    }

    public String getHash(String url) {

        MongoDataStore d = new MongoDataStore();
        d.init();

        while (true) {
            StringBuilder hashedUrl = new StringBuilder();
            int i = 0;
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(url.getBytes("UTF-16"));
                String s = Base64.getEncoder().encodeToString(hash);
                char[] arr = s.toCharArray();

                for (i = 0; i < 7; i++) {
                    hashedUrl.append(arr[i]);
                }

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            /* Getting the sequence that will be used for appending to URL in case of hash collisions */
            dbCheckService findUnique = ignite.services().serviceProxy(dbCheckService.SERVICE_NAME, dbCheckService.class, true);
            if (!findUnique.ifDuplicate(hashedUrl.toString())) {
                return hashedUrl.toString();
            }

            url += getUid();
        }
    }
}

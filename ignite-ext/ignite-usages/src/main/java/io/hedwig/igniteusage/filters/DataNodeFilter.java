package io.hedwig.igniteusage.filters;

import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgnitePredicate;

/**
 * 1. author: patrick
 */
public class DataNodeFilter implements IgnitePredicate<ClusterNode> {

  @Override
  public boolean apply(ClusterNode clusterNode) {
    Boolean dataNodeStatus = clusterNode.attribute("data.node");
    return dataNodeStatus != null && dataNodeStatus;
  }
}

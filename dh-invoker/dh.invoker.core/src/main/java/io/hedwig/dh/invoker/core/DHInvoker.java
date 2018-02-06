package io.hedwig.dh.invoker.core;

/**
 * @@author: patrick
 */
public interface DHInvoker {
//  public  void onInvoke(DHRequest request);
  public DHResponse invoke(DHRequest request);
//  public void exitInvoke(DHRequest request);
}

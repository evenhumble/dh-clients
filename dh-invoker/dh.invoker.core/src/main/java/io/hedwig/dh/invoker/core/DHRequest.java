package io.hedwig.dh.invoker.core;

    import lombok.Data;

/**
 * @@author: patrick
 */
@Data
public class DHRequest {
  private String requestType;
  private Object data;
}

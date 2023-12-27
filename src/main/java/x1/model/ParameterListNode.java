package x1.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ParameterListNode {
  private final List<ParameterNode> parameters = new ArrayList<>();

  public void addParameter(ParameterNode parameter) {
    parameters.add(parameter);
  }
}

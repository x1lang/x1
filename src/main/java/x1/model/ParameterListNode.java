package x1.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data public class ParameterListNode {
  private final List<ParameterNode> parameters = new ArrayList<>();
  public void addParameter(ParameterNode parameter) {
    parameters.add(parameter);
  }
}

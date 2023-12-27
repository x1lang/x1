package x1.model;

import java.util.List;
import lombok.Value;

@Value
public class ArrayExpressionNode implements ExpressionNode {
  List<ExpressionNode> expressions;
}

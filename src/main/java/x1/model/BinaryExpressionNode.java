package x1.model;

import lombok.Data;

@Data public class BinaryExpressionNode implements ExpressionNode {
    private ExpressionNode leftExpression;
    private BinaryOperatorNode operator;
    private ExpressionNode rightExpression;
}

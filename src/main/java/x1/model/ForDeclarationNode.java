package x1.model;

import lombok.Data;

@Data public class ForDeclarationNode implements StatementNode {
    private VariableDeclarationNode variableDeclaration;
    private ExpressionNode expression;
    private AssignmentStatementNode assignmentStatement;
    private BlockNode block;
}

package x1;

import x1.model.*;

public interface NodeVisitor {
  void visit(CompilationUnitNode node);

  void visit(TypeDeclarationNode node);

  void visit(FieldDeclarationNode node);

  void visit(MethodDeclarationNode node);

  void visit(ParameterListNode node);

  void visit(ParameterNode node);

  void visit(TypeNode node);

  void visit(BlockNode node);

  default void visit(StatementNode node) {
    if (node instanceof ForDeclarationNode) {
      visit((ForDeclarationNode) node);
    } else if (node instanceof ForEachDeclarationNode) {
      visit((ForEachDeclarationNode) node);
    } else if (node instanceof VariableDeclarationNode) {
      visit((VariableDeclarationNode) node);
    } else if (node instanceof AssignmentStatementNode) {
      visit((AssignmentStatementNode) node);
    } else if (node instanceof IfStatementNode) {
      visit((IfStatementNode) node);
    } else if (node instanceof ReturnStatementNode) {
      visit((ReturnStatementNode) node);
    } else {
      throw new RuntimeException("Unknown statement node: " + node);
    }
  }

  void visit(ForDeclarationNode node);

  void visit(ForEachDeclarationNode node);

  void visit(VariableDeclarationNode node);

  void visit(AssignmentStatementNode node);

  void visit(IfStatementNode node);

  void visit(ReturnStatementNode node);

  void visit(ExpressionNode node);

  void visit(ArrayExpressionNode node);

  void visit(LiteralNode node);

  void visit(BinaryExpressionNode node);

  void visit(BinaryOperatorNode node);

  void visit(UnaryExpressionNode node);

  void visit(UnaryOperatorNode node);

  void visit(IdentifierNode node);
}

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

  void visit(StatementNode node);

  void visit(ForDeclarationNode node);

  void visit(ForEachStatementNode node);

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

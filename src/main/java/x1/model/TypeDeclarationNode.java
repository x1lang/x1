package x1.model;

import lombok.Data;

@Data public class TypeDeclarationNode {
  private IdentifierNode identifier;
  private FieldDeclarationNode fieldDeclaration;

  public void addFieldDeclaration(FieldDeclarationNode fieldDeclarationNode) {
    this.
        fieldDeclaration = fieldDeclarationNode;
  }
}

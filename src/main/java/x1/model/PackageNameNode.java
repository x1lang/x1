package x1.model;

import lombok.Data;

@Data public class PackageNameNode {
  private String packageName = "";

  public void addIdentifier(IdentifierNode identifier) {
    if (!this.packageName.isEmpty()) {
      this.packageName += ".";
    }
    this.packageName += identifier.getToken().getText();
  }
}

package x1;

import x1.model.Node;

public interface CodeGenerator {
  String getLanguage();

  String generate(Node node);
}

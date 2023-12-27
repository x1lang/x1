package x1;

import x1.model.CompilationUnitNode;

@FunctionalInterface
public interface Transformer {

  /**
   * Transform the given compilation unit node into source code
   *
   * @param compilationUnitNode the compilation unit node. Not null.
   * @return the source code. Not null.
   */
  String transform(CompilationUnitNode compilationUnitNode);
}

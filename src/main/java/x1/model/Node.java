package x1.model;

import x1.NodeVisitor;

public interface Node {
  void accept(NodeVisitor visitor);
}

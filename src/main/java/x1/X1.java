package x1;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import x1.model.CompilationUnitNode;

public class X1 {
  public static void main(String[] args) throws Exception {
    // read from example.x1
    try (InputStream in = Files.newInputStream(Paths.get("example.x1"))) {
      Lexer lexer = new Lexer(in);
      Parser parser = new Parser(lexer);
      CompilationUnitNode compilationUnitNode = parser.parse();

      // print the AST
      System.out.println(compilationUnitNode);

      Transformer transformer = new JavaTransformer();

      // print the generated Java code
      System.out.println(transformer.transform(compilationUnitNode));
    }
  }
}

package x1;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ServiceLoader;
import org.junit.jupiter.api.Test;
import x1.model.CompilationUnitNode;

class CodeGeneratorTest {

  @Test
  void generate() throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    try (InputStream in = classLoader.getResourceAsStream("example.x1")) {
      ServiceLoader<CodeGenerator> serviceLoader = ServiceLoader.load(CodeGenerator.class);
      Lexer lexer = new Lexer(Objects.requireNonNull(in));
      Parser parser = new Parser(lexer);
      CompilationUnitNode compilationUnitNode = parser.parse();

      for (CodeGenerator codeGenerator : serviceLoader) {
        String generate = codeGenerator.generate(compilationUnitNode);

        Files.write(
            Paths.get("src/test/resources/example." + codeGenerator.getExtension()),
            generate.getBytes());
      }
    }
  }
}

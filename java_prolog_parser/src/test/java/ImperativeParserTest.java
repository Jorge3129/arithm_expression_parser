import com.sanchenko.exp.Exp;
import com.sanchenko.imperative_parser.ImperativeParser;
import org.junit.jupiter.api.Test;

import static com.sanchenko.exp.ExpConstructors.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ImperativeParserTest {


   @Test
   public void parseSimpleInput() throws Exception {
      String input = "1 + a";

      Exp result = ImperativeParser.parse(input);

      Exp expected = BinOp(
          "+",
          Num(1),
          Var("a")
      );

      assertThat(result).usingRecursiveComparison()
          .isEqualTo(expected);
   }

   @Test
   public void parseFunctionCall() throws Exception {
      String input = "sin(cos(foo)) + max(min(1,2), bar)";

      Exp result = ImperativeParser.parse(input);

      Exp expected = BinOp(
          "+",
          FunApp("sin",
              FunApp("cos",
                  Var("foo")
              )
          ),
          FunApp("max",
              FunApp("min",
                  Num(1),
                  Num(2)
              ),
              Var("bar")
          )
      );

      assertThat(result).usingRecursiveComparison()
          .isEqualTo(expected);
   }
}

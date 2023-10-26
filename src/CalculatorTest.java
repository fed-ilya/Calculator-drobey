import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void RPNtoAnswer() {
        var Calc = new Calculator();
        assertEquals("5/6",Calc.RPNtoAnswer("1/2 + 1/3"));
    }
}
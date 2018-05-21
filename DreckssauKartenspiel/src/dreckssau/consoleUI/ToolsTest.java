package dreckssau.consoleUI;

import org.junit.Test;

import static org.junit.Assert.*;

public class ToolsTest {

    protected Tools tools = new Tools();

    @Test
    public void testGetCardString() {
        System.out.println(tools.getCardString("Seven of Hearts"));
    }


}
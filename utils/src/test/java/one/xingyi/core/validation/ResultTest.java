package one.xingyi.core.validation;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
public class ResultTest {

    @Test public void testResultSucceds(){
        assertEquals(new Succeeds<>("String"), Result.succeed("String"));
    }
    @Test public void testResultFails(){
        assertEquals(new Failures<String,String>(List.of("fail1")), Result.fail("fail1"));
        assertEquals(new Failures<String,String>(List.of("fail1")), Result.fails("fail1"));
    }

    @Test public void testResultApply(){
        assertEquals(Result.succeed("one"), Result.apply(List.of(), "one"));
        assertEquals(Result.fail("fail"), Result.apply(List.of("fail"), "one"));
    }
    @Test public void testMap(){
        assertEquals(Result.succeed("1mapped"),Result.succeed("1").map(e->e+"mapped"));
        assertEquals(Result.fail("fail"),Result.fail("fail").map(e->e+"mapped"));
    }

    @Test public void testLeftMap(){
        fail();
    }
    @Test public void testFlatMap(){
        fail();
    }

}
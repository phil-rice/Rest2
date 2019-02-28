package one.xingyi.core.validation;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
public class ResultTest {

    @Test public void testResultSucceds(){
        assertEquals(new Succeeds<>("String"), Result.succeed("String"));
    }
    @Test public void testResultFails(){
        assertEquals(new Failures<String,String>(List.of("fail1")), Result.failwith("fail1"));
        assertEquals(new Failures<String,String>(List.of("fail1")), Result.allFail("fail1"));
    }

    @Test public void testResultApply(){
        assertEquals(Result.succeed("one"), Result.apply(List.of(), "one"));
        assertEquals(Result.failwith("failwith"), Result.apply(List.of("failwith"), "one"));
    }
    @Test public void testMap(){
        assertEquals(Result.succeed("1mapped"),Result.succeed("1").map(e->e+"mapped"));
        assertEquals(Result.failwith("failwith"),Result.failwith("failwith").map(e->e+"mapped"));
    }
//
//    @Test public void testLeftMap(){
//        failwith();
//    }
//    @Test public void testFlatMap(){
//        failwith();
//    }

}
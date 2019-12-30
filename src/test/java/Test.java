import java.math.BigDecimal;

/**
 * @Classname Test
 * @Description TODO
 * @Date 2019/12/30 21:50
 * @Author SonnSei
 */
public class Test {
    @org.junit.Test
    public void test1(){
        BigDecimal b1 = new BigDecimal("0.05");
        BigDecimal b2 = new BigDecimal("0.01");
        System.out.println(b1.add(b2));
    }
}

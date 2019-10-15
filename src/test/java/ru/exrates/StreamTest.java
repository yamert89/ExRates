package ru.exrates;

import org.junit.Test;
import ru.exrates.entities.CurrencyPair;

import java.util.Optional;
import java.util.TreeSet;

public class StreamTest {

    @Test
    public static void main(String[] args) {
        for (int i = 0; i < 8; i++) {
            test();
        }
    }


    public static void test() {
        int check = random(0, 30);
        TreeSet<CurrencyPair> pairs = null;
        final CurrencyPair[] pair = {null};

        pairs = stramTest();

        long cur = System.currentTimeMillis();

        for (CurrencyPair el : pairs) {
            if (el.getSymbol().equals(Character.getName(check))) {
                pair[0] = el;
                break;
            }
        }
        System.out.println("Iterator = " + (System.currentTimeMillis() - cur) + " " + pair[0]);

        pairs = stramTest();

        cur = System.currentTimeMillis();
        Optional<CurrencyPair> p = pairs.stream().filter(e -> e.getSymbol().equals(Character.getName(check))).findFirst();
        pair[0] = p.get();

        System.out.println("Stream = " + (System.currentTimeMillis() - cur) + " " + pair[0]);
    }


    public static TreeSet<CurrencyPair> stramTest() {
        TreeSet<CurrencyPair> pairs = new TreeSet<>();
        for (int i = 0; i < 20000; i++) {
            pairs.add(new CurrencyPair(Character.getName(random(0, 120))));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return pairs;
        //pairs.add(new CurrencyPair(Character.getName(15)));





    }

    static int random(int min, int max){
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}

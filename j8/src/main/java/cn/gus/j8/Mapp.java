package cn.gus.j8;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Mapp {

    public static void main(String[] args) {

        List<Foo> list = new ArrayList<>();

        list.add(new Foo(1, "gus1", 31));
        list.add(new Foo(3, "gus3", 33));
        list.add(new Foo(2, "gus2", 32));
        list.add(new Foo(5, "gus5", 35));
        list.add(new Foo(7, "gus7", 37));
        list.add(new Foo(6, "gus6", 36));

        List res = list.parallelStream()
                .filter(p -> p.getAge() > 30 && p.getId() > 6)
                .sorted(Comparator.comparing(Foo::getAge).reversed())
                .collect(Collectors.toList());

        System.out.println(res);


        new Thread(() -> {
            try {
                System.out.println(1);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

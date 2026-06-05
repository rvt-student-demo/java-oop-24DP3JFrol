package rvt;

import java.util.HashMap;

public class IOweYou {
    HashMap<String, Double> IOweYouList = new HashMap<>();

    public void setSum(String toWhom, double amount) {
        IOweYouList.put(toWhom, amount);
    }

    public double howMuchDoIOwe(String toWhom) {
        return IOweYouList.getOrDefault(toWhom, 0.0);
    }

    public static void main(String[] args) {
        IOweYou iOweYou = new IOweYou();
        iOweYou.setSum("Arthur", 51.5);
        iOweYou.setSum("Michael", 30.0);

        System.out.println(iOweYou.howMuchDoIOwe("Arthur"));
        System.out.println(iOweYou.howMuchDoIOwe("Michael"));
    }
}
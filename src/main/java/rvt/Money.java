package rvt;

public class Money {
    private final int euros;
    private final int cents;

    public Money(int euros, int cents) {

        this.euros = euros;
        this.cents = cents;
    }

    public int euros() {
        return euros;
    }

    public int cents() {
        return cents;
    }

    public Money plus(Money addition) {
        int newEuros = this.euros + addition.euros;
        int newCents = this.cents + addition.cents;
        if (newCents >= 100) {
            newEuros += newCents / 100;
            newCents = newCents % 100;
        }
        return new Money(newEuros, newCents);
    }

    public boolean lessThan(Money compared) {
        if (this.euros < compared.euros) {
            return true;
        }
        if (this.euros > compared.euros) {
            return false;
        }
        return  this.cents < compared.cents;
    }

    public Money minus(Money decreaser) {
        int thisTotalCents = this.euros * 100 + this.cents;
        int otherTotalCents = decreaser.euros * 100 + decreaser.cents;
        int resultCents = thisTotalCents - otherTotalCents;
        if (resultCents < 0) {
            return new Money(0, 0);
        }
        
        int newEuros = resultCents / 100;
        int newCents = resultCents % 100;
        return  new Money(newEuros, newCents);
    }

    public String toString() {
        String zero = "";
        if (cents <= 10) {
            zero = "0";
        }

        return euros + "." + zero + cents + "e";
    }
}

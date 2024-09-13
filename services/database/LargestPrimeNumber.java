package services.database;

// finds the largest prime number below a given number
public class LargestPrimeNumber {

    private final int number;

    public LargestPrimeNumber(int number) {
        this.number = number;
    }

    public int getLargestPrime() {
        if (number <= 1) {
            return -1;
        }
        for (int i = number; i > 1; i--) {
            if (isPrime(i)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isPrime(int number) {
        if (number == 2) {
            return true;
        }
        if (number <= 1 || number % 2 == 0) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

}

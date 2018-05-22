
class TestBogus {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++ )
            new Thread( new Bogus( i ) ).start();
    }
}

class Bogus implements Runnable {

    private int value;

    public Bogus ( int value ) {
        this.value = value;
    }


    @Override
    public void run() {
        for (int i = 0; i < 120; i++ ) {
            System.out.println( "Thread(" + value + ") i = " + i );
        }
    }
}

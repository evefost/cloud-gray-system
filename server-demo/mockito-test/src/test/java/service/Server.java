package service;

public class Server {

    public String doSomething(){
        System.out.println("99999999999999999");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    }
}

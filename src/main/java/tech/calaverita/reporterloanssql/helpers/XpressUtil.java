package tech.calaverita.reporterloanssql.helpers;

import tech.calaverita.reporterloanssql.pojos.LoginResponse;
import tech.calaverita.reporterloanssql.threads.LoginResponseThread;

public class XpressUtil {
    public static void setInvolucradosLoginResponse(LoginResponse loginResponse) {
        Thread[] threads = new Thread[3];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new LoginResponseThread(loginResponse, i));
            threads[i].start();
        }

        for (Thread thread : threads) {
            while (thread.isAlive()) {

            }
        }
    }
}

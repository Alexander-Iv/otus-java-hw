package alexander.ivanov.agents;

import java.lang.instrument.Instrumentation;

/*
* java -javaagent:hw04-AutomagicallyLogging-0.01.jar -jar hw04-AutomagicallyLogging-0.01.jar
* */
public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain start");
    }
}

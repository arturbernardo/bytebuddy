import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class ByteBuddyAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        installAgent(inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        installAgent(inst);
    }

    private static void installAgent(Instrumentation inst) {
        new AgentBuilder.Default()
            .type(ElementMatchers.nameContainsIgnoreCase("Service"))
            .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                builder.method(ElementMatchers.named("update"))
                        .intercept(MethodDelegation.to(MethodInterceptor.class)))
            .installOn(inst);
    }

    public static class MethodInterceptor {
        @Advice.OnMethodEnter
        public static void enter() {
            System.out.println("Method intercepted!");
        }
    }
}
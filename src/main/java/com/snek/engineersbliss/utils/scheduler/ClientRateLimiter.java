package com.snek.engineersbliss.utils.scheduler;


/**
 * An object that can be used to add a variable tick cooldown to arbitrary code.
 * @since v1.1.0
 */
public class ClientRateLimiter {
    private long cooldownEnd;


    /**
     * Creates a new RateLimiter.
     */
    public ClientRateLimiter() {
        cooldownEnd = 0;
    }


    /**
     * Sets the remaining cooldown time since the current tick.
     * <p>
     * If the previous remaining cooldown is shorter than the new one, the call will have no effect.
     * @param cooldown The cooldown, expressed in ticks. Must be {@code >= 0}.
     */
    public void renewCooldown(final long cooldown) {

        final long cooldownEndNew = ClientScheduler.getTickNum() + cooldown;
        if(cooldownEnd < cooldownEndNew) {
            cooldownEnd = cooldownEndNew;
        }
    }


    /**
     * Checks whether the cooldown has expired.
     * @return True if the cooldown has expired, false otherwise.
     */
    public boolean attempt() {
        return ClientScheduler.getTickNum() >= cooldownEnd;
    }
}



//TODO replace with a proper frameworklib dependency (maybe add client version of this to the lib?)
//TODO replace with a proper frameworklib dependency (maybe add client version of this to the lib?)
//TODO replace with a proper frameworklib dependency (maybe add client version of this to the lib?)
//TODO replace with a proper frameworklib dependency (maybe add client version of this to the lib?)
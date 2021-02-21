package fr.uiytt.eventuhc.gui;

public enum DeconnectionRule {

    INSTANT_KICK(0),
    NORMAL_KICK(30),
    PRIVATE_KICK(60),
    NO_KICK(-1);

    private final int waiting_time;
    DeconnectionRule(int waiting_time) {
        this.waiting_time = waiting_time;
    }
    /**
     * @return the time to wait before kick
     */
    public int getWaitingTime() {
        return waiting_time;
    }
    /**
     * This search for a matching rule
     * @param name in all capital letters of a rule
     * @return a rule or null if no rule match
     */
    public static DeconnectionRule getFromString(String name) {
        DeconnectionRule[] rules = DeconnectionRule.values();
        for (DeconnectionRule rule : rules) {
            if (rule.name().equals(name)) {
                return rule;
            }
        }
        return null;
    }
}


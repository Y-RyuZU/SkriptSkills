package skriptskills.skriptskills;

import ch.njol.skript.lang.Effect;
import org.bukkit.entity.Player;

import java.util.HashMap;

public abstract class Continuous extends Effect {
    public static HashMap<Player, Integer> Continuous = new HashMap<>();
    public static HashMap<Player, Long> ContinuousTime = new HashMap<>();

    public static int getContinuous(Player p , int interval) {
        if(ContinuousTime.containsKey(p) && (System.currentTimeMillis() - ContinuousTime.get(p)) < interval * 50) {
            return Continuous.get(p);
        } else {
            Continuous.put(p , 0);
            return 0;
        }
    }

    public static void addContinuous(Player p , int interval) {
        Continuous.put(p , getContinuous(p , interval) + 1);
        ContinuousTime.put(p , System.currentTimeMillis());
    }

    public static void setContinuous(Player p , int continuous) {
        Continuous.put(p , continuous);
        ContinuousTime.put(p , System.currentTimeMillis());
    }
}

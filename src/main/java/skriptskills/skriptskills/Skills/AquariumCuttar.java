package skriptskills.skriptskills.Skills;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.function.Function;
import ch.njol.skript.lang.function.Functions;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import skriptskills.skriptskills.Continuous;
import skriptskills.skriptskills.SkriptSkills;
import skriptskills.skriptskills.Utilities;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;

public class AquariumCuttar extends Continuous {
    private Expression<Player> player;

    static {
        Skript.registerEffect(AquariumCuttar.class, "aquariumcuttar %players%");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.player = (Expression<Player>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Aquarium Cuttar";
    }

    @Override
    protected void execute(Event event) {
        if (player == null)  return;
        for (Player p : player.getAll(event)) {
            int slot = p.getInventory().getHeldItemSlot();
            ItemStack item = p.getInventory().getItemInMainHand();
            addContinuous(p , 4 * 20);
            BukkitRunnable runnable = new BukkitRunnable() {
                int count = 1;
                int angle = 270;
                Vector eye = p.getEyeLocation().toVector();
                Vector dir = p.getEyeLocation().getDirection();
                Vector v;
                Vector u;
                double block = 5;
                int Continuous = getContinuous(p , 4 * 20);
                @Override
                public void run() {
                    if(Utilities.getBetweenNumber(count , 3, 3) && Continuous == 1) {
                        Vector dir2 = p.getEyeLocation().getDirection();
                        if(dir2.equals(dir)) {
                            setContinuous(p , 0);
                            cancel();
                        }
                        v = dir.clone().midpoint(dir2).normalize();
                        u = dir2.clone().subtract(dir).normalize();
                        p.getWorld().playSound(p.getLocation() , Sound.ENTITY_WITHER_SHOOT , 2 ,2);
                    }
                    if(Utilities.getBetweenNumber(count , 4 , 9) && Continuous == 1) {
                        for(int i = 0 ; i < 2 ; i++) {
                            angle += 15;
                            for(double r = 0.3; r <= 0.8 ; r += 0.1) {
                                Vector pos = eye.clone().add(v.clone().multiply(2));
                                double rad = (double) angle / 180 * Math.PI;
                                pos.add(v.clone().multiply(Math.cos(rad) * r * 3));
                                pos.add(u.clone().multiply(Math.sin(rad) * 3));
                                Location acloc = pos.toLocation(p.getWorld()).clone();
                                Utilities.spawnParticle(acloc , Particle.END_ROD , 1 , 0 , 0 , 0 , 0  , 256);
                                Utilities.spawnRedDustParticle(acloc , 60 , 179 , 255 , 1 , 0 , 0 , 0 , 256);
                                if((i == 0) && r == 0.5) {
                                    for(LivingEntity ent : Utilities.getNearByLivingEntityFromBlock(acloc , 1)) {
                                        if(!Utilities.getNoTargetEntityType2(ent , p , slot)) {
                                            continue;
                                        }
                                        Utilities.addDamage(ent , 5 , p , slot);
                                    }
                                }
                            }
                        }
                    }
                    if(Utilities.getBetweenNumber(count , 3, 3) && Continuous == 2) {
                        Vector dir2 = p.getEyeLocation().getDirection();
                        if(dir2.equals(dir)) {
                            setContinuous(p , 0);
                            cancel();
                        }
                        v = dir.clone().midpoint(dir2).normalize();
                        u = dir2.clone().subtract(dir).normalize();
                        p.getWorld().playSound(p.getLocation() , Sound.ENTITY_WITHER_SHOOT , 2 ,2);
                    }
                    if(Utilities.getBetweenNumber(count , 4 , 9) && Continuous == 2) {
                        for(int i = 0 ; i < 2 ; i++) {
                            angle += 15;
                            for(double r = 0.3; r <= 0.8 ; r += 0.1) {
                                Vector pos = eye.clone().add(v.clone().multiply(2));
                                Vector pos2 = eye.clone().add(v.clone().multiply(2));
                                Vector u2 = v.clone().crossProduct(u).normalize();
                                double rad = (double) angle / 180 * Math.PI;
                                pos.add(v.clone().multiply(Math.cos(rad) * r * 4));
                                pos.add(u.clone().multiply(Math.sin(rad) * 4));
                                pos2.add(v.clone().multiply(Math.cos(rad) * r * 4));
                                pos2.add(u2.clone().multiply(Math.sin(rad) * 4));
                                Location acloc = pos.toLocation(p.getWorld()).clone();
                                Location acloc2 = pos2.toLocation(p.getWorld()).clone();
                                Utilities.spawnParticle(acloc , Particle.END_ROD , 1 , 0 , 0 , 0 , 0  , 256);
                                Utilities.spawnRedDustParticle(acloc , 60 , 179 , 255 , 1 , 0 , 0 , 0 , 256);
                                Utilities.spawnParticle(acloc2 , Particle.END_ROD , 1 , 0 , 0 , 0 , 0  , 256);
                                Utilities.spawnRedDustParticle(acloc2 , 60 , 179 , 255 , 1 , 0 , 0 , 0 , 256);
                                if(i == 0 && r == 0.5) {
                                    for(LivingEntity ent : Utilities.getNearByLivingEntityFromRadius(acloc , 2)) {
                                        if(!Utilities.getNoTargetEntityType2(ent , p , slot)) {
                                            continue;
                                        }
                                        Utilities.addDamage(ent , 5 , p , slot);
                                    }
                                }
                            }
                        }
                    }
                    if(Utilities.getBetweenNumber(count , 3, 3)  && Continuous == 3) {
                        Vector dir2 = p.getEyeLocation().getDirection();
                        if(dir2.equals(dir)) {
                            setContinuous(p , 0);
                            cancel();
                        }
                        v = dir.clone().midpoint(dir2).normalize();
                        u = dir2.clone().subtract(dir).normalize();
                        p.getWorld().playSound(p.getLocation() , Sound.ENTITY_WITHER_SHOOT , 2 ,2);
                    }
                    if(Utilities.getBetweenNumber(count , 4 , 9)  && Continuous == 3) {
                        for(int i = 0 ; i < 3 ; i++) {
                            angle += 10;
                            for(double r = 0.3; r <= 0.8 ; r += 0.1) {
                                Vector pos = eye.clone().add(v.clone().multiply(2));
                                double rad = (double) angle / 180 * Math.PI;
                                pos.add(v.clone().multiply(Math.cos(rad) * r * 6));
                                pos.add(u.clone().multiply(Math.sin(rad) * 6));
                                Location acloc = pos.toLocation(p.getWorld()).clone();
                                Utilities.spawnParticle(acloc , Particle.END_ROD , 1 , 0 , 0 , 0 , 0  , 256);
                                Utilities.spawnRedDustParticle(acloc , 60 , 179 , 255 , 1 , 0 , 0 , 0 , 256);
                                if(r == 0.3 || r == 0.5 || r == 0.8) {
                                    for(LivingEntity ent : Utilities.getNearByLivingEntityFromRadius(acloc , 1)) {
                                        if(!Utilities.getNoTargetEntityType2(ent , p , slot)) {
                                            continue;
                                        }
                                        Utilities.addDamage(ent , 5 , p , slot);
                                    }
                                }
                            }
                        }
                    }
                    if(Utilities.getBetweenNumber(count , 9 , 27) && Continuous == 3) {
                        for(int s = 1 ; s <= 2 ; s++) {
                            block += 0.7;
                            angle = 270 + (s == 1 ? 0 : 5);
                            for(int a = 0 ; a < 18 ; a++) {
                                angle += 10;
                                Vector pos = eye.clone().add(v.clone().multiply(block));
                                pos.add(v.clone().multiply(Math.cos(Math.toRadians(angle)) * 0.8 * 3));
                                pos.add(u.clone().multiply(Math.sin(Math.toRadians(angle)) * 3));
                                Location acloc = pos.toLocation(p.getWorld()).clone();
                                Utilities.spawnRedDustParticle(acloc , 60 , 179 , 255 , 1 , 0 , 0 , 0 , 256);
                                Utilities.spawnParticle(acloc , Particle.DRIP_WATER , 1 , 0 , 0 , 0 , 0  , 256);
                                if(a % 2 == 0) {
                                    if(a == 8) {
                                        if(!Utilities.ThroughBlocks.contains(acloc.getBlock().getType())) {
                                            setContinuous(p , 0);
                                            cancel();
                                            return;
                                        }
                                    }
                                    for(LivingEntity ent : Utilities.getNearByLivingEntityFromRadius(acloc , 1)) {
                                        if(!Utilities.getNoTargetEntityType2(ent , p , slot)) {
                                            continue;
                                        }
                                        Utilities.addDamage(ent , 10 , p , slot);
                                    }
                                }
                            }
                        }
                        if(count == 27) {
                            u = v.clone().rotateAroundY(Math.toRadians(90));
                            Location circlepoint = eye.clone().toLocation(p.getWorld()).clone().add(v.clone().multiply(block + 3));
                            for(int i = 0 ; i < 36 ; i++) {
                                Location acloc = circlepoint.clone().add(v.clone().multiply(Math.cos(Math.toRadians(i * 10)) * 5));
                                double m = Math.sin(Math.toRadians(i * 10)) * 5;
                                Location cp1 = acloc.clone().add(u.clone().multiply(m).rotateAroundAxis(v , Math.toRadians(40)));
                                Location cp2 = acloc.clone().add(u.clone().multiply(m).rotateAroundAxis(v , Math.toRadians(320)));
                                Vector vec1 = circlepoint.clone().toVector().subtract(cp1.toVector()).multiply(-0.1);
                                Vector vec2 = circlepoint.clone().toVector().subtract(cp2.toVector()).multiply(-0.1);
                                Utilities.spawnParticle(cp1 , Particle.DRIP_WATER , vec1 , 256);
                                Utilities.spawnParticle(cp1 , Particle.END_ROD , vec1 , 256);
                                Utilities.spawnRedDustParticle(cp1 , 60 , 179 , 255 , vec1 , 256);
                                Utilities.spawnParticle(cp2 , Particle.DRIP_WATER , vec2  , 256);
                                Utilities.spawnParticle(cp2 , Particle.END_ROD , vec2 , 256);
                                Utilities.spawnRedDustParticle(cp2 , 60 , 179 , 255 , vec2 , 256);
                            }
                            p.getWorld().playSound(circlepoint , Sound.ENTITY_FISHING_BOBBER_SPLASH , 3 ,0);
                            for(LivingEntity ent : Utilities.getNearByLivingEntityFromRadius(circlepoint , 7)) {
                                if(!Utilities.getNoTargetEntityType2(ent , p , slot)) {
                                    continue;
                                }
                                Utilities.addDamage(ent , 20 , p , slot);
                            }
                            setContinuous(p , 0);
                        }
                    }
                    if((count == 9 && Continuous != 3) || count == 27) {
                        cancel();
                    }
                    count++;
                }
            };
            runnable.runTaskTimer(SkriptSkills.getPlugin() , 0 ,1);
        }
    }
}

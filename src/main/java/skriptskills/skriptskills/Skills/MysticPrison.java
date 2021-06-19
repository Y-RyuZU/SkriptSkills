package skriptskills.skriptskills.Skills;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
import java.util.Objects;

public class MysticPrison extends Continuous {
    private Expression<Player> player;

    static {
        Skript.registerEffect(MysticPrison.class, "mysticprison %players%");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.player = (Expression<Player>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Mystic Prison";
    }

    @Override
    protected void execute(Event event) {
        if (player == null) return;
        for (Player p : player.getAll(event)) {
            int slot = p.getInventory().getHeldItemSlot();
            BukkitRunnable runnable = new BukkitRunnable() {
                int count = 1;
                double s = 0;
                double block = 0;
                Location loc;
                Location sloc;
                Vector backvec = p.getLocation().getDirection().clone().setY(0).normalize().multiply(-1.5).setY(0.8).multiply(1.2);
                Vector v;
                Vector u;
                Vector w;
                Vector p1 = new Vector(0, -150, 0);
                Vector p2 = new Vector(0, -70, 0);
                Vector p3 = new Vector(155, -50, 0);
                Vector p4 = new Vector(155, 30, 0);
                Vector p5 = new Vector(155, 120, 0);
                Vector p6 = new Vector(30, 135, 0);
                Vector p7 = new Vector(0, 30, 0);

                @Override
                public void run() {
                    if (Utilities.getBetweenNumber(count, 1, 10)) {
                        if (count == 1) {
                            loc = p.getLocation().clone().add(0, 0.2, 0);
                        }
                        if (count % 3 == 0) {
                            p.setVelocity(backvec);
                        }
                    }
                    if (Utilities.getBetweenNumber(count, 11, 150)) {
                        p.setVelocity(new Vector(0, 0, 0));
                        p.setGravity(false);
                        if (count % 2 == 0) {
                            for (double y = 0; y <= 7; y += 0.5) {
                                for (int i = 0; i < 360; i += 30) {
                                    Location acloc = loc.clone().add(Math.cos(Math.toRadians(i)) * 7, y, Math.sin(Math.toRadians(i)) * 7);
                                    Utilities.spawnRedDustParticle(acloc, 218, 112, 214, 1, 0, 0, 0, 256);
                                    if (y == 7) {
                                        Location crossloc;
                                        for (int n = 0; n < 90; n += 15) {
                                            double rad1 = (double) n / 180 * Math.PI;
                                            Vector v = acloc.clone().toVector().clone().subtract(loc.clone().add(0, y, 0).toVector()).normalize();
                                            Vector u = new Vector(0, 1, 0).normalize();
                                            crossloc = loc.clone().add(0, y, 0);
                                            crossloc.add(v.clone().multiply(Math.cos(rad1) * 7));
                                            crossloc.add(u.clone().multiply(Math.sin(rad1) * 7));
                                            Utilities.spawnRedDustParticle(crossloc, 255, 105, 180, 1, 0, 0, 0, 256);
                                        }
                                        crossloc = loc.clone().add(0, y + 5, 0);
                                        Utilities.spawnRedDustParticle(crossloc, 255, 105, 180, 1, 0, 0, 0, 256);
                                    }
                                }
                            }
                        }

                        for (Entity ent : Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc, 7, 15, 7)) {
                            if (ent instanceof LivingEntity) {
                                LivingEntity livent = (LivingEntity) ent;
                                if (ent.getType().equals(EntityType.ARMOR_STAND)) {
                                    continue;
                                }
                                Location entloc = loc.clone();
                                if (entloc.getY() > livent.getLocation().getY())
                                    entloc.clone().setY(livent.getEyeLocation().getY());
                                if (6 <= livent.getEyeLocation().distance(entloc) && livent.getEyeLocation().distance(entloc) <= 7) {
                                    Vector pull = loc.clone().subtract(livent.getEyeLocation()).toVector().normalize().multiply(1.2);
                                    livent.setVelocity(pull);
                                    Utilities.addDamage(livent, 1, p, slot);
                                }
                            }
                        }
                    }
                    if (count == 15) {
                        sloc = p.getLocation().clone();
                        Utilities.spawnParticle(sloc, Particle.END_ROD, 10, 0.2, 0, 0, 0, 256);
                        Utilities.spawnParticle(loc, Particle.END_ROD, 10, 0.2, 0, 0, 0, 256);
                        v = loc.clone().toVector().clone().subtract(p.getLocation().clone().toVector()).normalize();
                        u = v.clone().setY(0).rotateAroundY(Math.toRadians(90)).normalize();
                        w = v.clone().crossProduct(u).normalize();
                    }
                    if (Utilities.getBetweenNumber(count, 15, 100)) {
                        if (p.getLocation().distance(sloc) >= 30) {
                            cancel();
                            p.setGravity(true);
                        }
                        Location bowpoint = sloc.clone().add(v.clone().multiply(5));
                        Location arrowpoint = bowpoint.clone().add(v.clone().multiply(block));
                        if (Utilities.getBetweenNumber(count, 15, 35)) {
                            for (double i = 0; i <= 5; i += 0.5) {
                                Vector g1 = bowpoint.clone().add(u.clone().multiply(4)).clone().toVector().clone().subtract(arrowpoint.clone().toVector()).normalize();
                                Vector g2 = bowpoint.clone().add(u.clone().multiply(-4)).clone().toVector().clone().subtract(arrowpoint.clone().toVector()).normalize();
                                Location acloc = arrowpoint.clone().add(g1.clone().multiply(i));
                                Location acloc2 = arrowpoint.clone().add(g2.clone().multiply(i));
                                Utilities.spawnRedDustParticle(acloc, 192, 192, 192, 1, 0, 0, 0, 256);
                                Utilities.spawnRedDustParticle(acloc2, 192, 192, 192, 1, 0, 0, 0, 256);
                            }
                            for (double i = 0; i < 18; i++) {
                                Location acloc = bowpoint.clone().add(u.clone().multiply(Math.cos(Math.toRadians(i * 10)) * 4)).add(v.clone().multiply(Math.sin(Math.toRadians(i * 10)) * 8));
                                Utilities.spawnRedDustParticle(acloc, 255, 200, 0, 1, 0, 0, 0, 256);
                            }
                        }
                        for (double i = 0; i <= 8; i += 0.5) {
                            Location acloc = arrowpoint.clone().add(v.clone().multiply(i));
                            Utilities.spawnRedDustParticle(acloc, 255, 105, 180, 1, 0, 0, 0, 256);
                            if (i == 8) {
                                for (double n = 0; n < 36; n += 2) {
                                    Location acloc2 = arrowpoint.clone().add(v.clone().multiply(8)).add(u.clone().multiply(Math.cos(Math.toRadians(n * 10))).add(w.clone().multiply(Math.sin(Math.toRadians(n * 10)))));
                                    Utilities.spawnRedDustParticle(acloc2, 255, 105, 180, 1, 0, 0, 0, 256);
                                }
                            }
                        }
                        for (double j = 0; j <= 60; j++) {
                            Vector acloc = arrowpoint.clone().add(v.clone().multiply(6)).toVector();
                            Vector bcp1 = Utilities.BezierCurvePoint(
                                    u.clone().multiply(p1.getX()).add(w.clone().multiply(p1.getY())),
                                    u.clone().multiply(p2.getX()).add(w.clone().multiply(p2.getY())),
                                    u.clone().multiply(p3.getX()).add(w.clone().multiply(p3.getY())),
                                    u.clone().multiply(p4.getX()).add(w.clone().multiply(p4.getY())),
                                    j / 60).multiply(0.02);
                            Vector bcp2 = Utilities.BezierCurvePoint(
                                    u.clone().multiply(p4.getX()).add(w.clone().multiply(p4.getY())),
                                    u.clone().multiply(p5.getX()).add(w.clone().multiply(p5.getY())),
                                    u.clone().multiply(p6.getX()).add(w.clone().multiply(p6.getY())),
                                    u.clone().multiply(p7.getX()).add(w.clone().multiply(p7.getY())),
                                    j / 60).multiply(0.02);
                            Utilities.spawnRedDustParticle(acloc.toLocation(p.getWorld()).clone().add(bcp1), 255, 105, 180, 1, 0, 0, 0, 256);
                            Utilities.spawnParticle(acloc.toLocation(p.getWorld()).clone().add(bcp1), Particle.END_ROD, 1, 0, 0, 0, 0, 256);
                            Utilities.spawnRedDustParticle(acloc.toLocation(p.getWorld()).clone().add(bcp2), 255, 105, 180, 1, 0, 0, 0, 256);
                            Utilities.spawnParticle(acloc.toLocation(p.getWorld()).clone().add(bcp2), Particle.END_ROD, 1, 0, 0, 0, 0, 256);
                            Utilities.spawnRedDustParticle(acloc.toLocation(p.getWorld()).clone().add(bcp1.clone().rotateAroundAxis(w, Math.toRadians(180))), 218, 112, 214, 1, 0, 0, 0, 256);
                            Utilities.spawnParticle(acloc.toLocation(p.getWorld()).clone().add(bcp1.clone().rotateAroundAxis(w, Math.toRadians(180))), Particle.END_ROD, 1, 0, 0, 0, 0, 256);
                            Utilities.spawnRedDustParticle(acloc.toLocation(p.getWorld()).clone().add(bcp2.clone().rotateAroundAxis(w, Math.toRadians(180))), 218, 112, 214, 1, 0, 0, 0, 256);
                            Utilities.spawnParticle(acloc.toLocation(p.getWorld()).clone().add(bcp2.clone().rotateAroundAxis(w, Math.toRadians(180))), Particle.END_ROD, 1, 0, 0, 0, 0, 256);
                        }
                        for (double i = 6; i <= 8; i += 0.5) {
                            if (!Utilities.ThroughBlocks.contains(arrowpoint.clone().add(v.clone().multiply(i)).getBlock().getType())) {
                                count = 101;
                                break;
                            }
                        }
                        if (Utilities.getBetweenNumber(count, 35, 100)) {
                            block += 2;
                        } else {
                            block -= 0.2;
                        }
                    }
                    if (count == 101) {
                        v.setY(0);
                        for (double j = 0; j <= 60; j++) {
                            Vector bcp1 = Utilities.BezierCurvePoint(
                                    u.clone().multiply(p1.getX()).add(v.clone().multiply(p1.getY())),
                                    u.clone().multiply(p2.getX()).add(v.clone().multiply(p2.getY())),
                                    u.clone().multiply(p3.getX()).add(v.clone().multiply(p3.getY())),
                                    u.clone().multiply(p4.getX()).add(v.clone().multiply(p4.getY())),
                                    j / 60).multiply(0.03);
                            Vector bcp2 = Utilities.BezierCurvePoint(
                                    u.clone().multiply(p4.getX()).add(v.clone().multiply(p4.getY())),
                                    u.clone().multiply(p5.getX()).add(v.clone().multiply(p5.getY())),
                                    u.clone().multiply(p6.getX()).add(v.clone().multiply(p6.getY())),
                                    u.clone().multiply(p7.getX()).add(v.clone().multiply(p7.getY())),
                                    j / 60).multiply(0.03);
                            Location cp1 = loc.clone().add(bcp1);
                            Location cp2 = loc.clone().add(bcp2);
                            Location cp3 = loc.clone().add(bcp1.clone().rotateAroundAxis(v, Math.toRadians(180)));
                            Location cp4 = loc.clone().add(bcp2.clone().rotateAroundAxis(v, Math.toRadians(180)));
                            Vector vec1 = loc.clone().toVector().subtract(cp1.toVector().clone()).multiply(-0.2);
                            Vector vec2 = loc.clone().toVector().subtract(cp2.toVector().clone()).multiply(-0.2);
                            Vector vec3 = loc.clone().toVector().subtract(cp3.toVector().clone()).multiply(-0.2);
                            Vector vec4 = loc.clone().toVector().subtract(cp4.toVector().clone()).multiply(-0.2);
                            Utilities.spawnParticle(cp1, Particle.END_ROD, vec1, 256);
                            Utilities.spawnParticle(cp2, Particle.END_ROD, vec2, 256);
                            Utilities.spawnParticle(cp3, Particle.END_ROD, vec3, 256);
                            Utilities.spawnParticle(cp4, Particle.END_ROD, vec4, 256);
                        }
                        for (LivingEntity ent : Utilities.getNearByLivingEntityFromRadius(loc, 7)) {
                            if (!Utilities.getNoTargetEntityType2(ent, p, slot)) {
                                continue;
                            }
                            Utilities.addDamage(ent, 20, p, slot);
                        }
                    }
                    if (count >= 101) {
                        cancel();
                        p.setGravity(true);
                    }
                    count++;
                }
            };
            runnable.runTaskTimer(SkriptSkills.getPlugin(), 1, 2);
        }
    }
}
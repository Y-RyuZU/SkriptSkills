package skriptskills.skriptskills;

import ch.njol.skript.lang.function.Functions;
import net.minecraft.server.v1_16_R3.PacketPlayOutPosition;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import sun.misc.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Utilities {
    public static final List<Material> ThroughBlocks = Arrays.asList(Material.GRASS , Material.TALL_GRASS , Material.WATER , Material.LAVA , Material.AIR , Material.SNOW);

    public static boolean getBetweenNumber(int num, int min, int max) {
        if(min <= num && num <= max) {
            return true;
        }
        return false;
    }

    public boolean getNoTargetEntityType(Entity ent , Player p) {
        if(!(ent instanceof LivingEntity)) {
            return false;
        }
        if(ent instanceof ArmorStand) {
            return false;
        }
        if(ent.equals(p)) {
            return false;
        }
        if(ent instanceof Player) {
            Player ep = (Player) ent;
            if(ep.getGameMode().equals(GameMode.SPECTATOR)) {
                return false;
            }
        }
        return true;
    }

    public static boolean getNoTargetEntityType2(Entity ent , Player p , int slot) {
        if(!(ent instanceof LivingEntity)) {
            return false;
        }
        if(ent instanceof ArmorStand) {
            return false;
        }
        if(ent.equals(p)) {
            return false;
        }
        if(ent instanceof Player) {
            Player ep = (Player) ent;
            if(ep.getGameMode().equals(GameMode.SPECTATOR)) {
                return false;
            }
        }
        return true;
    }

    public static List<Entity> getNearByEntityFromRadius(Location loc, double radius) {
        List<Entity> entities = new ArrayList<>();
        for(Entity ent :  Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc, radius, radius, radius)) {
            if(ent.getLocation().distance(loc) <= radius) {
                entities.add(ent);
            }
        }
        return entities;
    }

    public static List<LivingEntity> getNearByLivingEntityFromRadius(Location loc, double radius) {
        List<LivingEntity> entities = new ArrayList<>();
        for(Entity ent :  Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc, radius, radius, radius)) {
            if(ent instanceof LivingEntity) {
                LivingEntity livent = (LivingEntity) ent;
                if(ent.getType().equals(EntityType.ARMOR_STAND)) {
                    continue;
                }
                if(livent.getLocation().clone().add(0 , livent.getEyeHeight() / 2 , 0).distance(loc) <= radius) {
                    entities.add(livent);
                }
            }
        }
        return entities;
    }


    public static List<LivingEntity> getNearByLivingEntityFromBlock(Location loc, double block) {
        List<LivingEntity> entities = new ArrayList<>();
        for(Entity ent :  Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc, block, block, block)) {
            if(ent instanceof LivingEntity) {
                LivingEntity livent = (LivingEntity) ent;
                if(ent.getType().equals(EntityType.ARMOR_STAND)) {
                    continue;
                }
                entities.add(livent);
            }
        }
        return entities;
    }

    public static Location toLocationFromString(String stloc) {
        String[] datas = stloc.split(",");
        return new Location(Bukkit.getWorld(datas[0]), Double.parseDouble(datas[1]) , Double.parseDouble(datas[2]), Double.parseDouble(datas[3]));
    }

    public static ItemStack ItemNameSet(Material m, String s) {
        ItemStack myItem = new ItemStack(m, 1);
        ItemMeta im = myItem.getItemMeta();
        im.setDisplayName(s);
        im.setUnbreakable(true);
        List<ItemFlag> ItemHideFlags = Arrays.asList(ItemFlag.HIDE_ATTRIBUTES , ItemFlag.HIDE_DESTROYS ,ItemFlag.HIDE_ENCHANTS , ItemFlag.HIDE_PLACED_ON , ItemFlag.HIDE_POTION_EFFECTS , ItemFlag.HIDE_UNBREAKABLE);
        for(ItemFlag IF : ItemHideFlags) {
            im.addItemFlags(IF);
        }
        myItem.setItemMeta(im);
        return myItem;
    }

    public static Vector BezierCurvePoint(Vector p1, Vector p2, Vector p3, Vector p4, double t) {
        return p1.clone().multiply(Math.pow(1 - t, 3))
                .add(p2.clone().multiply(3 * Math.pow(1 - t, 2) * t))
                .add(p3.clone().multiply(3 * (1 - t) * Math.pow(t, 2)))
                .add(p4.clone().multiply(Math.pow(t, 3)));
    }

    public static Vector varianceVector(Vector vector, double variance) {
        Vector u = vector.clone().crossProduct(new Vector(0, 1, 0)).normalize();
        Vector v = vector.clone().crossProduct(u).normalize();

        Random random = new Random();
        double uRand = random.nextGaussian() * variance;
        double vRand = random.nextGaussian() * variance;

        return vector.clone()
                .add(u.clone().multiply(uRand))
                .add(v.clone().multiply(vRand))
                .normalize();
    }

    public static Location getChestLocation(LivingEntity ent) {
        return ent.getLocation().clone().add(0 , ent.getEyeHeight() / 2 , 0);
    }

    public static Vector SpherePoint(Vector c, double r, double a1, double a2) {
        return c.clone().add(new Vector(Math.cos(a1) * Math.sin(a2), Math.cos(a2), Math.sin(a1) * Math.sin(a2)).multiply(r));
    }

    public static String getItemDisplay(ItemStack item) {
        if(item == null || item.getType().equals(Material.AIR)) { return null; }
        if(item.getItemMeta() == null) { return null; }
        if(item.getItemMeta().getDisplayName() == "") { return null; }
        return item.getItemMeta().getDisplayName();
    }

/*    public static String getName(String uuid) {
        String url = "https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names";
        try {
            @SuppressWarnings("deprecation")
            String nameJson = IOUtils.toString(new URL(url));
            JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
            String playerSlot = nameValue.get(nameValue.size()-1).toString();
            JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
            return nameObject.get("name").toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public static String getUuid(String name) {
        String url = "https://api.mojang.com/users/profiles/minecraft/"+name;
        try {
            @SuppressWarnings("deprecation")
            String UUIDJson = IOUtils.toString(new URL(url));
            if(UUIDJson.isEmpty()) return "invalid name";
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
            return uuidHyhenation(UUIDObject.get("id").toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return "error";
    }*/

    private static String uuidHyhenation(String uuid) {
        return uuid.substring(0 , 8) + "-"
                + uuid.substring(8 , 12) + "-"
                + uuid.substring(12 , 16) + "-"
                + uuid.substring(16 , 20) + "-"
                + uuid.substring(20 , 32);
    }

    public static void addDamage(LivingEntity ent , double damage , Player p , int slot) {
        Object[][] objects = new Object[5][1];
        Integer[] ArraySlot = new Integer[1];
        ArraySlot[0] = slot;
        objects[0] = ArraySlot;
        LivingEntity[] ArrayEnt = new LivingEntity[1];
        ArrayEnt[0] = ent;
        objects[1] = ArrayEnt;
        Player[] ArrayAttacker = new Player[1];
        ArrayAttacker[0] = p;
        objects[2] = ArrayAttacker;
        Double[] ArrayDamage = new Double[1];
        ArrayDamage[0] = damage;
        objects[3] = ArrayDamage;
        Boolean[] ArrayKickBack = new Boolean[1];
        ArrayKickBack[0] = true;
        objects[4] = ArrayKickBack;
        Functions.getFunction("makeDamage").execute(objects);
    }

    public static double Round(double value , int order) {
        if(order < 1) {
            return (int) Math.round(value);
        }
        return Math.round((value * Math.pow(10 , order))) / Math.pow(10 , order);
    }

    public static void RelativeMove(Player p , double x , double y , double z , float yaw , float pitch) {
        HashSet<PacketPlayOutPosition.EnumPlayerTeleportFlags> set = new HashSet<>();
        set.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.X);
        set.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y);
        set.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z);
        set.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT);
        set.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT);
        PacketPlayOutPosition packet = new PacketPlayOutPosition(x, y, z, yaw, pitch, set, 0);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

    public static boolean canGive(Inventory inventory, ItemStack item) {
        if (inventory.firstEmpty() > 0) return true;
        int stackSize = item.getType().getMaxStackSize();
        List<ItemStack> list = Arrays.stream(inventory.getContents()).filter(e -> e.isSimilar(item)).collect(Collectors.toList());
        int slotCount = list.size();
        int sum = list.stream().mapToInt(ItemStack::getAmount).sum();
        return stackSize * slotCount > sum;
    }

    public static void Recoil(Player p , float yaw , float pitch) {
        HashSet<PacketPlayOutPosition.EnumPlayerTeleportFlags> set = new HashSet<>();
        set.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.X);
        set.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y);
        set.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z);
        set.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT);
        set.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT);
        PacketPlayOutPosition packet = new PacketPlayOutPosition(0, 0, 0, yaw, pitch, set, 0);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

    public static void spawnParticle(Location loc , Particle particle , int count , double speed , double offX , double offY , double offZ , double distance) {
        for(Entity ent : Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc , distance , distance , distance)) {
            if(ent instanceof Player) {
                Player p = (Player) ent;
                p.spawnParticle(particle , loc , count , offX , offY , offZ , speed);
            }
        }
    }

    public static void spawnParticle(Location loc , Particle particle , Vector vector , double distance) {
        for(Entity ent : Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc , distance , distance , distance)) {
            if(ent instanceof Player) {
                Player p = (Player) ent;
                p.spawnParticle(particle , loc , 0 , vector.getX() , vector.getY() , vector.getZ() , 1);
            }
        }
    }

    public static void spawnRedDustParticle(Location loc , int R , int G , int B ,  int count , double offX , double offY , double offZ , double distance) {
        for(Entity ent : Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc , distance , distance , distance)) {
            if(ent instanceof Player) {
                Player p = (Player) ent;
                p.spawnParticle(Particle.REDSTONE , loc , count , offX , offY , offZ , 0 , new Particle.DustOptions(Color.fromRGB(R , G , B), 1));
            }
        }
    }

    public static void spawnRedDustParticle(Location loc , Color color,  int count , double offX , double offY , double offZ , double distance) {
        for(Entity ent : Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc , distance , distance , distance)) {
            if(ent instanceof Player) {
                Player p = (Player) ent;
                p.spawnParticle(Particle.REDSTONE , loc , count , offX , offY , offZ , 0 , new Particle.DustOptions(color , 1));
            }
        }
    }

    public static void spawnRedDustParticle(Location loc , int R , int G , int B , Vector vector , double distance) {
        for(Entity ent : Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc , distance , distance , distance)) {
            if(ent instanceof Player) {
                Player p = (Player) ent;
                p.spawnParticle(Particle.REDSTONE , loc , 0 , vector.getX() , vector.getY() , vector.getZ() , 1 , new Particle.DustOptions(Color.fromRGB(R , G , B), 1));
            }
        }
    }

    public static void spawnRedDustParticle(Location loc , Color color , Vector vector , double distance) {
        for(Entity ent : Objects.requireNonNull(loc.getWorld()).getNearbyEntities(loc , distance , distance , distance)) {
            if(ent instanceof Player) {
                Player p = (Player) ent;
                p.spawnParticle(Particle.REDSTONE , loc , 0 , vector.getX() , vector.getY() , vector.getZ() , 0 , new Particle.DustOptions(color , 1));
            }
        }
    }

    public static void viewVector(Location point , Vector vec) {
        new BukkitRunnable() {
            int count = 1;
            double block = 0;
            @Override
            public void run() {
                Location acloc = point.clone().add(vec.clone().multiply(block));
                spawnRedDustParticle(acloc , 255 , 0 , 0 ,1 , 0 , 0 , 0 , 256);
                block += 0.4;
                if(block >= 10) {
                    count++;
                    block = 0;
                }
                if(count >= 5) {
                    cancel();
                }
            }
        }.runTaskTimer(SkriptSkills.getPlugin() , 0 , 2);
    }
}

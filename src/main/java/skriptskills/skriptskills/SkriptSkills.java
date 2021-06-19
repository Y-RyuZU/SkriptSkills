package skriptskills.skriptskills;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.lang.function.Functions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class SkriptSkills extends JavaPlugin {
    private static Plugin plugin;
    public static SkriptAddon addon;

    @Override
    public void onEnable() {
        plugin = this;
        boolean already = false;
        for(SkriptAddon a : Skript.getAddons()) {
            if(a.plugin.getName().equals(plugin.getName())) {
                already = true;
                addon = a;
                break;
            }
        }
        if(!already) { addon = Skript.registerAddon(this); }
        try {
            addon.loadClasses("skriptskills.skriptskills", "Skills");
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLogger().info(ChatColor.GREEN + "SkriptSkillsが起動しました");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.GREEN + "SkriptSkillsが停止しました");
    }

    public static Plugin getPlugin() {return plugin;}
}

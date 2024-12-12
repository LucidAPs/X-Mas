package com.lucidaps.xmas;

//I plan to make this plugin bigger. So... 

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemMaker {

    private final ItemStack is;
    private ItemMeta im;

    public ItemMaker(Material material) {
        is = new ItemStack(material);
        im = is.getItemMeta();
    }

    public ItemMaker(Material material, String name) {
        is = new ItemStack(material);
        im = is.getItemMeta();
        im.setDisplayName(name);
    }

    public ItemMaker(Material material, String name, List<String> lore) {
        is = new ItemStack(material);
        im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
    }

    public ItemMaker(Material material, int amount, short durability) {
        is = new ItemStack(material, amount);
        im = is.getItemMeta();
    }

    public ItemMaker(Material material, int amount, short durability, String name) {
        is = new ItemStack(material, amount);
        im = is.getItemMeta();
        im.setDisplayName(name);
    }

    public ItemMaker(Material material, int amount, short durability, String name, List<String> lore) {
        is = new ItemStack(material, amount);
        im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
    }

    public ItemMaker setAmount(int amount) {
        is.setAmount(amount);
        return this;
    }

    public ItemMaker setDurability(short data) {
        //is.setDurability(data);
        ((Damageable) is).setDamage(data);
        return this;
    }

    public ItemMaker setName(String name) {
        im.setDisplayName(ChatColor.RESET + name);
        return this;
    }

    public ItemMaker setLore(List<String> lore) {
        im.setLore(lore);
        return this;
    }

    public ItemMaker addLoreLine(String line) {
        List<String> lore;
        if (im.getLore() != null) {
            lore = im.getLore();
        } else {
            lore = new ArrayList<>();
        }
        lore.add(line);
        im.setLore(lore);

        return this;
    }

    public ItemMaker addEnchant(Enchantment enchantment) {
        im.addEnchant(enchantment, 1, true);
        return this;
    }

    public ItemMaker addEnchant(Enchantment enchantment, int level) {
        im.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemStack make() {
        is.setItemMeta(im);
        im = null;
        return is;
    }
}

package com.joi.races.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.joi.races.Main;

public class FireBreath {

    private int id;
    private final int fpCost = 3; // todo: get fp cost from config
    private Data data = Data.get();
    

    public FireBreath(Player sender) {
        
        data.setFp(sender, data.getFp(sender) - fpCost);
        data.showFp(sender);

        Location senderLocation = sender.getLocation().add(0, 1.3, 0);

        Location senderLocation_angle1 = sender.getLocation().add(0, 1.3, 0);
        senderLocation_angle1.setYaw(senderLocation_angle1.getYaw() + 15/2);

        Location senderLocation_angle2 = sender.getLocation().add(0, 1.3, 0);
        senderLocation_angle2.setYaw(senderLocation_angle2.getYaw() - 15/2);

        Location senderLocation_angle3 = sender.getLocation().add(0, 1.3, 0);
        senderLocation_angle3.setYaw(senderLocation_angle3.getYaw() + 15);

        Location senderLocation_angle4 = sender.getLocation().add(0, 1.3, 0);
        senderLocation_angle4.setYaw(senderLocation_angle4.getYaw() - 15);

        id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
            double t = 0.5;
            @Override
            public void run() {
                for (int i = 0; i < 2; i++) {
                if (t > 3) {
                    cancel();
                    return;
                }
                if (!playEffect(senderLocation, t, sender.getUniqueId()) ||
                !playEffect(senderLocation_angle1, t, sender.getUniqueId()) ||
                !playEffect(senderLocation_angle2, t, sender.getUniqueId()) ||
                !playEffect(senderLocation_angle3, t, sender.getUniqueId()) ||
                !playEffect(senderLocation_angle4, t, sender.getUniqueId())) {
                    cancel();
                    return;
                }
                t = t + 0.2;
                }
            }
        }, 0L, 1);
    }

    public void cancel() {
        Bukkit.getServer().getScheduler().cancelTask(id);
    }

    public boolean playEffect(Location loc, double t, UUID senderId) {
        Vector direction = loc.getDirection().normalize();
        // X
        double x = direction.getX() * t;
        double y = direction.getY() * t;
        double z = direction.getZ() * t;
        loc.add(x, y, z);
        loc.getWorld().spawnParticle(Particle.LAVA, loc, 0);
        loc.getWorld().spawnParticle(Particle.FLAME, loc, 0);
        if (handleEffect(loc.clone(), senderId)) {
            cancel();
            return false;
        }
        loc.subtract(x, y, z);

        // Y
        x = direction.getX() * t;
        y = (direction.getY() + 0.2) * t;
        z = direction.getZ() * t;
        loc.add(x, y, z);
        loc.getWorld().spawnParticle(Particle.FLAME, loc, 0);
        if (handleEffect(loc.clone(), senderId)) {
            cancel();
            return false;
        }
        loc.subtract(x, y, z);

        x = direction.getX() * t;
        y = (direction.getY() - 0.2) * t;
        z = direction.getZ() * t;
        loc.add(x, y, z);
        loc.getWorld().spawnParticle(Particle.FLAME, loc, 0);
        if (handleEffect(loc.clone(), senderId)) {
            cancel();
            return false;
        }
        loc.subtract(x, y, z);
        return true;
    }

    public boolean handleEffect(Location location, UUID senderId) {
        boolean result = false;
        if (isSolid(location.getBlock())) {
            result = true;
        }
        java.util.List<Entity> entities = getEntitiesAroundPoint(location.clone(), 1, senderId);
        if (!entities.isEmpty()) {
            for (Entity entity : entities) {
                if (!(entity instanceof Damageable)) {
                    continue;
                }
                if (entity instanceof Player && !((Player) entity).getGameMode().equals(GameMode.SURVIVAL)) {
                    continue;
                }
                ((Damageable) entity).setFireTicks(40); // todo: get from config
                result = true;
            }
        }
        return result;
    }

    public java.util.List<Entity> getEntitiesAroundPoint(Location location, double radius, UUID senderId) {
        location.setY(location.getY() - 1.6);
		java.util.List<Entity> entities = new ArrayList<Entity>();
		World world = location.getWorld();

		int smallX = (int) (location.getX() - radius) >> 4;
		int bigX = (int) (location.getX() + radius) >> 4;
		int smallZ = (int) (location.getZ() - radius) >> 4;
		int bigZ = (int) (location.getZ() + radius) >> 4;

		for (int x = smallX; x <= bigX; x++) {
			for (int z = smallZ; z <= bigZ; z++) {
				if (world.isChunkLoaded(x, z)) {
					entities.addAll(Arrays.asList(world.getChunkAt(x, z).getEntities()));
				}
			}
		}
		Iterator<Entity> entityIterator = entities.iterator();
		while (entityIterator.hasNext()) {
			Entity e = entityIterator.next();
			if (!e.getWorld().equals(location.getWorld())) {
                entityIterator.remove();
                continue;
            }
            if (e instanceof Player) {
                if (((Player) e).getUniqueId() == senderId) {
                    entityIterator.remove();
                    continue;
                }
            }
            if (e.getLocation().distanceSquared(location) > radius * radius) {
				entityIterator.remove();
			}
		}
		return entities;
	}

    public boolean isSolid(Block block) {
		return block.getType().isOccluding();
	}
    
}

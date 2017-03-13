package de.moddylp.AncientRegions.loader;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import org.bukkit.entity.Player;

import java.io.*;

public class WorldEditHandler6 {
    private final Main plugin;
    private World world;

    public WorldEditHandler6(Main plugin) {
        this.plugin = plugin;
    }

    public boolean restoreRegionBlocks(File file, String regionname, Player p, ProtectedRegion region, Vector dimension) {
        this.plugin.getLogger().info("Restore in progress...");
        try {
            world = LocalWorldAdapter.adapt(new BukkitWorld(p.getWorld()));
            EditSession editSession = plugin.setupWorldEdit().getWorldEdit().getEditSessionFactory().getEditSession(world, 999999999);
            Vector origin = new Vector(region.getMinimumPoint().getBlockX(), region.getMinimumPoint().getBlockY(), region.getMinimumPoint().getBlockZ());
            Closer closer = Closer.create();
            FileInputStream fis = closer.register(new FileInputStream(file));
            BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
            ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(bis);

            WorldData worldData = world.getWorldData();
            LocalSession session = new LocalSession(plugin.setupWorldEdit().getLocalConfiguration());
            Clipboard clipboard = reader.read(worldData);
            clipboard.setOrigin(clipboard.getMinimumPoint());
            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, worldData);
            session.setBlockChangeLimit(999999999);
            session.setClipboard(clipboardHolder);

            Operation operation = clipboardHolder.createPaste(editSession, editSession.getWorld().getWorldData()).to(origin).build();
            Operations.completeLegacy(operation);
            try {
                closer.close();
            } catch (IOException ignored) {
            }
            file.delete();
        } catch (Exception e) {
            this.plugin.getLogger().warning(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean saveRegionBlocks(File file, String regionname, Player p, ProtectedRegion region) {
        World world = null;
        if (p.getWorld() != null) {
            world = LocalWorldAdapter.adapt(new BukkitWorld(p.getWorld()));
        }
        if (world == null) {
            this.plugin.getLogger().warning("Did not save region " + regionname + ", world not found: " + p.getWorld().getName());
            return false;
        }
        EditSession editSession = this.plugin.setupWorldEdit().getWorldEdit().getEditSessionFactory().getEditSession(world, 9999999);

        CuboidRegion selection = new CuboidRegion(world, region.getMinimumPoint(), region.getMaximumPoint());
        BlockArrayClipboard clipboard = new BlockArrayClipboard(selection);
        clipboard.setOrigin(region.getMinimumPoint());
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, new CuboidRegion(world, region.getMinimumPoint(), region.getMaximumPoint()), clipboard, region.getMinimumPoint());
        try {
            Operations.completeLegacy(copy);
        } catch (MaxChangedBlocksException e1) {
            this.plugin.getLogger().warning("Exeeded the block limit while saving schematic of " + regionname);
            return false;
        }
        Closer closer = Closer.create();
        try {
            FileOutputStream fos = closer.register(new FileOutputStream(file));
            BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
            ClipboardWriter writer = closer.register(ClipboardFormat.SCHEMATIC.getWriter(bos));
            writer.write(clipboard, world.getWorldData());

            return true;
        } catch (IOException e) {
            this.plugin.getLogger().warning("An error occured while saving schematic of " + regionname + ", " + e.getMessage());
            return false;
        } finally {
            try {
                closer.close();
            } catch (IOException ignored) {
            }
        }
    }
}

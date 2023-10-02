// porting this absolute godsend of a class from matcha client. Thank you to whoever originally made it. - Lefty.
package org.tungsten.client.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.message.LastSeenMessagesCollector;
import net.minecraft.network.message.MessageBody;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.tungsten.client.Tungsten;
import org.tungsten.client.mixin.ClientPlayNetworkHandlerAccessor;
import org.tungsten.client.mixin.ClientWorldMixin;
import org.tungsten.client.mixin.MinecraftClientAccessor;
import org.tungsten.client.mixin.RenderTickCounterAccessor;
import org.tungsten.client.util.helper.ArrayHelper;

import java.awt.*;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
@ExtensionMethod(ArrayHelper.class)
public class Util {

    static final Random r = new Random();
    private static final char[] A_TO_Z = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    String[] nouns = new String[] { "bird", "clock", "boy", "plastic", "duck", "teacher", "old lady", "professor", "hamster", "dog" };
    String[] verbs = new String[] { "kicked", "ran", "flew", "dodged", "sliced", "rolled", "died", "breathed", "slept", "killed" };
    String[] adjectives = new String[] { "beautiful", "lazy", "professional", "lovely", "dumb", "rough", "soft", "hot", "vibrating", "slimy" };
    String[] adverbs = new String[] { "slowly", "elegantly", "precisely", "quickly", "sadly", "humbly", "proudly", "shockingly", "calmly", "passionately" };
    String[] preposition = new String[] { "down", "into", "up", "on", "upon", "below", "above", "through", "across", "towards" };

    public Stream<LivingEntity> getEntityStream(ClientWorld world) {
        return StreamSupport.stream(world.getEntities().spliterator(), false).filter(entity -> entity instanceof LivingEntity).map(entity -> (LivingEntity) entity);
    }

    public static BlockPos toBlockPos(Vec3d gv3){
        return new BlockPos((int) gv3.x, (int) gv3.y, (int) gv3.z);
    }

    public String randomAZString(int size) {
        StringBuilder buf = new StringBuilder();
        char[] chars = A_TO_Z;

        for (int i = 0; i < size; i++) {
            buf.append(chars[r.nextInt(chars.length)]);
        }
        return buf.toString();
    }

    public BlockIterator iterateOverRange(BlockPos startIncl, BlockPos endIncl, Predicate<BlockPos> validPos) {
        return new BlockIterator(startIncl, endIncl, validPos);
    }

    public String[] getPlayerList() {
        return Tungsten.client.player.networkHandler.getPlayerList().stream().map(playerListEntry -> playerListEntry.getProfile().getName()).toArray(String[]::new);
    }

    public static void handledSend(Packet<?> p){
        if(p instanceof ClickSlotC2SPacket packet){
            Tungsten.client.interactionManager.clickSlot(Tungsten.client.player.currentScreenHandler.syncId, packet.getSlot(), packet.getButton(), packet.getActionType(), Tungsten.client.player);
            return;
        }
        if(p instanceof PlayerInteractItemC2SPacket packet){
            p = new PlayerInteractItemC2SPacket(packet.getHand(), Util.increaseAndCloseUpdateManager());
        }
        if(p instanceof PlayerInteractBlockC2SPacket packet){
            p = new PlayerInteractBlockC2SPacket(packet.getHand(), packet.getBlockHitResult(), Util.increaseAndCloseUpdateManager());
        }
        if(p instanceof PlayerMoveC2SPacket packet){
            return;
        }
        if(p instanceof KeepAliveC2SPacket){
            return;
        }
        if(p instanceof CommandExecutionC2SPacket packet){
            Tungsten.client.getNetworkHandler().sendChatCommand(packet.command());
            return;
        }
        if(p instanceof ChatMessageC2SPacket packet){
            Tungsten.client.getNetworkHandler().sendChatMessage(packet.chatMessage());
            return;
        }

        //the real
        Tungsten.client.player.networkHandler.sendPacket(p);
    }

    // np
    public String[] getPlayersInRenderRange() {
        return StreamSupport.stream(Tungsten.client.world.getEntities().spliterator(), false)
                .filter(entity -> entity instanceof PlayerEntity)
                .map(entity -> ((PlayerEntity) entity).getGameProfile().getName())
                .toArray(String[]::new);
    }

    public boolean giveStack(ItemStack stack) {
        for (int i = 0; i < 9; i++) {
            if (!Tungsten.client.player.getInventory().getStack(i).isEmpty()) {
                continue;
            }

            Tungsten.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + i, stack));
            return true;
        }

        return false;
    }

    private String getUUID(String username) {
        try {
            URL profileURL = URI.create("https://api.mojang.com/users/profiles/minecraft/").resolve(URLEncoder.encode(username, StandardCharsets.UTF_8)).toURL();

            try (InputStream profileInputStream = profileURL.openStream()) {
                JsonObject profileJson = new Gson().fromJson(IOUtils.toString(profileInputStream, StandardCharsets.UTF_8), JsonObject.class);

                return profileJson.get("id").getAsString();
            }
        } catch (Exception e) {
            return "91c71ac4-73a9-4659-a550-1a650731836e";
        }
    }

    public <T> T[] concatArrays(T[] first, T[] second) {
        T[] s = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(first, 0, s, 0, first.length); // s -> 0 - first.length-1
        System.arraycopy(second, 0, s, first.length, second.length); // s -> first.length - s.length-1
        return s;
    }

    public String getUUIDFromStringUsingAPI(String username) {
        try {
            URL profileURL = URI.create("https://api.mojang.com/users/profiles/minecraft/").resolve(URLEncoder.encode(username, StandardCharsets.UTF_8)).toURL();

            try (InputStream profileInputStream = profileURL.openStream()) {
                JsonObject profileJson = new Gson().fromJson(IOUtils.toString(profileInputStream, StandardCharsets.UTF_8), JsonObject.class);

                return profileJson.get("id").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ChatMessageC2SPacket makeChatMessage(String content) {
        Instant instant = Instant.now();
        long l = NetworkEncryptionUtils.SecureRandomUtil.nextLong();
        ClientPlayNetworkHandlerAccessor accessor = ((ClientPlayNetworkHandlerAccessor) Tungsten.client.getNetworkHandler());
        LastSeenMessagesCollector.LastSeenMessages lastSeenMessages = accessor.getLastSeenMessages().collect();
        MessageSignatureData messageSignatureData = accessor.getMessagePacker().pack(new MessageBody(content, instant, l, lastSeenMessages.lastSeen()));
        return new ChatMessageC2SPacket(content, instant, l, messageSignatureData, lastSeenMessages.update());
    }

    public void sleep(long millis) {
        if (millis <= 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {
            //            throw new RuntimeException("Failed to run sleep("+millis+")", e);
        }
    }

    public void swapAndPlace(ItemStack item) {
        int handSlot = Tungsten.client.player.getInventory().selectedSlot + 36;
        Vec3d cpos = Tungsten.client.player.getPos().add(0, -1, 0);
        Tungsten.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(handSlot, item));
        Tungsten.client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND,
                new BlockHitResult(cpos, Direction.UP, Util.toBlockPos(cpos), false),
                Util.increaseAndCloseUpdateManager()));
    }

    public ItemStack setPos(ItemStack i, Vec3d pos) {
        NbtList pos3d = new NbtList();
        pos3d.add(NbtDouble.of(pos.x));
        pos3d.add(NbtDouble.of(pos.y));
        pos3d.add(NbtDouble.of(pos.z));
        NbtCompound tag = i.getOrCreateSubNbt("EntityTag");
        tag.put("Pos", pos3d);
        i.setSubNbt("EntityTag", tag);

        return i;
    }

    public void spawnEndcrystal(BlockPos destination, Vec3d targ_p) {
        String nbt = String.format(Locale.ENGLISH, """
            {
                EntityTag: {
                    id: "minecraft:end_crystal",
                    ShowBottom: 0b,
                    Pos: [%s, %s, %s],
                    BeamTarget: {
                        X: %s,
                        Y: %s,
                        Z: %s
                    }
                }
            }
            """, targ_p.x, targ_p.y, targ_p.z, destination.getX(), destination.getY(), destination.getZ());
        ItemStack item = generateItemStack(Items.BAT_SPAWN_EGG, 1, nbt);
        ItemStack before = Tungsten.client.player.getMainHandStack();
        Tungsten.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + Tungsten.client.player.getInventory().selectedSlot, item));
        Tungsten.client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND,
                new BlockHitResult(Tungsten.client.player.getPos().add(0, -2, 0), Direction.UP, Tungsten.client.player.getBlockPos().offset(Direction.DOWN, 2), false),
                increaseAndCloseUpdateManager()));
        Tungsten.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + Tungsten.client.player.getInventory().selectedSlot, before));
    }

    public int increaseAndCloseUpdateManager() {
        ClientWorld world = Tungsten.client.world;

        PendingUpdateManager pum = getUpdateManager(world);
        int p = pum.getSequence();
        pum.close();
        return p;
    }

    public void setClientTps(float tps) {
        RenderTickCounterAccessor accessor = ((RenderTickCounterAccessor) ((MinecraftClientAccessor) Tungsten.client).getRenderTickCounter());
        accessor.setTickTime(1000f / tps);
    }

    public PendingUpdateManager getUpdateManager(ClientWorld world) {
        return ((ClientWorldMixin) world).acquirePendingUpdateManager();
    }

    @SafeVarargs
    public <T> T firstMatching(Function<T, Boolean> func, T... elements) {
        return Arrays.stream(elements).filter(func::apply).findFirst().orElse(null);
    }

    @SafeVarargs
    public <T> T firstNonNull(T... elements) {
        return firstMatching(Objects::nonNull, elements);
    }

    public NbtElement getElementFromDottedName(NbtCompound source, String name) {
        String[] path = name.split("\\.");
        NbtCompound currentElement = source;
        for (int i = 0; i < path.length; i++) { // go over each segment
            boolean isLast = i + 1 == path.length; // are we at the last one?
            String s = path[i];
            NbtElement nbtElement = currentElement.get(s); // get current segment
            if (!(nbtElement instanceof NbtCompound) && !isLast) { // we still have remaining path segments but this one is a dead end. Huh?
                return null;
            }
            if (nbtElement instanceof NbtCompound nc) {
                currentElement = nc;
            } else {
                return nbtElement; // dead end and this was the last one, perfect
            }
        }
        return currentElement; // we went over all elements and ended up with a trailing compound, alright, return that
    }

    public void setElementFromDottedName(NbtCompound source, String name, NbtElement newElement) {
        String[] path = name.split("\\.");
        NbtCompound currentElement = source;
        for (int i = 0; i < path.length; i++) { // go over each segment
            boolean isLast = i + 1 == path.length; // are we at the last one?
            String s = path[i];
            NbtElement nbtElement = currentElement.get(s); // get current segment
            if (nbtElement == null && !isLast) { // this one is null and we need this to be not null, so create it?
                nbtElement = new NbtCompound();
                currentElement.put(s, nbtElement);
            }
            if (!(nbtElement instanceof NbtCompound) && !isLast) { // we still have remaining path segments but this one is a dead end. Huh?
                throw new IllegalArgumentException("Path " + name + " points to alaska");
            }
            if (isLast) { // the last segment, do it
                if (newElement != null) {
                    currentElement.put(s, newElement);
                } else {
                    currentElement.remove(s);
                }
            } else { // not last, but we already know this is an nbtcompound
                currentElement = (NbtCompound) nbtElement;
            }
        }
    }

    public String[] getAllNbtKeys(NbtCompound nc) {
        List<String> ret = new ArrayList<>();
        Set<String> keys = nc.getKeys();
        for (String key : keys) {
            ret.add(key);
            NbtElement nbtElement = nc.get(key);
            if (nbtElement instanceof NbtCompound nc1) {
                for (String nbtKey : getAllNbtKeys(nc1)) {
                    ret.add(key + "." + nbtKey);
                }
            }
        }
        return ret.toArray(String[]::new);
    }

    public Color lerp(Color a, Color b, float delta) {
        return new Color((int) MathHelper.lerp(delta, a.getRed(), b.getRed()),
                (int) MathHelper.lerp(delta, a.getGreen(), b.getGreen()),
                (int) MathHelper.lerp(delta, a.getBlue(), b.getBlue()),
                (int) MathHelper.lerp(delta, a.getAlpha(), b.getAlpha()));
    }

    public Vec3d transformVec3dWithMatrix(MatrixStack stack, Vec3d in) {
        Matrix4f matrix = stack.peek().getPositionMatrix();
        Vector4f coord = new Vector4f((float) in.getX(), (float) in.getY(), (float) in.getZ(), 1.0F);
        coord.mul(matrix);
        return new Vec3d(coord.x(), coord.y(), coord.z());
    }

    public double getMouseX() {
        return Tungsten.client.mouse.getX() / Tungsten.client.getWindow().getScaleFactor();
    }

    public double getMouseY() {
        return Tungsten.client.mouse.getY() / Tungsten.client.getWindow().getScaleFactor();
    }

    public BitSet searchMatches(String original, String search) {
        int searchIndex = 0;
        BitSet matches = new BitSet();
        char[] chars = search.toLowerCase().toCharArray();
        String lower = original.toLowerCase();
        for (char aChar : chars) {
            if (searchIndex >= original.length()) {
                matches.clear();
                return matches;
            }
            int index;
            if ((index = lower.substring(searchIndex).indexOf(aChar)) >= 0) {
                matches.set(searchIndex + index);
                searchIndex += index + 1;
            } else {
                matches.clear();
                return matches;
            }
        }
        return matches;
    }

    public double roundToDecimal(double n, int point) {
        if (point == 0) {
            return Math.floor(n);
        }
        double factor = Math.pow(10, point);
        return Math.round(n * factor) / factor;
    }

    public double[] calculateLookAt(double px, double py, double pz, PlayerEntity me) {
        double dirx = me.getX() - px;
        double diry = me.getY() + me.getEyeHeight(me.getPose()) - py;
        double dirz = me.getZ() - pz;

        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        //to degree
        pitch = pitch * 180.0d / Math.PI;
        yaw = yaw * 180.0d / Math.PI;

        yaw += 90f;

        return new double[] { yaw, pitch };
    }

    public void applyNbtData(ItemStack source, String nbt) {
        NbtCompound nbtCompound = throwSilently(() -> StringNbtReader.parse(nbt),
                throwable -> Tungsten.LOGGER.error("Tried to parse nbt string {}, resulted in error", nbt.replaceAll("\\P{Print}", "."), throwable));
        if (nbtCompound != null) {
            source.setNbt(nbtCompound);
        }
    }

    public <T> T throwSilently(ThrowingSupplier<T> func, Consumer<Throwable> errorHandler) {
        try {
            return func.get();
        } catch (Throwable t) {
            errorHandler.accept(t);
            return null;
        }
    }

    public LivingEntity getClosestEntityByName(@NonNull String viewName) {
        if (!viewName.isEmpty()) {
            return getEntityStream(Tungsten.client.world).filter(e -> !e.isRemoved() && e.isAlive() && e.isAttackable())
                    .filter(e -> viewName.equalsIgnoreCase(e.getName().getString()))
                    .min(Comparator.comparingDouble(e -> Tungsten.client.player.squaredDistanceTo(e)))
                    .orElse(null);
        }
        return null;
    }

    public static AbstractClientPlayerEntity getPlayerByName(String viewName){
        for(AbstractClientPlayerEntity player : Tungsten.client.world.getPlayers()){
            if(player.getEntityName().equals(viewName)){
                return player;
            }
        }
        return null;
    }

    public ItemStack generateItemStack(Item item, int amount, String tag) {
        ItemStack stack = new ItemStack(item, amount);
        applyNbtData(stack, tag);
        return stack;
    }

    public String getRandomContent() {
        return "The " + adjectives.random() + " " + nouns.random() + " " + adverbs.random() + " " + verbs.random() + " because some " + nouns.random() + " " + adverbs.random() + " " + verbs.random() + " " + preposition.random() + " a " + adjectives.random() + " " + nouns.random() + " which, became a " + adjectives.random() + ", " + adjectives.random() + " " + nouns.random() + ".";
    }

    public String getRandomTitle() {
        return "The " + adjectives.random() + " tale of a " + nouns.random();
    }

    public int[] uuidToInts(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        return new int[] { (int) (msb >> 32), (int) (msb), (int) (lsb >> 32), (int) (lsb) };
    }

    public Vec3d getEntityPosition(Entity entity) {
        float td = Tungsten.client.getTickDelta();
        return new Vec3d(entity.prevX, entity.prevY, entity.prevZ).lerp(entity.getPos(), td);
    }

    public interface ThrowingSupplier<T> {
        T get() throws Throwable;
    }

    @RequiredArgsConstructor
    public static class BlockIterator implements Iterator<BlockPos> {
        final BlockPos startIncl, endIncl;
        final Predicate<BlockPos> validPos;
        boolean reachedEnd = false;
        int currentX, currentY, currentZ;

        @Override
        public boolean hasNext() {
            return !reachedEnd;
        }

        @Override
        public void forEachRemaining(Consumer<? super BlockPos> action) {
            BlockPos next;
            while ((next = next()) != null) {
                action.accept(next);
            }
        }

        @Override
        public BlockPos next() {
            if (reachedEnd) {
                return null;
            }
            BlockPos foundValid = null;
            // Get the sign of the delta values, since ex. (-5) / |(-5)| is -1 and 5 / |5| is 1
            int deltaX = endIncl.getX() - startIncl.getX();
            int signX = deltaX / Math.abs(deltaX);
            int deltaY = endIncl.getY() - startIncl.getY();
            int signY = deltaY / Math.abs(deltaY);
            int deltaZ = endIncl.getZ() - startIncl.getZ();
            int signZ = deltaZ / Math.abs(deltaZ);
            // Flip all negative targets to positive targets if necessary
            for (int y = currentY; y <= Math.abs(deltaY); y++) {
                for (int x = currentX; x <= Math.abs(deltaX); x++) {
                    for (int z = currentZ; z <= Math.abs(deltaZ); z++) {
                        BlockPos offset = new BlockPos(x * signX, y * signY, z * signZ);
                        // Flip them back for the user
                        BlockPos np = startIncl.add(offset);
                        if (foundValid != null) { // Do one more iteration to update our `current` to the next index
                            currentX = x;
                            currentY = y;
                            currentZ = z;
                            return foundValid;
                        }
                        if (validPos.test(np)) {
                            foundValid = np;
                        }
                    }
                    currentZ = 0;
                }
                currentX = 0;
            }
            reachedEnd = true;
            return foundValid; // is gonna be null anyways if we didn't find anything
        }
    }
}

package io.github.jaffe2718.steroid_planet.neoforge.attachment;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FloatSyncHandler implements AttachmentSyncHandler<Float> {


    @Override
    public boolean sendToPlayer(@NotNull IAttachmentHolder holder, @NotNull ServerPlayerEntity to) {
        return holder == to && to.networkHandler != null;
    }

    @Override
    public void write(@NotNull RegistryByteBuf buf, @Nullable Float attachment, boolean initialSync) {
        buf.writeBoolean(attachment != null);
        if (attachment != null) {
            buf.writeFloat(attachment);
        }
    }

    @Override
    public @Nullable Float read(@NotNull IAttachmentHolder holder, @NotNull RegistryByteBuf buf, @Nullable Float previousValue) {
        boolean hasData = buf.readBoolean();
        if (!hasData) {
            return null;
        }
        return buf.readFloat();
    }
}

package xyz.mcsls.starryMiningListRebuilt.Mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.mcsls.starryMiningListRebuilt.Event.PlayerPlaceBlockEvent;

/**
 * 监听方块物品的放置行为，用于统计放置榜。
 * <p>
 * 只在服务端、并且放置成功时计数，客户端以及发射器之类的非玩家放置不会被统计。
 */
@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;", at = @At("RETURN"))
    private void starrymininglist$countPlacement(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        InteractionResult result = cir.getReturnValue();
        if (result == null || !result.consumesAction()) {
            return;
        }

        Player player = context.getPlayer();
        if (player instanceof ServerPlayer) {
            PlayerPlaceBlockEvent.onPlace(player);
        }
    }
}

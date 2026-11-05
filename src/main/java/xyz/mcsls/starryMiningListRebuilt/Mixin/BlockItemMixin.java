package xyz.mcsls.starryMiningListRebuilt.Mixin;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
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

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("RETURN"))
    private void starrymininglist$countPlacement(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        ActionResult result = cir.getReturnValue();
        if (result == null || !result.isAccepted()) {
            return;
        }

        if (context.getPlayer() instanceof ServerPlayerEntity) {
            PlayerPlaceBlockEvent.onPlace((ServerPlayerEntity) context.getPlayer());
        }
    }
}

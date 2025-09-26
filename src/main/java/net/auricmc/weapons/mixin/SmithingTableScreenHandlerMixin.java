// package net.auricmc.weapons.mixin;

// import net.minecraft.screen.SmithingScreenHandler;
// import net.minecraft.item.ItemStack;
// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// @Mixin(SmithingScreenHandler.class)
// public class SmithingTableScreenHandlerMixin {

//     @Inject(
//         method = "canTakeOutput", // Changed to correct method name
//         at = @At("RETURN"),
//         cancellable = true
//     )
//     private void allowCustomItemsInSmithingTable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
//         if (stack != null && !stack.isEmpty()) {
//             String itemId = stack.getItem().toString();
//             if (itemId.contains("auricweapons:diamond_")) {
//                 cir.setReturnValue(true);
//             }
//         }
//     }
// }